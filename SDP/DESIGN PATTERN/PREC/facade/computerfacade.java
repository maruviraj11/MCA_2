public class computerfacade {

    private cpu cpu;
    private memmory memmory;
    private harddisk harddisk;

    public computerfacade()
    {
        cpu = new cpu();
        memmory = new memmory();
        harddisk = new harddisk();
    }
    public String startcomputer()
    {
        return "\n"+ cpu.start() + "\n" + memmory.load() + "\n" + harddisk.read();
    }
}