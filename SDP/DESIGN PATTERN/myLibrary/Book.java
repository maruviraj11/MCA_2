package myLibrary;

public class Book 
{
    // region Private Variables
       private int Book_Id;
       private String Auther_Name;
       private String Title;
       private boolean IsIssued;
       private int Member_Id;
    // endregion

    // region Cunstructor
      public Book()
      {
        
      }
    // endregion

     //region Get And Set Functionality
        public void SetData(int Book_Id,String Auther_Name,String Title,boolean IsIssued)
        {
            this.Book_Id = Book_Id;
            this.Auther_Name = Auther_Name;
            this.Title = Title;
            this.IsIssued = IsIssued;
        }

        public String Get_Auther_Name()
        {
                return this.Auther_Name;
        }

        public String Get_Title()
        {
                return this.Title;
        }

        public void Issue_Book(int Member_Id)
        {
                if(!this.IsIssued)
                {
                    this.Member_Id = Member_Id;
                    this.IsIssued = true;
                }
        }

        public void Return_Book()
        {
                this.IsIssued = false;
        }

        public boolean Get_IsAvalible()
        {
                return this.IsIssued;
        }

        public int Get_Member_Id()
        {
                if(this.IsIssued)
                    return this.Member_Id;
                else
                    throw new IllegalArgumentException("This Book Is Not Issued By Any Member");
        }
         // endregion
}
