package example.micronaut;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;

import javax.validation.constraints.NotNull;

import static io.micronaut.data.annotation.GeneratedValue.Type.AUTO;

public class Foo {
	@Id // <2>
	@GeneratedValue(AUTO)
	private Long id;

	@NotNull
	private String color;

	private int size;

	private String flubber;

	public Foo(String color, int size, String flubber) {
		this.color = color;
		this.size = size;
		this.flubber = flubber;
	}

	public Long getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public int getSize() {
		return size;
	}

	public String getFlubber() {
		return flubber;
	}
}
