package example.micronaut.narrowresult;

import example.micronaut.connection.plumbing.AbstractConnectionDataFetcher;
import example.micronaut.connection.plumbing.IterableRepository;
import example.micronaut.foo.FooMapConnectionRepository;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Singleton
public class NarrowResultConnectionDataFetcher extends AbstractConnectionDataFetcher {

	private static final Logger LOG = LoggerFactory.getLogger(NarrowResultConnectionDataFetcher.class);

	@Override
	protected IterableRepository<Map<String, Object>> getNewRepository(DataFetchingEnvironment env) {
		return new NarrowResultConnectionRepository(
				"https://www.waterqualitydata.us/data/Result/search?" +
						"statecode=US%3A27&countycode=US%3A27%3A123&siteType=Stream&" +
						"characteristicType=Inorganics%2C%20Major%2C%20Non-metals&" +
						"startDateLo=01-01-2000&startDateHi=01-01-2005&" +
						"mimeType=csv&zip=no&" +
						"dataProfile=narrowResult&providers=STORET");
	}

	@Override
	protected String getConnectionName() {
		return "NarrowResult";
	}

}
