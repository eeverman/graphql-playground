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

	/**
	 * Argument name for how many records to return, i.e. The first X records.
	 * Type Int value expected.
	 */
	String ARG_FIRST = "first";
	/**
	 * Argument name for the cursor to start loading records after.
	 * i.e. The records after cursor X.  'cursor X' would have been passed in a
	 * the pageInfo of a previous response.
	 * Type String value expected. */
	String ARG_AFTER_CURSOR = "afterCursor";

	Iterable<T> getNodes();

	PageInfo getPageInfo();
}
