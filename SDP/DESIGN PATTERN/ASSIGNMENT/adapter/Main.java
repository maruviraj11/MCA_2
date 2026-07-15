public class Main {
    public static void main(String[] args) {

        OldPrinter oldPrinter = new OldPrinter();

        Printer printer = new PrinterAdapter(oldPrinter);

        printer.print();
    }
}