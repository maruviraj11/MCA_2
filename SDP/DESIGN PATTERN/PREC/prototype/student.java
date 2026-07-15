public class student  implements prototype{

     private int id;
     private String name;
     private String corse;
     private String department;

     public student(int id,String name,String corse,String department)
     {
          this.id = id;
          this.name = name;
          this.corse = corse;
          this.department = department;
     }
     public prototype clone()
     {
          return new student(this.id,this.name,this.corse,this.department);
     }
     public void display()
     {
          System.out.println("\nid="+id+"\nname="+name+"\ncorce="+corse+"\ndepartment="+department);
     }
     public void setid(int id)
     {
          this.id=id;
     }
     public void setname(String name)
     {
          this.name=name;
     }

     
}