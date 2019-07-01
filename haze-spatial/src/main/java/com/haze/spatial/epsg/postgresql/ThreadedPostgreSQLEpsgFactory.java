package com.haze.spatial.epsg.postgresql;

import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.epsg.ThreadedEpsgFactory;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.ds.common.BaseDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

public class ThreadedPostgreSQLEpsgFactory extends ThreadedEpsgFactory {
    public static final String CONFIGURATION_FILE = "EPSG-DataSource.properties";

    /** The schema name, or {@code null} if none. */
    private String schema;

    /** Creates a new instance of this factory. */
    public ThreadedPostgreSQLEpsgFactory() {
        this(null);
    }

    public ThreadedPostgreSQLEpsgFactory(final Hints hints) {
        super(hints, PRIORITY + 5);
    }

    /** Loads the {@linkplain #CONFIGURATION_FILE configuration file}. */
    private static Properties load() {
        final Properties p = new Properties();

        File file = null;
        try {
            file = Paths.get(ThreadedPostgreSQLEpsgFactory.class.getResource(CONFIGURATION_FILE).toURI()).toFile();
            //file = new File(new FileInputStream(ThreadedPostgreSQLEpsgFactory.class.getClassLoader().getResourceAsStream(CONFIGURATION_FILE))) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!file.isFile()) {
            File home = new File(System.getProperty("user.home", "."));
            file = new File(home, CONFIGURATION_FILE);
            if (!file.isFile()) {
                // support online testing
                File epsgFixtures = new File(new File(home, ".geotools"), "epsg");
                file = new File(epsgFixtures, "postgresql.properties");
                if (!file.isFile()) {
                    // Returns an empty set of properties.
                    return p;
                }
            }
        }
        try {
            final InputStream in = new FileInputStream(file);
            p.load(in);
            in.close();
        } catch (IOException exception) {
            Logging.unexpectedException(
                    "org.geotools.referencing.factory", DataSource.class, "<init>", exception);
            // Continue. We will try to work with whatever properties are available.
        }
        return p;
    }

    /** Returns a data source for the PostgreSQL database. */
    protected DataSource createDataSource() throws SQLException {
        DataSource candidate = super.createDataSource();
        if (candidate instanceof BaseDataSource) {
            // Any kind of DataSource from the PostgreSQL driver.
            return candidate;
        }
        final PGSimpleDataSource source = new PGSimpleDataSource();
        final Properties p = load();
        int portNumber;
        try {
            portNumber = Integer.parseInt(p.getProperty("portNumber", "5432"));
        } catch (NumberFormatException exception) {
            portNumber = 5432;
            Logging.unexpectedException(
                    "org.geotools.referencing.factory", DataSource.class, "<init>", exception);
        }
        source.setPortNumber(portNumber);
        source.setServerName(p.getProperty("serverName", "localhost"));
        source.setDatabaseName(p.getProperty("databaseName", "EPSG"));
        source.setUser(p.getProperty("user", "Geotools"));
        source.setPassword(p.getProperty("password", "Geotools"));
        source.setProperty("stringtype", "unspecified");
        //source.setProperty("schema", p.getProperty("schema", "public"));
        schema = p.getProperty("schema", null);
        return source;
    }

    /**
     * Returns the backing-store factory for PostgreSQL syntax.
     *
     * @param hints A map of hints, including the low-level factories to use for CRS creation.
     * @return The EPSG factory using PostgreSQL syntax.
     * @throws SQLException if connection to the database failed.
     */
    protected AbstractAuthorityFactory createBackingStore(final Hints hints) throws SQLException {
        final MyAnsiDialectEpsgFactory factory = new MyAnsiDialectEpsgFactory(hints, getDataSource());
        /*factory.setValidationQuery("select now()");*/
        if (schema != null) {
            factory.setSchema(schema);
        }
        return factory;
    }

    public String getSchema() {
        return this.schema;
    }
}
