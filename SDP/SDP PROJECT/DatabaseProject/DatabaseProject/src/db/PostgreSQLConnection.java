package db;

public class PostgreSQLConnection extends AbstractDatabaseConnection {

    private PostgreSQLConnection() {}

    public static PostgreSQLConnection getInstance() {
        if (instance == null) {
            instance = new PostgreSQLConnection();
        }
        return (PostgreSQLConnection) instance;
    }
}
