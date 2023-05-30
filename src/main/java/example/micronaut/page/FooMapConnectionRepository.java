package example.micronaut.page;

import example.micronaut.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FooMapConnectionRepository implements Iterator<Map>, Iterable<Map> {

	private static final Logger LOG = LoggerFactory.getLogger(FooMapConnectionRepository.class);

	private final int size;
	private int currentCount = 0;

	public FooMapConnectionRepository(int size) {
		this.size = size;
	}
	@Override
	public boolean hasNext() {
		return currentCount < size;
	}

	@Override
	public Map next() {
		if (hasNext()) {
			currentCount++;

			if (currentCount % 10 == 0) {
				LOG.debug("Sent {} Foos...", currentCount);
			}

			Map<String, Object> objectMap = new HashMap<>(4, 1.0f);

			objectMap.put("color", "Blue #" + currentCount);
			objectMap.put("size", currentCount);
			objectMap.put("flubber", "flub");

			return objectMap;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public Iterator<Map> iterator() {
		return this;
	}
}
