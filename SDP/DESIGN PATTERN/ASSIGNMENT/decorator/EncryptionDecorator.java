import java.util.Base64;

public class EncryptionDecorator extends DataSourceDecorator {

    public EncryptionDecorator(DataSource source) {
        super(source);
    }

    public void writeData(String data) {

        String encoded = Base64.getEncoder().encodeToString(data.getBytes());

        wrappee.writeData(encoded);
    }

    public String readData() {

        String data = wrappee.readData();

        byte[] decoded = Base64.getDecoder().decode(data);

        return new String(decoded);
    }
}