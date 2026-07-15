public class Main {
    public static void main(String[] args) {

        NewsAgency agency = new NewsAgency();

        observer u1 = new User("viraj");
        observer u2 = new User("Rahul");

       
        agency.subscribe(u1);
        agency.subscribe(u2);

        
        agency.setNews("New Technology Launch!");

        System.out.println("---- After Unsubscribe ----");

       
        agency.unsubscribe(u2);

        agency.setNews("Breaking News: Market Crash!");
    }
}