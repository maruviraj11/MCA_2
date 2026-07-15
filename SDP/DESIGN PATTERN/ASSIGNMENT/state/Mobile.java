public class Mobile {
    private MobileState state;
    public void setState(MobileState state){
        this.state=state;
      
    }
    public String incomingCall(){
        return state.handleCall();
    }
    
    
}
