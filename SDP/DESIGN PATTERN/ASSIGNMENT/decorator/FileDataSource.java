import java.io.*;

public class FileDataSource implements DataSource {

    private String fileName;

    public FileDataSource(String fileName) {
        this.fileName = fileName;
    }

    public void writeData(String data) {

        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(data);
            fw.close();
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String readData() {

        String data = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;

            while((line = br.readLine()) != null) {
                data += line;
            }

            br.close();
        } 
        catch(Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}