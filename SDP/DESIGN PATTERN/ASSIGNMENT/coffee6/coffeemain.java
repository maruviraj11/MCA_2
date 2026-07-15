public class coffeemain {
    
    public static void main(String[] args)
    {
        coffee cf = new simplecoffee();
        System.out.println(cf.getDescription() +  "=" + cf.getCost());

        cf=new milkdecorator(cf);
        System.out.println(cf.getDescription() + "=" + cf.getCost());

        cf= new sugerdecorator(cf);
        System.out.println(cf.getDescription() + "=" + cf.getCost());
    }
}
