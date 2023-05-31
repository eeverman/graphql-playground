package example.micronaut.connection;

/**
 * Public facing interface that could be directly mapped in a graphQL schema
 * to provide Connections of pages with a live cursor.
 *
 * Instances of this interface will be serialized back to the user to pass
 * query responses (nodes of data, pageInfo of metadata).
 *
 * @param <T>
 */
public interface Connection<T> {

	Iterable<T> getNodes();

	PageInfo getPageInfo();
}
