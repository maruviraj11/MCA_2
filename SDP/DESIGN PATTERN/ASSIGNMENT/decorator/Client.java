public class Client {

    public static void main(String[] args) {

        String data = "Decorator Pattern Example";

        DataSource source =
                new CompressionDecorator(
                        new EncryptionDecorator(
                                new FileDataSource("data.txt")));

        source.writeData(data);

        System.out.println("Original Data: " + data);

        String result = source.readData();

        System.out.println("Read Data: " + result);
    }
}