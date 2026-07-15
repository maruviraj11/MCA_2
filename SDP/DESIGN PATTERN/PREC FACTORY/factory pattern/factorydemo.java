public class factorydemo {

    public static void main(String[] args)
    {
        drinkfactory factory =new drinkfactory();

        drink d1=factory.setdrink("tea");
        d1.getdrink();

        drink d2=factory.setdrink("greentea");
        d2.getdrink();

        drink d3=factory.setdrink("milk");
        d3.getdrink();

    }
  
}
