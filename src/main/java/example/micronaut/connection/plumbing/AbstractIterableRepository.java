package example.micronaut.connection.plumbing;

import example.micronaut.foo.FooConnectionDataFetcher;
import example.micronaut.foo.FooMapConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractIterableRepository<T> implements IterableRepository<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractIterableRepository.class);

	public Iterable<T> buildNodes(int nodeCount) {
		List<T> page = new ArrayList(nodeCount);

		int currentCount = 0;

		while (hasNext() && currentCount < nodeCount) {
			page.add(next());
			currentCount++;
		}

		if (!hasNext()) {
			release();
		}

		LOG.debug("Found {} nodes (request page of {})", currentCount, nodeCount);

		return page;
	}
}
