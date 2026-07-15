public class StudentFactory {

    public static Student getStudent(String type) {

        if (type.equalsIgnoreCase("REGULAR"))
            return new RegularStudent();

        else if (type.equalsIgnoreCase("INTERNATIONAL"))
            return new InternationalStudent();

        else if (type.equalsIgnoreCase("EXTERNAL"))
            return new ExternalStudent();

        return null;
    }
}
