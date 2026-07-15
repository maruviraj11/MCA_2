public abstract class coffeedecorator  implements coffee{

    protected coffee coffee;

    public coffeedecorator(coffee coffee)
    {
        this.coffee = coffee;
    }
    
}