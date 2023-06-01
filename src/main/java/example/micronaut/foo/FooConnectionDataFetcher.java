package example.micronaut.foo;

import example.micronaut.connection.*;
import example.micronaut.connection.plumbing.*;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class FooConnectionDataFetcher extends AbstractConnectionDataFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(FooConnectionDataFetcher.class);

    @Override
    protected IterableRepository<Map<String, Object>> getNewRepository(DataFetchingEnvironment env) {
        return new FooMapConnectionRepository(1000);
    }

    @Override
    protected String getConnectionName() {
        return "Foo";
    }

}
