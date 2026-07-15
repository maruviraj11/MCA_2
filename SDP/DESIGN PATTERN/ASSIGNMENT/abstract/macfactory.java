public class macfactory extends abstractfactory {

    public button createbutton() {
        return new macbutton();
    }

    public check createcheckbox() {
        return new maccheckbox();
    }
}
