public class sugercoffee extends coffeedecorator {

    public sugercoffee(coffee coffee)
    {
        super(coffee);
    }
    public String getdes()
    {
        return coffee.getdes() + "+Suger";
    }
    public int getcost()
    {
        return coffee.getcost() + 30;
    }
}