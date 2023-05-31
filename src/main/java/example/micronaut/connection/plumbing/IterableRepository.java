package example.micronaut.connection.plumbing;

import java.util.Iterator;

/**
 * Similar to a DAO, but able to iterate through individual results
 * based on criteria (passed in a constructor).
 *
 * Implementations should assume that a batch of results will be fetched,
 * then the Repository will wait for a brief interval (seconds), then
 * another batch will be fetched.  This will repeat until hasNext()
 * returns false or some level of inactivity has passed.
 *
 * When hasNext() returns false, release() should automatically be called
 * in the implementation to release resources.  Some monitor outside this
 * class may determine a request has been abandoned due to a timeout and
 * call release.
 *
 * @param <T>
 */
public interface IterableRepository<T> extends Iterator<T>, Iterable<T>  {

	/**
	 * Build an iterable populated from the underlying source.
	 *
	 * Calling this method moves forward through the underlying source,
	 * incrementally calling next() and hasNext(), and leaving the state of
	 * this class changed.
	 *
	 * @param nodeCount
	 * @return
	 */
	Iterable<T> buildNodes(int nodeCount);

	@Override
	default Iterator<T> iterator() {
		return this;
	}

	/**
	 * Called to release any open resources.
	 */
	void release();
}
