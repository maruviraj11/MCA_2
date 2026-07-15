public class proxyimage  implements image
{
    
    private realimage realImage;
    private String filename;

    public proxyimage(String filename) {
        this.filename = filename;
    }

    public void display() {

        if(realImage == null) {
            realImage = new realimage(filename);
        }

        realImage.display();
    }
}
