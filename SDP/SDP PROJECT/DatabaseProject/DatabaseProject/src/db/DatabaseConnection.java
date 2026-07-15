package db;

import java.sql.*;

public interface DatabaseConnection {
    public void connect(String driver, String url, String username, String password) throws Exception;

    public void disconnect() throws Exception;

    public int executeQuery(String query) throws Exception;
}
