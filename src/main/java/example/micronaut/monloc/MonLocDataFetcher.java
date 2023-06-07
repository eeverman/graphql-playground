package example.micronaut.monloc;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.dataloader.DataLoader;

import java.util.Map;
import java.util.concurrent.CompletionStage;

@Singleton
public class MonLocDataFetcher implements DataFetcher<CompletionStage<Map<String, Object>>> {

    @Override
    public CompletionStage<Map<String, Object>> get(DataFetchingEnvironment environment) {
        Map<String, Object> result = environment.getSource();
        DataLoader<String, Map<String, Object>> dl = environment.getDataLoader("monLoc");
        Object mlId = result.get("MonitoringLocationIdentifier");
        return dl.load(mlId.toString());
    }
}