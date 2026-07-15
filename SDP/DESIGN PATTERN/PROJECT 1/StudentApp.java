import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentApp extends JFrame {

    JTextField txtName, txtAddress, txtEmail, txtPhone, txtCourse, txtAge;
    JComboBox<String> cmbType;
    JButton btnSave, btnUpdate, btnDelete;
    JTable table;
    DefaultTableModel model;

    public StudentApp() {

        setTitle("Student Information System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel left = new JPanel(null);
        left.setPreferredSize(new Dimension(420, 0));
        left.setBackground(Color.WHITE);
        add(left, BorderLayout.WEST);

        Font lf = new Font("Segoe UI", Font.BOLD, 13);
        Font tf = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel title = new JLabel("Student Form", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(0, 20, 420, 30);
        left.add(title);

        int y = 80, gap = 40;

        txtName = addField(left, "Name", y, lf, tf); y += gap;
        txtAddress = addField(left, "Address", y, lf, tf); y += gap;
        txtEmail = addField(left, "Email", y, lf, tf); y += gap;
        txtPhone = addField(left, "Phone", y, lf, tf); y += gap;
        txtCourse = addField(left, "Course", y, lf, tf); y += gap;
        txtAge = addField(left, "Age", y, lf, tf); y += gap;

        JLabel t = new JLabel("Type:");
        t.setBounds(40, y, 100, 30);
        t.setFont(lf);
        left.add(t);

        cmbType = new JComboBox<>(new String[]{
                "REGULAR", "INTERNATIONAL", "EXTERNAL"
        });
        cmbType.setBounds(160, y, 200, 30);
        left.add(cmbType);

        btnSave = new JButton("Save");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        btnSave.setBounds(40, y + 60, 100, 35);
        btnUpdate.setBounds(160, y + 60, 100, 35);
        btnDelete.setBounds(280, y + 60, 100, 35);

        left.add(btnSave);
        left.add(btnUpdate);
        left.add(btnDelete);

        model = new DefaultTableModel(
            new String[]{"ID","Name","Address","Email","Phone","Course","Age","Type"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r == -1) return;

                txtName.setText(model.getValueAt(r, 1).toString());
                txtAddress.setText(model.getValueAt(r, 2).toString());
                txtEmail.setText(model.getValueAt(r, 3).toString());
                txtPhone.setText(model.getValueAt(r, 4).toString());
                txtCourse.setText(model.getValueAt(r, 5).toString());
                txtAge.setText(model.getValueAt(r, 6).toString());
                cmbType.setSelectedItem(model.getValueAt(r, 7).toString());
            }
        });

        loadStudents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void saveStudent() {
        if (!validateForm()) return;

        Student s = StudentFactory.getStudent(
                cmbType.getSelectedItem().toString());

        s.saveStudent(
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtCourse.getText(),
                Integer.parseInt(txtAge.getText())
        );

        loadStudents();
        clear();
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        if (!validateForm()) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        DatabaseUtil.updateStudent(
                id,
                txtName.getText(),
                txtAddress.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtCourse.getText(),
                Integer.parseInt(txtAge.getText()),
                cmbType.getSelectedItem().toString()
        );

        loadStudents();
        clear();
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        DatabaseUtil.deleteStudent(id);
        loadStudents();
        clear();
    }

    private void loadStudents() {
        model.setRowCount(0);
        try (Connection con = DatabaseUtil.getConnection();
             ResultSet rs = DatabaseUtil.getAllStudents(con)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("course"),
                        rs.getInt("age"),
                        rs.getString("student_type")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        if (txtName.getText().isEmpty()) return error("Name required");
        if (!txtEmail.getText().contains("@")) return error("Invalid Email");
        if (!txtPhone.getText().matches("\\d{10,}")) return error("Invalid Phone");
        try { Integer.parseInt(txtAge.getText()); }
        catch (Exception e) { return error("Invalid Age"); }
        return true;
    }

    private boolean error(String m) {
        JOptionPane.showMessageDialog(this, m);
        return false;
    }

    private JTextField addField(JPanel p, String n, int y, Font lf, Font tf) {
        JLabel l = new JLabel(n + ":");
        l.setBounds(40, y, 100, 30);
        l.setFont(lf);
        p.add(l);

        JTextField t = new JTextField();
        t.setBounds(160, y, 200, 30);
        t.setFont(tf);
        p.add(t);
        return t;
    }

    private void clear() {
        txtName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtCourse.setText("");
        txtAge.setText("");
    }

    public static void main(String[] args) {
        new StudentApp();
    }
}
