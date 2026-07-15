import java.lang.Math;

public class circle implements shape{

    private double radius;

    public circle(double radius)
    {
        this.radius=radius;
    }
    public String draw()
    {
        return "THIS IS A DRAW IN CIRCLE";
    }
    public String info()
    {
        return "THIS IS A CIRCLE INFORMATION ";
    }
    public String fillcolor()
    {
        return "THIS IS A CIRCLE COLOR  IS YELLOW";
    }
    public double area()
    {
        return Math.PI*radius*radius;
    }

    
}
