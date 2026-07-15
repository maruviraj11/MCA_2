public class proxybook implements book {

    private String filename;
    private realbook realbook;


    public proxybook(String  filename)
    {
        this.filename = filename;
    }

    public String display()
    {
        if(realbook == null)
        {
            realbook = new realbook(filename);
        }
        return realbook.display();
    }
    
}
