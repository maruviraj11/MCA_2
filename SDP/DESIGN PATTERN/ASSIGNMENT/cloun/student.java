class student implements prototype {

    private int id;
    private String name;
    private String course;
    private String department;

    public student(int id, String name, String course, String department) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.department = department;
    }

    public prototype clone() {
        return new student(this.id, this.name, this.course, this.department);
    }

    public void display() {
        System.out.println("id: " + id +
                " name: " + name +
                " course: " + course +
                " department: " + department);
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