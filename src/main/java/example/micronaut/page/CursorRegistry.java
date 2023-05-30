package example.micronaut.page;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Singleton
public class CursorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(CursorRegistry.class);

	private final HashMap<String, Cursor> cursors;

	public CursorRegistry() {
		this.cursors = new HashMap<String, Cursor>();
	}

	public void put(Cursor cursor) {

		LOG.debug("Put cursor {}.  Cursor count: {}", cursor.getName(), cursors.size());
		cursors.put(cursor.getName(), cursor);
	}

	public Cursor get(String name) {
		return cursors.get(name);
	}

	public Cursor remove(String name) {

		LOG.debug("Remove cursor {}.  Cursor count: {}", name, cursors.size());
		return cursors.remove(name);
	}
}
