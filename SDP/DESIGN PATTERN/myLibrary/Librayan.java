package myLibrary;
import java.util.*;

public class Librayan extends Person
{
    // region  Private Variables
    private long Salary;
    private int Librayan_Id;
    private List<Member> Members = new ArrayList<>();
    private List<Book> All_Books = new ArrayList<>();
    // endregion

    // region Cunstructor
    public Librayan()
    {

    }
    // endregion

     //region Get And Set Functionality
    public void SetData(int Librayan_Id,long Salary,String Name,String Email,long Contact,String Address)
    {
        this.Librayan_Id = Librayan_Id;
        this.Salary = Salary;
        super.SetData(Name,Email,Contact,Address);
    } 

    public void SetBooksData(List<Book> books)
    {
        for(Book b: books)
        {
            All_Books.add(b);
        }
    }

    public long GetSalary()
    {
        return Salary;
    }

    public List<Book> Show_All_Books()
    {
        if (this.All_Books.size() > 0) 
        {
            return this.All_Books;
        }
        else
        {
            throw new IllegalArgumentException("No Any Book here");
        }
    }

    public List<Member> Show_All_Member()
    {
        if (this.Members.size() > 0) 
        {
            return this.Members;
        }
        else
        {
            throw new IllegalArgumentException("No Any Member here");
        }
    }

     // endregion

    public Member Show_Member_of_IssuedBook(Book b)
    {
        int id = b.Get_Member_Id();
        for(Member m: Members)
        {
            if(m.Get_Member_Id() == id)
            {
                    return m;
            }
        }
        return null;
    }
}
