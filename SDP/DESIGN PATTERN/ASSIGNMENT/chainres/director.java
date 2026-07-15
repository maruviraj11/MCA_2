class director extends loanHandler{
    private static director instance;
    private director(){}
    public static director getInstance(){
        if (instance==null){
            instance=new director();
            
        }
        return instance;
    }
    public void handleRequest(int amount){
        if (amount <=1000000){
            System.out.println("Supervisor Approved "+amount);
        }
        else{
            System.out.println("Loan Rejected");
        }
    }
}
