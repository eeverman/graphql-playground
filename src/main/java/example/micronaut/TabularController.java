package example.micronaut;

import example.micronaut.connection.PageInfo;
import example.micronaut.connection.SimplePageInfo;
import graphql.*;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.apache.commons.csv.*;
import org.dataloader.DataLoaderRegistry;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller("/tabular")
public class TabularController {

	@Inject
	DataLoaderRegistry dataLoaderRegistry;

	/** GQL standard name for element containing the requested data */
	final String GQL_DATA_KEY = "data";

	/** Name (based on the query) of the key containing the user request data.  Will be a child of 'data' element.  */
	final String GQL_USER_DATA_ROOT = "narrowResultConnection";

	final String GQL_QUERY = "{\n" +
			"  narrowResultConnection(first: 1000 filter: {\n" +
			"    countycode: \"US%3A27%3A123\"\n" +
			"    siteType: \"Stream\"\n" +
			"    characteristicType: \"Inorganics%2C%20Major%2C%20Non-metals\"\n" +
			"    startDateLo: \"01-01-2000\"\n" +
			"    startDateHi: \"01-01-2005\"\n" +
			"    providers: \"STORET\"\n" +
			"  }) {\n" +
			"    pageInfo {\n" +
			"      hasNextPage\n" +
			"      endCursor\n" +
			"    }\n" +
			"    nodes {\n" +
			"      OrganizationIdentifier\n" +
			"      ResultMeasureValue\n" +
			"      ResultMeasure_MeasureUnitCode\n" +
			"      MeasureQualifierCode\n" +
			"      MonitoringLocationIdentifier\n" +
			"      ActivityStartDate\n" +
			"    \tActivityStartTime_Time\n" +
			"\t\t\tActivityStartTime_TimeZoneCode\n" +
			"      monitoringLocation {MLOC_ID, MLOC_NAME, MLOC_TRIBAL_LAND_YN, MLOC_TRIBAL_LAND_NAME}\n" +
			"    }\n" +
			"  }\n" +
			"}";

	GraphQL graphQl;

	public TabularController(GraphQL graphQl) {
		this.graphQl = graphQl;
	}

	@Get("/result")
	@ExecuteOn(TaskExecutors.IO)
	@Produces(MediaType.TEXT_CSV)
	public String result() throws IOException {

		StringBuilder output = new StringBuilder();

		CSVFormat format = CSVFormat.Builder.create().setDelimiter(',').setQuote('\"').setRecordSeparator("\r\n")
				.setIgnoreEmptyLines(true).setDuplicateHeaderMode(DuplicateHeaderMode.ALLOW_ALL).
				setQuoteMode(QuoteMode.ALL_NON_NULL).build();

		final CSVPrinter csvPrinter = new CSVPrinter(output, format);

		ExecutionResult gqlResult = createGqlRequest(GQL_QUERY, null, Collections.emptyMap(), dataLoaderRegistry);

		Map<String, Object> gqlData = getAsMap(gqlResult.getData());
		Map<String, Object> userData = getChildAsMap(gqlData, GQL_USER_DATA_ROOT);

		PageInfo pageInfo = getPageInfo(userData);
		List<Map<String, Object>> nodes = getNodes(userData);

		// Write headers
		writeHeaders(csvPrinter, nodes.get(0));
		writeDataNodes(csvPrinter, nodes);

		csvPrinter.close();

		return csvPrinter.getOut().toString();
	}

	protected ExecutionResult createGqlRequest(String query, String operationName, Map<String, Object> variables, DataLoaderRegistry dataLoaderRegistry) {
		ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
				.query(query)
				.operationName(operationName)
				.variables(variables);
		if (dataLoaderRegistry != null) {
			executionInputBuilder.dataLoaderRegistry(dataLoaderRegistry);
		}

		ExecutionInput executionInput = executionInputBuilder.build();

		return graphQl.execute(executionInput);
	}

	protected void writeHeaders(CSVPrinter csvPrinter, Map<String, Object> rowMap) {
		writeHeaderMap(csvPrinter, rowMap);
		try {
			csvPrinter.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	protected void writeHeaderMap(CSVPrinter csvPrinter, Map<String, Object> rowMap) {
		rowMap.entrySet().stream().forEach(v -> {

			try {
				if (v.getValue() instanceof Map) {
					writeHeaderMap(csvPrinter, (Map<String, Object>)v.getValue());
				} else {
					csvPrinter.print(v.getKey());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		});
	}

	protected void writeDataNodes(CSVPrinter csvPrinter, List<Map<String, Object>> nodes) {
		nodes.stream().forEach(n -> {
			writeMap(csvPrinter, n);
			try {
				csvPrinter.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	protected void writeMap(CSVPrinter csvPrinter, Map<String, Object> rowMap) {
		rowMap.values().stream().forEach(v -> {

			try {
				if (v instanceof Map) {
					writeMap(csvPrinter, (Map<String, Object>)v);
				} else {
					csvPrinter.print(v);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		});
	}

	protected static Map<String, Object> getChildAsMap(Map<String, Object> parent, String childName) {
		Object o = parent.get(childName);
		return (Map<String, Object>)o;
	}

	protected static Map<String, Object> getAsMap(Object aMapAsAnObject) {
		return (Map<String, Object>)aMapAsAnObject;
	}

	protected static List<Map<String, Object>> getChildAsListOfMaps(Map<String, Object> parent, String childName) {
		return (List<Map<String, Object>>)(parent.get(childName));
	}

	protected static PageInfo getPageInfo(Map<String, Object> gqlData) {
		Map<String, Object> pageInfo = getChildAsMap(gqlData, "pageInfo");
		boolean hasNext = (Boolean)(pageInfo.get("hasNextPage"));
		String endCursor = pageInfo.get("endCursor").toString();

		return new SimplePageInfo(hasNext, endCursor);
	}

	protected static List<Map<String, Object>> getNodes(Map<String, Object> gqlData) {
		return getChildAsListOfMaps(gqlData, "nodes");
	}
}