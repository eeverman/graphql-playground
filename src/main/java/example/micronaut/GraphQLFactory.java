package example.micronaut;

import example.micronaut.foo.FooConnectionDataFetcher;
import example.micronaut.monloc.MonLocDataFetcher;
import wqp.result.narrowresult.NarrowResultConnectionDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaMissingError;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Factory // <1>
public class GraphQLFactory {

	@Singleton // <2>
	public GraphQL graphQL(ResourceResolver resourceResolver,
			FooDataFetcher fooDataFetcher,
			FooConnectionDataFetcher fooConnectionDataFetcher,
			NarrowResultConnectionDataFetcher narrowResultConnectionDataFetcher,
			MonLocDataFetcher monLocDataFetcher) {
		SchemaParser schemaParser = new SchemaParser();
		SchemaGenerator schemaGenerator = new SchemaGenerator();

		// Load the schema
		InputStream schemaDefinition = resourceResolver
				.getResourceAsStream("classpath:schema.graphqls")
				.orElseThrow(SchemaMissingError::new);

		// Parse the schema and merge it into a type registry
		TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
		typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(schemaDefinition))));

		// Create the runtime wiring.
		RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()

				.type("Query", typeWiring -> typeWiring
						.dataFetcher("narrowResultConnection", narrowResultConnectionDataFetcher))

				.type("NarrowResult", typeWiring -> typeWiring.dataFetcher("monitoringLocation", monLocDataFetcher))

				/* Simple Foo example */
				.type("Query", typeWiring -> typeWiring
						.dataFetcher("foos", fooDataFetcher))

				.type("Query", typeWiring -> typeWiring
						.dataFetcher("fooConnection", fooConnectionDataFetcher))
				.build();

		// Create the executable schema.
		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

		// Return the GraphQL bean.
		return GraphQL.newGraphQL(graphQLSchema).build();
	}
}
