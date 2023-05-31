package example.micronaut.connection.plumbing;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Singleton
public class CursorRegistry<T> {

	private static final Logger LOG = LoggerFactory.getLogger(CursorRegistry.class);

	private final HashMap<String, Cursor<T>> cursors;

	public CursorRegistry() {
		this.cursors = new HashMap<String, Cursor<T>>();
	}

	public void put(Cursor cursor) {

		LOG.debug("Put cursor {}.  Cursor count: {}", cursor.getName(), cursors.size());
		cursors.put(cursor.getName(), cursor);
	}

	public Cursor<T> get(String name) {
		return cursors.get(name);
	}

	public Cursor<T> remove(String name) {

		LOG.debug("Remove cursor {}.  Cursor count: {}", name, cursors.size());
		return cursors.remove(name);
	}
}
