package example.micronaut.page;

import example.micronaut.Foo;

import java.util.Map;

public class FooConnection {

	private final Iterable<Map> nodes;
	private final PageInfo pageInfo;

	public FooConnection(Iterable<Map> nodes, PageInfo pageInfo) {
		this.nodes = nodes;
		this.pageInfo = pageInfo;
	}

	public Iterable<Map> getNodes() {
		return nodes;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
