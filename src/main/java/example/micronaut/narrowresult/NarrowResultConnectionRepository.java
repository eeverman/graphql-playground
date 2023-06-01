package example.micronaut.narrowresult;

import example.micronaut.connection.plumbing.AbstractIterableRepository;
import org.apache.commons.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class NarrowResultConnectionRepository extends AbstractIterableRepository<Map<String, Object>> {

	private final Object PARSER_LOCK = new Object();

	private static final Logger LOG = LoggerFactory.getLogger(NarrowResultConnectionRepository.class);

	private final String url;
	private int currentCount = 0;

	protected CSVParser parser = null;

	public NarrowResultConnectionRepository(String url) {
		this.url = url;
	}

	protected CSVParser getParser() throws IOException {

		synchronized (PARSER_LOCK) {
			if (this.parser == null) {
				CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
				parser = CSVParser.parse(new URL(url), Charset.forName("UTF-8"), format);
			}
		}

		return parser;
	}

	@Override
	public boolean hasNext() {
		try {
			return getParser().iterator().hasNext();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map next() {
		if (hasNext()) {
			currentCount++;

			if (currentCount % 10 == 0) {
				LOG.debug("Sent {} Items...", currentCount);
			}

			try {
				CSVParser parser = getParser();

				CSVRecord record = parser.iterator().next();
				return readToMap(record, parser.getHeaderMap());

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} else {
			throw new NoSuchElementException();
		}
	}

	protected Map<String, Object> readToMap(CSVRecord record, Map<String, Integer> headerMap) {
		Map<String, Object> map = new HashMap<>(record.size(), 1.0f);

		if (headerMap == null) {
			return map;
		}
		headerMap.forEach((name, idx) -> {
			if (idx < record.values().length) {
				map.put(cleanFieldName(name), record.values()[idx]);
			}
		});

		return map;
	}

	public String cleanFieldName(String originalFieldName) {
		return originalFieldName.replace("/", "_");
	}

	@Override
	public void release() {
		if (parser != null) {
			try {
				parser.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}
