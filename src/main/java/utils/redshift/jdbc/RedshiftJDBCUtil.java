package utils.redshift.jdbc;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.redshift.AmazonRedshift;
import com.amazonaws.services.redshift.AmazonRedshiftClient;
import entities.Log;
import exceptions.NoConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import configurations.RedshiftConfigurator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class consists of methods that assigned to work with Redshift.
 * You shouldn't create connection before calling other methods.Class checks the connection and creates it if it's needed.
 * If you don't open connection before some operations then there will be an error(log will notify you about it).
 * To create connection used JDBC 4.0 and Postgresql driver.
 * There is a method that inserts the log into table as well.
 * As Redshift is a relation database to process the data in the tables, we use simple SQL queries.
 * Also they are used for table creation (that have constant structure).
 * The structure is constant because we need to save only logs.
 * This class includes also methods that make group queries to Redshift (that is its strong side).
 */
public class RedshiftJDBCUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedshiftJDBCUtil.class);

    //redshift client and endpoint
    private static final AmazonRedshift redshift;
    private static Connection conn;

    /**
     * The initialization of AmazonRedshiftClient.
     */
    static {
        redshift = new AmazonRedshiftClient(new ProfileCredentialsProvider());
        redshift.setEndpoint(RedshiftConfigurator.getEndpoint());
    }

    /**
     * This method is used to create  connection with Redshift cluster.
     * It should be called first before all the other operations.
     */
    public static void createConnection() throws NoConnectionException {
        try {
            //Dynamically load driver at runtime.
            //Redshift JDBC 4.1 driver: com.amazon.redshift.jdbc41.Driver
            //Redshift JDBC 4 driver: com.amazon.redshift.jdbc4.Driver
            Class.forName("org.postgresql.Driver");
            //Open a connection and define properties.
            System.out.println("Connecting to database...");
            final Properties props = new Properties();
            //Uncomment the following line if using a keystore.
            //props.setProperty("ssl", "true");
            props.setProperty("user", RedshiftConfigurator.getMasterUsername());
            props.setProperty("password", RedshiftConfigurator.getMasterUserPassword());
            conn = DriverManager.getConnection(RedshiftConfigurator.getDbURL(), props);
            LOG.info("\t Redshift: Connection successfully created");
        } catch (SQLException | ClassNotFoundException e) {
            LOG.error("\t Redshift: Connection is not created " + e.toString());
            throw new NoConnectionException("Connection isn't created");

        }
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                LOG.info("\t Redshift: Connection closed");
            } else {
                LOG.error("\t Redshift: Connection cannot be closed, because It's not created.11'");
            }
        } catch (SQLException sqle) {
            LOG.error("\t Redshift: Connection can not be closed");
        }
    }

    /**
     * Method creates table with a permanent structure that describes everything inside of it.
     * This table is assigned to save only logs.
     *
     * @param tableName -name of table that we want to create.
     */
    public static void createTableForLogs(final String tableName) {
        if (checkConnection()) {
            final String tableConstructor = " (" +
                    "id integer CONSTRAINT key PRIMARY KEY," +
                    "time varchar(40)," +
                    "userid integer," +
                    "key varchar(70)," +
                    "value varchar(70));";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE " + tableName + tableConstructor);
                LOG.info("\t Redshift: Table " + tableName + " successfully created");
            } catch (SQLException sqle) {
                LOG.error("\t Redshift: Table " + tableName + " can not be created \n" + sqle.toString());
            } catch (NullPointerException npe) {
                LOG.error("\t Redshift: Table " + tableName + " can not be created. No connection.");
            }
        } else {
            LOG.error("Redshift: No connection");
        }
    }

    /**
     * This method deletes a table with this name from database.
     *
     * @param tableName-it's a name of a table that will be deleted.
     */
    public static void deleteTable(final String tableName) {
        if (checkConnection()) {
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DROP TABLE " + tableName);
                LOG.info("\t Redshift: Table " + tableName + " successfully deleted");
            } catch (SQLException sqsle) {
                LOG.error("\t Redshift: Table " + tableName + " can not be deleted \n" + sqsle.toString());
            }
        }
    }

    /**
     * This method inserts an object of log into a table, which name is tableName.
     *
     * @param tableName - it's a name of a table which will be used for inserting of log.
     * @param log       - log that will be inserted.
     */
    public static void insertLog(final String tableName, final Log log) {
        if (checkConnection()) {
            try {
                Statement stmt = conn.createStatement();
                if (checkConnection()) {
                    stmt.executeUpdate("insert into " + tableName + " values" +
                            " (" + log.getId() + ", '" + log.getTime() + "'," + log.getUserId() + " ,'" + log.getKey() + "', '" + log.getValue() + "');");
                    LOG.info("\t Redshift: " + log + " into Table " + tableName + " successfully inserted");
                }
            } catch (SQLException sqsle) {
                LOG.error("\t Redshift: Table " + tableName + " can not be inserted " + sqsle.toString());
            }
        } else {
            LOG.error("Redshift: No connection");
        }
    }

    public static void insertLog(final Log log) {
        insertLog(RedshiftConfigurator.getDefaultTable(), log);
    }

    /**
     * This method returns all logs from the table.
     *
     * @param tableName- name of table that we get logs from
     * @return all logs from the table
     */
    public static ArrayList<Log> getAllLogsFromTable(final String tableName) {
        String query = "select * from " + tableName + ";";
        return processQuery(query);
    }

    /**
     * This method returns ArrayList of logs whose id is bounded.
     *
     * @param tableName- name of table that we get logs from
     * @param high       - high range of id
     * @param low        - low range of id
     * @return -list of logs that have id that is bounded [low..high]
     */
    public static ArrayList<Log> getLogsFromTableByIDRange(final String tableName, final long high, final long low) {
        final String query = "select * from " + tableName + " where id between " + high + " and " + low + ";";
        return processQuery(query);
    }

    /**
     * This method returns ArrayList of logs with defined field value.
     *
     * @param tableName- name of table that we get logs from
     * @param value-     It's get the logs that have value which equals value that we determined
     * @return -list of logs that have required value
     */
    public static ArrayList<Log> getLogsFromTableByValue(final String tableName, final String value) {
        final String query = "select * from " + tableName + " where value='" + value + "';";
        return processQuery(query);
    }

    /**
     * This method returns ArrayList of logs with defined field key.
     *
     * @param tableName - name of table that we get logs from
     * @param key       - key which must have all logs that we will got
     * @return -list of logs that have id that is bounded [low..high]
     */
    public static ArrayList<Log> getLogsFromTableByKey(final String tableName, final String key) {
        final String query = "select * from " + tableName + " where key='" + key + "';";
        return processQuery(query);
    }

    /**
     * Service methods
     * (which are used in main methods)
     */
    public static ArrayList<Log> processQuery(String query) {
        ArrayList<Log> listOfLogs = null;
        if (checkConnection()) {
            try {
                //Try a simple query.
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                //Get the data from the result set.
                listOfLogs = convertResultSetToList(rs);
                rs.close();
                stmt.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } finally {
            }
        } else {
            LOG.error("Redshift: No connection");
        }
        return listOfLogs;
    }

    private static ArrayList<Log> convertResultSetToList(ResultSet rs) {
        ArrayList<Log> listOfLogs = new ArrayList<>();
        try {
            while (rs.next()) {
                //Retrieve two columns.
                final Log log = new Log();
                log.setId(rs.getString("id"));
                log.setTime(rs.getString("time"));
                log.setUserId(Integer.parseInt(rs.getString("userid")));
                log.setKey(rs.getString("key"));
                log.setValue(rs.getString("value"));
                listOfLogs.add(log);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return listOfLogs;
    }

    /**
     * This method checks the connection status and if connection is not created method creates it.
     *
     * @return false if there is some problem with creating connection
     */
    private static boolean checkConnection() {
        if (conn == null) {
            try {
                createConnection();
            } catch (NoConnectionException nce) {
                nce.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        conn.close();
    }
}

