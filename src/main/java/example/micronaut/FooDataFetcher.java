package example.micronaut;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class FooDataFetcher implements DataFetcher<Iterable<Foo>> {

    private static final Logger LOG = LoggerFactory.getLogger(DataFetcher.class);


    @Override
    public Iterable<Foo> get(DataFetchingEnvironment env) {
        return findAll();
    }


    public Iterable<Foo> findAll() {
        return new FooCreator(5000000);
    }

    static class FooCreator implements Iterator<Foo>, Iterable<Foo> {

        private final int size;
        private int currentCount = 0;

        public FooCreator(int size) {
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

                if (currentCount % 1000 == 0) {
                    LOG.info("Sent {} Foos...", currentCount);
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

}
