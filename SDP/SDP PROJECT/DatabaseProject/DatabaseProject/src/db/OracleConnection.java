package db;

public class OracleConnection extends AbstractDatabaseConnection {

    private OracleConnection() {}

    public static OracleConnection getInstance() {
        if (instance == null) {
            instance = new OracleConnection();
        }
        return (OracleConnection) instance;
    }
}
