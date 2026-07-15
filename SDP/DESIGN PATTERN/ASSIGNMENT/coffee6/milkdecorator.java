public class milkdecorator extends coffeedecorator {

    public milkdecorator(coffee cf)
    {
        super(cf);
    }

    public String getDescription()
    {
        return cf.getDescription()+"+MILK";
    }
    public int getCost()
    {
        return cf.getCost()+ 20;
    }
    
}
