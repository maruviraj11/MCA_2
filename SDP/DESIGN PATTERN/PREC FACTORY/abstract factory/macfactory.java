public class macfactory extends abstractfactory {
    
    public button createbutton()
    {
        return new macbutton();
    }
    public checkbox  createCheckbox()
    {
        return new maccheckbox();
    }
}
