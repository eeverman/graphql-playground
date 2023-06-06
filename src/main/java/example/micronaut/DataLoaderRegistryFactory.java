package example.micronaut;

import example.micronaut.monloc.MonLocDataLoader;
import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.http.scope.RequestScope;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory // <1>
public class DataLoaderRegistryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoaderRegistryFactory.class);

    @SuppressWarnings("unused")
    @RequestScope // <2>
    public DataLoaderRegistry dataLoaderRegistry(MonLocDataLoader monLocDataLoader) {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("monLoc", DataLoader.newMappedDataLoader(monLocDataLoader));

        LOG.trace("Created new data loader registry");

        return dataLoaderRegistry;
    }

}