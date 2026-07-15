public class manager extends loanHandler {
    private static manager instance;
    private manager(){}
    public static manager getInstance(){
        if(instance == null){
            instance=new manager();
        }
        return instance;

    }
    public void handleRequest(int amount){
        if(amount <=100000){
            System.out.println("Manager Approved : "+amount);
        }
        else{
            System.out.println("Manager passed to Supervisor");
            next.handleRequest(amount); 
        }
    }

}
