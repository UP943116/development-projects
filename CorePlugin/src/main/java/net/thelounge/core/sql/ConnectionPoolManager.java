package net.thelounge.core.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import net.thelounge.core.CorePlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {


    private HikariDataSource dataSource;

    private final String hostname;

    private final int port;

    private final String database;

    private final String username;

    private final String password;

    private final int minimumConnections;

    private final int maximumConnections;

    private final long connectionTimeout;

    private final long idleTimeout;

    private final long maxLifetime;

    private boolean failed = false;

    private final CorePlugin corePlugin;

    public ConnectionPoolManager(FileConfiguration fileConfiguration, CorePlugin corePlugin) {
        hostname = fileConfiguration.getString("host");
        port = fileConfiguration.getInt("port");
        database = fileConfiguration.getString("database");
        username = fileConfiguration.getString("username");
        password = fileConfiguration.getString("password");
        this.corePlugin = corePlugin;
        minimumConnections = 3;
        maximumConnections = 6;
        connectionTimeout = 5000;
        idleTimeout = 600000;
        maxLifetime = 1800000;
        setupPool();

    }

    private void setupPool() {
        try {
            HikariConfig config = new HikariConfig();
            config.setPoolName("CorePlugin_SQL");
            config.setDataSourceClassName("org.mariadb.jdbc.MySQLDataSource");
            config.setUsername(username);
            config.setPassword(password);
            config.setMinimumIdle(minimumConnections);
            config.setMaximumPoolSize(maximumConnections);
            config.setConnectionTimeout(connectionTimeout);
            config.setMaxLifetime(maxLifetime);
            config.setIdleTimeout(idleTimeout);
            config.addDataSourceProperty("serverName", hostname);
            config.addDataSourceProperty("portNumber", port);
            config.addDataSourceProperty("databaseName", database);
            dataSource = new HikariDataSource(config);
            corePlugin.log("Successfully connected to database using MySQL!");
        } catch (HikariPool.PoolInitializationException e) {
            if(e.getMessage().toLowerCase().contains("access denied")) {
                corePlugin.log("Invalid SQL Credentials...");
            } else {
                e.printStackTrace();
            }
            failed = true;
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (!failed && dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public boolean hasFailed() {
        return failed;
    }

}

