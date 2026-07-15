public class propertytaxFactory {

    public static propertytax gettax(String city)
    {
        propertytax rs;
        
      
        if(city.equalsIgnoreCase("ahemdabad"))
        {
            rs = new ahemdabad();
        }
        else if(city.equalsIgnoreCase("rajkot"))
        {
            rs =  new rajkot();
        }
        else if(city.equalsIgnoreCase("surat"))
        {
            rs =  new surat();
        }
        else{
            return null;
        }
        return rs;

    }
    
}
