//import net.proteanit.sql.DbUtils;

import net.proteanit.sql.DbUtils; // table insertion
import javax.swing.*; // SWING GUI
import java.awt.event.ActionEvent; //Execution of code by pressing a button
import java.awt.event.ActionListener;  // button pressing listener
import java.sql.*; //database

public class JavaMiniProject {
    private JLabel heading;
    private JButton btnsave;
    private JButton btnupdate;
    private JButton btndelete;
    private JButton btnsearch;
    private JLabel name;
    private JLabel salary;
    private JLabel phoneno;
    private JTextField tfname;
    private JTextField tfsalary;
    private JTextField tfphoneno;
    private JTextField tf;
    private JScrollPane jtable;
    private JPanel Main;
    private JTable table1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("JavaMiniProject");
        frame.setContentPane(new JavaMiniProject().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public void connect()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/Employee", "root", "");
            System.out.println("Database linked successfully!");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    void table_load()
    {
        try{
            pst = con.prepareStatement("select * from employee_details");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public JavaMiniProject() {
        connect();
        table_load();
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname, salary, mobile;
                empname = tfname.getText();
                salary = tfsalary.getText();
                mobile = tfphoneno.getText();

                try{
                    pst = con.prepareStatement("insert into employee_details(name, salary, contact)values(?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added successfully!");
                    table_load();
                    tfname.setText("");
                    tfphoneno.setText("");
                    tfsalary.setText("");
                    tfname.requestFocus();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        btnsearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String empid = tf.getText();

                    pst = con.prepareStatement("select name, salary, contact from employee_details where id = ?");
                    pst.setString(1,empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String empname= rs.getString(1);
                        String salary= rs.getString(2);
                        String mobile= rs.getString(3);

                        tfname.setText(empname);
                        tfsalary.setText(salary);
                        tfphoneno.setText(mobile);

                    }
                    else
                    {
                        tfname.setText("");
                        tfsalary.setText("");
                        tfphoneno.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid Employee ID...");
                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        btnupdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empname, salary, mobile, empid;
                empname = tfname.getText();
                salary = tfsalary.getText();
                mobile = tfphoneno.getText();
                empid = tf.getText();

                try{
                    pst = con.prepareStatement("update employee_details set name = ?,salary = ?, contact = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!");
                    table_load();
                    tfname.setText("");
                    tfphoneno.setText("");
                    tfsalary.setText("");
                    tfname.requestFocus();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        btndelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bid;
                bid = tf.getText();

                try{
                    pst = con.prepareStatement("delete from employee_details where id = ?");
                    pst.setString(1, bid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!");
                    table_load();
                    tfname.setText("");
                    tfsalary.setText("");
                    tfphoneno.setText("");
                    tf.setText("");
                    tfname.requestFocus();
                }
                catch (SQLException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }
}