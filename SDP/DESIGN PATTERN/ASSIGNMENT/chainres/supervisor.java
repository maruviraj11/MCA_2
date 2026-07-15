class supervisor extends loanHandler {
    private static supervisor instance;
    private supervisor(){}
    public static supervisor getInstance(){
        if(instance==null){
            instance=new supervisor();
        }
        return instance;
    }
    public void handleRequest(int amount){
        if (amount<=500000){
            System.out.println("Supervisor Approved : "+amount);
        }
        else{
            System.out.println("Supervisor Passed to Director");
            next.handleRequest(amount);
        }
    }
}
