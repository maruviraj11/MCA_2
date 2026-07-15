public class sugerdecorator extends coffeedecorator{

    public sugerdecorator(coffee cf)
    {
        super(cf);
    }
    public String getDescription()
    {
        return cf.getDescription()+"+SUGER";
    }
    public int getCost()
    {
        return cf.getCost() + 10;
    }
    
}
