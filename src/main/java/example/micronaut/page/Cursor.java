package example.micronaut.page;

public class Cursor {
	private final String name;

	private Object repo;

	public Cursor(final String name, Object repository) {
		this.name = name;
		this.repo = repository;
	}

	public String getName() {
		return name;
	}

	public Object getRepository() {
		return repo;
	}
}
