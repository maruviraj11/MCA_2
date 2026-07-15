public class milkcoffee extends coffeedecorator {

    
    public milkcoffee(coffee coffee)
    {
        super(coffee);
    }
    public String getdes()
    {
        return coffee.getdes() + "+milk";
    }
    public int getcost()
    {
        return coffee.getcost() + 20;
    }
}