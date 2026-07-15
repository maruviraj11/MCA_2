public class BookProxy implements Book {

    private String fileName;
    private RealBook realBook;

    public BookProxy(String fileName){
        this.fileName = fileName;
    }

    public String display(){

        if(realBook == null){
            realBook = new RealBook(fileName);
        }

        return realBook.display();
    }
}