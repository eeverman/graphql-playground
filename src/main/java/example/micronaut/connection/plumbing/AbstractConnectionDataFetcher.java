package example.micronaut.connection.plumbing;

import example.micronaut.connection.*;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractConnectionDataFetcher implements DataFetcher<SimpleMapConnection> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConnectionDataFetcher.class);

    public static final int DEFAULT_NODE_COUNT = 100;

    protected CursorRegistry<Map<String, Object>> cursorRegistry;

    public AbstractConnectionDataFetcher(final CursorRegistry<Map<String, Object>> cursorRegistry) {
        this.cursorRegistry = cursorRegistry;
    }

    public AbstractConnectionDataFetcher() {
        this.cursorRegistry = new CursorRegistry<>();
    }

    /**
     * Called to create a new repository.  Must be overwritten.
     * @param env
     * @return
     */
    protected abstract IterableRepository<Map<String, Object>> getNewRepository(DataFetchingEnvironment env);

    /**
     * A user friendly name for th data connection.
     *
     * The name is prepended to the cursor name and is used in the logs to distinguish logs from different implementations.
     *
     * The cursor name shows up in the pageInfo:endCursor key for requesting pages of the results.
     * If unspecified, names look like this: {@code Cursor-282389421029}.
     * If you override the method to return {@code "Foo"}, cursors would look like: {@code Foo-282389421029}.
     *
     * @return A String that is prepended to the cursor name.
     */
    protected String getConnectionName() {
        return "Cursor";
    }

    @Override
    public SimpleMapConnection get(DataFetchingEnvironment env) {

        int nodeCount = env.getArgumentOrDefault(Connection.ARG_FIRST, DEFAULT_NODE_COUNT);
        String afterCursor = env.getArgument(Connection.ARG_AFTER_CURSOR);
        String connName = getConnectionName();

        LOG.debug("{}: Fetching for nodeCount {}  afterCursor {}", connName, nodeCount, afterCursor);

        IterableRepository<Map<String, Object>> repo = null;

        if (afterCursor != null) {
            Cursor<Map<String, Object>> cursor = cursorRegistry.get(afterCursor);
            if (cursor == null) {
                LOG.info("{}: Cursor '{}' not found.", connName, afterCursor);
                throw new RuntimeException(String.format(connName + ": Cursor not found: " + afterCursor));
            }

            repo = cursor.getRepository();

            LOG.debug("{}: Found existing cursor '{}'", connName, afterCursor);
        } else {
            repo = getNewRepository(env);

            LOG.debug("{}: New cursor created", connName);
        }

        Iterable<Map<String, Object>> it = repo.buildNodes(nodeCount);

        String cursorName = buildCursorName(repo);

        env.getGraphQlContext().put(PageInfo.KEY_HAS_NEXT_PAGE, Boolean.valueOf(repo.hasNext()));
        env.getGraphQlContext().put(PageInfo.KEY_END_CURSOR, cursorName);

        Cursor<Map<String, Object>> newCursor = new Cursor<>(cursorName, repo);

        if (afterCursor != null) cursorRegistry.remove(afterCursor);
        cursorRegistry.put(newCursor);

        PageInfo pageInfo = new SimplePageInfo(repo.hasNext(), cursorName);

        SimpleMapConnection fc = new SimpleMapConnection(it, pageInfo);
        return fc;
    }

    String buildCursorName(IterableRepository<?> repo) {
        return getConnectionName() + "-" + System.currentTimeMillis();
    }


}
