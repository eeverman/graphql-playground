package example.micronaut.connection;

public class SimplePageInfo implements PageInfo {

	private final boolean hasNextPage;
	private final String endCursor;

	public SimplePageInfo(boolean hasNextPage, String endCursor) {
		this.hasNextPage = hasNextPage;
		this.endCursor = endCursor;
	}

	@Override
	public boolean hasNextPage() {
		return hasNextPage;
	}

	@Override
	public String getEndCursor() {
		return endCursor;
	}
}
