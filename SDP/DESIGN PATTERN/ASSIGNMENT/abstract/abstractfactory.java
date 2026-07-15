public abstract class abstractfactory {

    public abstract button createbutton();
    public abstract check createcheckbox();

    public static abstractfactory createfactory(int ch) {
        if (ch == 1) {
            return new windowfactory();
        }
        if (ch == 2) {
            return new macfactory();
        }
        return null;  
    }
}
