class PrinterAdapter implements Printer {


    private OldPrinter oldPrinter;

   
    public PrinterAdapter(OldPrinter oldPrinter) {
        this.oldPrinter = oldPrinter;
    }

    
    public void print() {
        oldPrinter.printOld();
    }
}