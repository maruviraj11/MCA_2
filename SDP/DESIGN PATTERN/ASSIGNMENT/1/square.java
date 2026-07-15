import java.lang.Math;
public class square implements shape{

    private double side;

    public square(double side)
    {
        this.side=side;
    }
    public String draw()
    {
        return "THIS IS A SQUARE METHOD ";
    }
    public String info()
    {
        return "THIS IS A INFORMATION IN SQUARE";
    }
    public String fillcolor()
    {
        return "THIS IS A COLOUR OF SQUARE IS RED";
    }
    public double area()
    {
        return Math.round(side*side);
    }


    
}


