public class computerfacade {
    private cpu Cpu;
    private memory Memory;
    private harddisk Harddisk;
    public String rs;

    public computerfacade()
    {
        Cpu = new cpu();
        Memory = new memory();
        Harddisk = new harddisk();
    }
    public String startcomputer()
    {
       return "\n "+Cpu.start() + "\n" + Memory.load() + "\n" + Harddisk.read();
       
    }

    
}
