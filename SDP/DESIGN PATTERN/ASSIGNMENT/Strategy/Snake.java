public abstract class Snake {
    
    protected String name;
    protected BiteStrategy biteStrategy;

    public Snake(String name, BiteStrategy biteStrategy) {
        this.name = name;
        this.biteStrategy = biteStrategy;
    }

    public void PerfomBite(){
        biteStrategy.bite();
    }

    public void setBiteStrategy(BiteStrategy biteStrategy){
        this.biteStrategy = biteStrategy;
    }

    public String display(){
        return "Snake type : " + name.toUpperCase();
    }

}
