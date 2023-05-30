package example.micronaut.page;

public class PageInfo {

	public static final String KEY_HAS_NEXT_PAGE = "hasNextPage";

	public static final String KEY_END_CURSOR = "endCursor";

	private final boolean hasNextPage;
	private final String endCursor;

	public PageInfo(boolean hasNextPage, String endCursor) {
		this.hasNextPage = hasNextPage;
		this.endCursor = endCursor;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public String getEndCursor() {
		return endCursor;
	}
}
