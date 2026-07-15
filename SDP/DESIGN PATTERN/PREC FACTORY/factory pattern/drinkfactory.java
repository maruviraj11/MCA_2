public class drinkfactory {

    public drink setdrink(String drinktype){

        if(drinktype==null)
        {
            return null;
        }
        if(drinktype.equalsIgnoreCase("greentea"))
        {
            return new greentea();

        }
        else if(drinktype.equalsIgnoreCase("milk"))
        {
            return new milk();
        }
        else if(drinktype.equalsIgnoreCase("tea"))
        {
            return new tea();
        }
        return null;

       

    }
    
}
