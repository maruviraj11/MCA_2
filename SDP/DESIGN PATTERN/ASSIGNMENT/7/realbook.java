public class realbook  implements book
{
    private String filename;

    public realbook(String filename)
    {
        this.filename=filename;
        loadfiledisk();
    }

    public void loadfiledisk()
    {
        System.out.println("this is a cloud based load file on disk.."+filename);
    }
  public String display()
    {
       if(filename.equalsIgnoreCase("java"))
       {
         return "this is a open the book of the cntent of java book";
       }
       else if(filename.equalsIgnoreCase("c++"))
       {
            return " this is a open the book of the content of c++ book";
       }
       else if(filename.equalsIgnoreCase("orecal"))
       {
            return "this is a open the book of the content of orecal book";
       }
       else
       {
            return "book is not found";
       }

    }
}