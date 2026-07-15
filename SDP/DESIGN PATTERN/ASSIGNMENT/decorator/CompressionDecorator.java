public class CompressionDecorator extends DataSourceDecorator {

    public CompressionDecorator(DataSource source) {
        super(source);
    }

    public void writeData(String data) {

        String compressed = data.replaceAll(" ", "");

        wrappee.writeData(compressed);
    }

    public String readData() {

        String data = wrappee.readData();

        return data;
    }
}