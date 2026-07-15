public class proxybook implements book {
    
    private realbook realbook;
    private String filename;

    public proxybook(String filename)
    {
        this.filename=filename;
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
