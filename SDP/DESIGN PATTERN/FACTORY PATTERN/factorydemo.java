public class factorydemo {

    public static void main(String args[])
    {
        shapeFactory factory=new shapeFactory();

        shape s1=factory.getShape("circle");
        s1.drow();

        shape s2=factory.getShape("rectangle");
        s2.drow();

    }
    
}
