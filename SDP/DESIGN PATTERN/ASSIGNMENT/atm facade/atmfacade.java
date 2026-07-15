public class atmfacade
{
    private amount am;
    private transaction tr;

    public atmfacade()
    {
        am = new amount();
        tr = new transaction();
    }
    public String atmtransaction()
    {
        return "\n"+am.getamount() + "\n" + tr.widrow() + "\n" + tr.deposite();
    }
}