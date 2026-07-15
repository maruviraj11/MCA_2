package db;

public class MySQLConnection extends AbstractDatabaseConnection {

    private MySQLConnection() {}

    public static MySQLConnection getInstance() {
        if (instance == null) {
            instance = new MySQLConnection();
        }
        return (MySQLConnection) instance;
    }
}
