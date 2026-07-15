package db;

import java.sql.*;

public abstract class AbstractDatabaseConnection implements DatabaseConnection {

    protected static Connection connection;
    protected static AbstractDatabaseConnection instance;

    @Override
    public void connect(String driver, String url, String username, String password) throws Exception {
        if (connection == null || connection.isClosed()) {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    @Override
    public void disconnect() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public int executeQuery(String query) throws Exception {
        Statement stmt = connection.createStatement();

        if (query.trim().toLowerCase().startsWith("select")) {

            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

            return 0;
        } else {
            return stmt.executeUpdate(query);
        }
    }
}
