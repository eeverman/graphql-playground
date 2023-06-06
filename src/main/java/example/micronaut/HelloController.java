package example.micronaut;

import graphql.GraphQL;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/csv")
public class HelloController {

	GraphQL graphQl;

	public HelloController(GraphQL graphQl) {
		this.graphQl = graphQl;
	}

	@Get
	@Produces(MediaType.TEXT_PLAIN)
	public String index() {

		return "Hello World";
	}
}