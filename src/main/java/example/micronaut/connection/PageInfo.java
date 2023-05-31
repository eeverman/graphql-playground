package example.micronaut.connection;

public interface PageInfo {
	String KEY_HAS_NEXT_PAGE = "hasNextPage";
	String KEY_END_CURSOR = "endCursor";

	boolean hasNextPage();

	String getEndCursor();
}
