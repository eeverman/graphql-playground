package example.micronaut.monloc;

import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toMap;

@Singleton // <1>
public class MonLocDataLoader implements MappedBatchLoader<String, Map<String, Object>> {

    private final MonLocRepository repository;
    private final ExecutorService executor;

    public MonLocDataLoader(
            MonLocRepository repository,
            @Named(TaskExecutors.IO) ExecutorService executor // <2>
    ) {
        this.repository = repository;
        this.executor = executor;
    }

    @Override
    public CompletionStage<Map<String, Map<String, Object>>> load(Set<String> keys) {
        return CompletableFuture.supplyAsync(() -> repository
                        .findByIdIn(keys), executor
        );
    }

}
