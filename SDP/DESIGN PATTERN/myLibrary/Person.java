package myLibrary;

public class Person 
{
    // region  Private Variables
    private String Name;
    private String Email;
    private long Contact;
    private String Address;
    // endregion  

    // region Cunstructor
    void Person()
    {

    }
    // endregion

    //region Get And Set Functionality
    public void SetData(String Name,String Email,long Contact,String Address)
    {
        this.Name = Name;
        this.Email = Email;
        this.Contact = Contact;
        this.Address = Address;
    }

    public String Get_Name()
    {
        return this.Name;
    }
     public String Get_Email()
    {
        return this.Email;
    }
     public long Get_Contact()
    {
        return this.Contact;
    }
     public String Get_Address()
    {
        return this.Address;
    }

    // endregion
}