public class demomain {

    public static void main(String[] args)
    {
        coffee cf = new simplecoffee();

        cf = new milkcoffee(cf);

        cf = new sugercoffee(cf);

        System.out.println(cf.getdes());
        System.out.println(cf.getcost());

    }
}