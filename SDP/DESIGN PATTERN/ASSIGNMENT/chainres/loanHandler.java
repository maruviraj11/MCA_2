abstract class loanHandler{
    protected loanHandler next;
    public void setNext(loanHandler next){
        this.next=next;
    }
    public abstract void handleRequest(int amount);
    
}