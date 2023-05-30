package example.micronaut.page;

import example.micronaut.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FooConnectionRepository implements Iterator<Foo>, Iterable<Foo> {

	private static final Logger LOG = LoggerFactory.getLogger(FooConnectionRepository.class);

	private final int size;
	private int currentCount = 0;

	public FooConnectionRepository(int size) {
		this.size = size;
	}
	@Override
	public boolean hasNext() {
		return currentCount < size;
	}

	@Override
	public Foo next() {
		if (hasNext()) {
			currentCount++;

			if (currentCount % 10 == 0) {
				LOG.debug("Sent {} Foos...", currentCount);
			}
			return new Foo("Blue #" + currentCount, currentCount, "flub");
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public Iterator<Foo> iterator() {
		return this;
	}
}
