package com.acelerazg.persistency

import com.acelerazg.utils.EnvHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

@CompileStatic
class DatabaseHandler {
    private static final String URL = EnvHandler.get("DB_URL")
    private static final String USER = EnvHandler.get("DB_USER")
    private static final String PASSWORD = EnvHandler.get("DB_PASSWORD")

    static boolean testConnection() {
        try {
            getConnection()
        } catch (Exception e) {
            println(e)
            println(e.getStackTrace())
            return false
        }
        return true
    }

    static Connection getConnection() throws SQLException {
        DriverManager.getConnection(URL, USER, PASSWORD)
    }

    static void closeQuietly(AutoCloseable... resources) {
        resources.each { r ->
            if (r != null) {
                try {
                    r.close()
                } catch (Exception ignored) {
                }
            }
        }
    }

    static void runSql(String path) {
        if (!testConnection()) return

        InputStream inputStream = DatabaseHandler.class.classLoader.getResourceAsStream(path)

        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: $path")
        }

        String sql = inputStream.text

        Connection connection = null

        try {
            connection = getConnection()
            connection.autoCommit = false

            sql.split(';').each { stmt ->
                stmt = stmt.trim()
                if (stmt) {
                    connection.createStatement().withCloseable { s -> s.execute(stmt)
                    }
                }
            }

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            closeQuietly(connection)
        }
    }
}