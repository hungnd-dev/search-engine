package vn.danghung.dev.global;

import java.io.File;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
public class ConfigInfo {
    private static final Config config = ConfigFactory.parseFile(new File("conf.properties"));
    public static final String MYSQL_JDBC_URL = config.getString("mysql.jdbc.url");
    public static final String MYSQL_USERNAME = config.getString("mysql.username");
    public static final String MYSQL_PASSWORD = config.getString("mysql.password");

    public static final String INDEX_DIR = config.getString("indexDir");
}
