public class windowfactory extends abstractfactory {
    public button createbutton()
    {
        return new windowbutton();
    }
    public  checkbox  createCheckbox()
    {
        return new windowcheckbox();
    }
    
}
