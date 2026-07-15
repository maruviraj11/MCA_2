public class shapeFactory {

    public shape getShape(String shapeType)
    {
        if(shapeType==null)
        {
            return null;
            
        }
        if(shapeType.equalsIgnoreCase("circle"))
        {
            return new circle();
        }
        else if(shapeType.equalsIgnoreCase("rectangle"))
        {
            return new rectangle();
        }
        return null;
        
    }
    
}
