public class ExternalStudent implements Student {

    @Override
    public void saveStudent(String name, String address, String email,
                            String phone, String course, int age) {

        DatabaseUtil.insertStudent(
            name, address, email, phone, course, age, "External"
        );
    }
}

