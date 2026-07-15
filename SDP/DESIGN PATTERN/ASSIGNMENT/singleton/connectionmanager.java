public class connectionmanager 
{
    private static connectionmanager conn = null;
    
    private connectionmanager()
    {
        System.out.println("connection create..");
    }
    public static synchronized connectionmanager getconnection() 
    {
        if(conn == null)
        {
            conn = new connectionmanager();
        }
        return conn;
    }

}