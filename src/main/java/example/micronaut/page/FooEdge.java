package example.micronaut.page;

public class FooEdge<N> {
	private final N node;

	public FooEdge(N node) {
		this.node = node;
	}

	public N getNode() {
		return node;
	}
}
