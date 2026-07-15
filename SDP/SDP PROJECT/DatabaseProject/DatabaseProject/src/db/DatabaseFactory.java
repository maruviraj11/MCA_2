package db;

public class DatabaseFactory {

    public static DatabaseConnection getDatabase(String type) {

        switch (type.toLowerCase()) {
            case "mysql":
                return MySQLConnection.getInstance();
            case "postgresql":
                return PostgreSQLConnection.getInstance();
            case "oracle":
                return OracleConnection.getInstance();
            default:
                return null;
        }
    }
}
