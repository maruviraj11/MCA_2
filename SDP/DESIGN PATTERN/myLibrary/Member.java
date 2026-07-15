package myLibrary;
import java.util.*;
import myLibrary.Book;
import myLibrary.Person;

public class Member extends Person
{
    // region Private Variables
    private int Issue_Limit;
    private int Member_Id;
    private List<Book> All_Books = new ArrayList<>();
    private List<Book> My_Issued_Books = new ArrayList<>();
    // endregion
    
    // region Cunstructor
    public Member()
    {

    }
    // endregion

    // region Get And Set Data Methods
    public void SetData(int Member_Id,int Issue_Limit,String Name,String Email,long Contact,String Address)
    {
        this.Member_Id = Member_Id;
        this.Issue_Limit = Issue_Limit;
        super.SetData(Name,Email,Contact,Address);
    } 

    public void SetBooksData(List<Book> books)
    {
        for(Book b: books)
        {
            All_Books.add(b);
            if (!b.Get_IsAvalible()) 
            {
                if (b.Get_Member_Id() == this.Member_Id) 
                {
                    My_Issued_Books.add(b);
                }
            } 
        }
    }

    public int Get_Issue_Limit()
    {
        return this.Issue_Limit;
    }

    public int Get_Member_Id()
    {
        return this.Member_Id;
    }
    // endregion

    // region Publicly Logical Functionality
    public boolean Issue_Book(Book book)
    {
        if (My_Issued_Books.size() < this.Issue_Limit) 
        {
            book.Issue_Book(this.Member_Id);
            return true;
        }
        else
        {
            return false;
        }
    }

    public List<Book> Get_All_Issued_Book()
    {
        if (this.My_Issued_Books.size() > 0) 
        {
            return this.My_Issued_Books;
        }
        else
        {
            throw new IllegalArgumentException("No Issued Any Book");
        }
    }

    public List<Book> Get_All_Books()
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
    // endregion
}
