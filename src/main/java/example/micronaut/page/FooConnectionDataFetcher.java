package example.micronaut.page;

import example.micronaut.Foo;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class FooConnectionDataFetcher implements DataFetcher<FooConnection> {

    private static final Logger LOG = LoggerFactory.getLogger(FooConnectionDataFetcher.class);

    public static final int DEFAULT_NODE_COUNT = 100;

    /**
     * Argument name for how many records to return, i.e. The first X records.
     * Type Int value expected.
     */
    public static final String ARG_FIRST = "first";

    /**
     * Argument name for the cursor to start loading records after.
     * i.e. The records after cursor X.  'cursor X' would have been passed in a
     * the pageInfo of a previous response.
     * Type String value expected. */
    public static final String ARG_AFTER_CURSOR = "afterCursor";

    CursorRegistry cursorRegistry;

    public FooConnectionDataFetcher(final CursorRegistry cursorRegistry) {
        this.cursorRegistry = cursorRegistry;
    }

    @Override
    public FooConnection get(DataFetchingEnvironment env) {

        int nodeCount = env.getArgumentOrDefault(ARG_FIRST, DEFAULT_NODE_COUNT);
        String afterCursor = env.getArgument(ARG_AFTER_CURSOR);

        LOG.debug("Fetching for nodeCount {}  afterCursor {}", nodeCount, afterCursor);

        FooMapConnectionRepository repo = null;

        if (afterCursor != null) {
            Cursor cursor = cursorRegistry.get(afterCursor);
            if (cursor == null) {
                LOG.info("Cursor '{}' not found.", afterCursor);
                throw new RuntimeException(String.format("Cursor not found: " + afterCursor));
            }

            repo = (FooMapConnectionRepository) cursor.getRepository();

            LOG.debug("Found existing cursor '{}'", afterCursor);
        } else {
            repo = new FooMapConnectionRepository(1000);

            LOG.debug("New cursor '{}'", afterCursor);
        }

        Iterable<Map> it = findAll(repo, nodeCount);

        String cursorName = buildCursorName(repo);

        env.getGraphQlContext().put(PageInfo.KEY_HAS_NEXT_PAGE, Boolean.valueOf(repo.hasNext()));
        env.getGraphQlContext().put(PageInfo.KEY_END_CURSOR, cursorName);

        Cursor newCursor = new Cursor(cursorName, repo);
        if (afterCursor != null) cursorRegistry.remove(afterCursor);
        cursorRegistry.put(newCursor);

        PageInfo pageInfo = new PageInfo(repo.hasNext(), cursorName);

        FooConnection fc = new FooConnection(it, pageInfo);
        return fc;
    }

    String buildCursorName(FooMapConnectionRepository repo) {
        return "Foo-" + Long.toString(System.currentTimeMillis());
    }


    public Iterable<Map> findAll(FooMapConnectionRepository repo, int nodeCount) {
        List<Map> page = new ArrayList(nodeCount);

        int currentCount = 0;

        while (repo.hasNext() && currentCount < nodeCount) {
            page.add(repo.next());
            currentCount++;
        }

        LOG.debug("Found {} nodes (request page of {})", currentCount, nodeCount);

        return page;
    }


}
