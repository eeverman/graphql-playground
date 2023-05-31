package example.micronaut.connection;

import java.util.Map;

/**
 * Simple Connection implementation that uses a Map instead of a domain object to represent the
 * key/value pairs of any object.
 *
 */
public class SimpleMapConnection implements Connection<Map<String, Object>> {

	private final Iterable<Map<String, Object>> nodes;
	private final PageInfo pageInfo;

	public SimpleMapConnection(final Iterable<Map<String, Object>> nodes, final PageInfo pageInfo) {
		this.nodes = nodes;
		this.pageInfo = pageInfo;
	}

	@Override
	public Iterable<Map<String, Object>> getNodes() {
		return nodes;
	}

	@Override
	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
