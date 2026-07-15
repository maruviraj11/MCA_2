public class main
{
    public static void main(String args[])
    {
        connectionmanager s1 = connectionmanager.getconnection();
        connectionmanager s2 = connectionmanager.getconnection();
        System.out.println(s1.hashCode()); 
        System.out.println(s2.hashCode());
        if(s1 == s2)
        {
            System.out.println("true");
        }
        else
        {
            System.out.println("false");
        }   
            
    }
}