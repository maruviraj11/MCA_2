import java.lang.Math;
public class rectangle implements shape
{
    double lenth;
    double width;

    public rectangle(double lenth,double width)
    {
        this.lenth=lenth;
        this.width=width;
    }
    public String draw()
    {
        return "THIS IS A DRAW METHOD IN RECTANGLE";
    }
    public String info()
    {
        return "THIS IS A INFORMATION RECTANGLE";
    }
    public String fillcolor()
    {
        return "THIS IS A RECTANGLE COLOR OF BLUE";
    }
    public double area()
    {
        return Math.round(lenth*width);
    }
}