package example.micronaut.connection.plumbing;

public class Cursor<T> {
	private final String name;

	private IterableRepository<T> repo;

	public Cursor(final String name, IterableRepository<T> repository) {
		this.name = name;
		this.repo = repository;
	}

	public String getName() {
		return name;
	}

	public IterableRepository<T> getRepository() {
		return repo;
	}
}
