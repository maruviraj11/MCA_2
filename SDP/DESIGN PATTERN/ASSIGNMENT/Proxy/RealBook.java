public class RealBook implements Book {

    private String fileName;

    public RealBook(String fileName){
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk(){
        System.out.println("Loading book from cloud: " + fileName);
    }

    public String display(){

        if(fileName.equalsIgnoreCase("Java")){
            return "Description\n Java is a high-level object-oriented programming language.\n and platform independent language.\n";
        }

        else if(fileName.equalsIgnoreCase("Python")){
            return " Diescription \nPython is a simple and powerful interpreted language.\nn It is widely used for web development, data analysis, artificial intelligence, and scientific computing.\n";
        }

        else if(fileName.equalsIgnoreCase("C++")){
            return "description\n C++ is a high performance system programming language.\n It is widely used for developing operating systems, games, and other performance-critical applications.\n";
        }

        else{
            return "Book not found.";
        }
    }
}