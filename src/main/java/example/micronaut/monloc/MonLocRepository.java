package example.micronaut.monloc;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.sql.*;
import java.util.*;

import static io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES;

@JdbcRepository(dialect = POSTGRES) // <1>
public class MonLocRepository implements GenericRepository<Map<String, Object>, String> {

    private static final Logger LOG = LoggerFactory.getLogger(MonLocRepository.class);

    final DataSource dataSource;

    private final String ID_FIELD_NAME = "full_site_id";


    private final String FIND_BY_ID_IN_QUERY = "select ml.*, org.\"ORG_ID\" || '-' || ml.\"MLOC_ID\" as full_site_id " +
            "from wqx_dump.\"MONITORING_LOCATION\" ml inner join wqx_dump.\"ORGANIZATION\" org on ml.\"ORG_UID\" = org.\"ORG_UID\" " +
            "where org.\"ORG_ID\" || '-' || ml.\"MLOC_ID\" = ANY (?)";

//    private final String FIND_BY_ID_IN_QUERY = "select * from wqx_dump.\"MONITORING_LOCATION\" where \"MLOC_ID\" = any (?)";

    private final String FIND_BY_ID_QUERY = "select * from wqx_dump.\"MONITORING_LOCATION\" where \"MLOC_ID\" = ?";

    public MonLocRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    Right now the @ReadOnly is a work-around for a Transaction issue that I don't fully
    understand in Micronaut.  I'm bending the normal flow a bit in this class and it
    seems to need this particular annotation.  Here is discussion:
    https://github.com/micronaut-projects/micronaut-data/issues/1293
     */

    @ReadOnly
    public Map<String, Map<String, Object>> findByIdIn(Collection<String> ids) {

        LOG.debug("findByIdIn for ids: '{}'", ids);

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(FIND_BY_ID_IN_QUERY);

            Array array = st.getConnection().createArrayOf("VARCHAR", ids.toArray());

            st.setArray(1, array);

            ResultSet rs = st.executeQuery();
            ResultSetMetaData md = rs.getMetaData();

            Map<String, Map<String, Object>> records = new HashMap<>();

            while (rs.next()) {

                Map<String, Object> record = new HashMap<String, Object>();

                for (int i = 1; i <= md.getColumnCount(); i++) {
                    record.put(md.getColumnName(i), rs.getObject(i));
                }

                LOG.trace("Found record: '{}'", record.get(ID_FIELD_NAME));

                records.put(record.get(ID_FIELD_NAME).toString(), record);
            }

            LOG.debug("Found {} records", records.size());

            rs.close();

            return records;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    @ReadOnly
    public Optional<Map<String, Object>> findById(String id) {

        LOG.debug("findById for ids: '{}'", id);

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(FIND_BY_ID_QUERY);
            st.setString(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                ResultSetMetaData md = rs.getMetaData();

                Map<String, Object> map = new HashMap<String, Object>();

                for (int i = 0; i < md.getColumnCount(); i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }

                if (rs.next()) {
                    LOG.warn("Unexpected extra result found for id '{}'", id);
                }

                rs.close();

                return Optional.of(map);

            } else {
                LOG.debug("findById: '{}' NO RECORD FOUND", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
