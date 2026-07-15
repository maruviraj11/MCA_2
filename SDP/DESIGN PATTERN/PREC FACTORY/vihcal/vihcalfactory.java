public class vihcalfactory {


    public static vihcal gettype(String vihcal)
    {
       // String rs;

        if(vihcal.equalsIgnoreCase("car"))
        {
            return new car();
        } 
        else if(vihcal.equalsIgnoreCase("bike"))
        {
            return new bike();
        }
        else if(vihcal.equalsIgnoreCase("track"))
        {
            return new track();
        }
        else
        {
            return null;

        }
        
    }
    
}
