public class windowfactory extends abstractfactory {

   
    public button createbutton()
    {
        return new windowbutton();
    }

 
    public check createcheckbox()
    {
        return new windowcheck();
    }
    
}
