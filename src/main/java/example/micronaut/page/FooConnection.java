package example.micronaut.page;

import java.util.List;

public class FooConnection<N> {

	private final List<FooEdge<N>> edges;
	private final PageInfo pageInfo;

	public FooConnection(List<FooEdge<N>> edges, PageInfo pageInfo) {
		this.edges = edges;
		this.pageInfo = pageInfo;
	}

	public List<FooEdge<N>> getEdges() {
		return edges;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
