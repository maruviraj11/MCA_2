public class realbook implements book {

    private String  filename;

    public realbook(String  filename)
    {
        this.filename = filename;
        loadfile();
    }
    public void loadfile()
    {
        System.out.println("this file is load the file");
    }

    public String display()
    {
       if(filename.equalsIgnoreCase("java"))
       {
            return "this is a java file so just a proxy summary of this book";
       }
       else if(filename.equalsIgnoreCase("python"))
       {
            return "this is a python file so just a proxt summary of this book";        
       }
       else if(filename.equalsIgnoreCase("web"))
       {
        return "this is a web  file so just a proxy summary of this book";
       }
       else
       {
        return "book not found valid choice";
       }
    }
    
}
