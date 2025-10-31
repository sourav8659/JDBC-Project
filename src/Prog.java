import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
class Prog
{
    public static void main(String[] args)
    {
        Connection conn=null;
        Scanner sc=new Scanner(System.in);
        try {
            conn=ConnectionUtil.getConnection();
            Prog prog=new Prog();
            prog.createTable(conn);
            String name;
            long id,mobile;
            while(true) {
                System.out.println("1. Insert Data:\t 2. Delete Data\t 3. Modify Data\t 4. Retrieve Data\t 5. Exit");
                System.out.print("Enter your Choice: ");
                int ch=Integer.parseInt(sc.nextLine());
                switch(ch) {
                    case 1:
                        // Insert
                        System.out.print("Enter student id: ");
                        id=Long.parseLong(sc.nextLine());
                        System.out.print("Enter student name: ");
                        name=sc.nextLine();
                        System.out.print("Enter student phone no.: ");
                        mobile=Long.parseLong(sc.nextLine());
                        StudentDetails st=new StudentDetails(id,name,mobile);
                        prog.insertData(conn,st);
                        break;
                    case 2:
                        // Delete
                        System.out.print("Enter student id for which data be deleted: ");
                        id=Long.parseLong(sc.nextLine());
                        prog.deleteARow(conn,id);
                        break;
                    case 3:
                        // Update
                        System.out.print("Enter student id for which data be updated: ");
                        id=Long.parseLong(sc.nextLine());
                        PreparedStatement selStmt = conn.prepareStatement("SELECT * FROM student_details WHERE st_id=?");
                        selStmt.setLong(1,id);
                        ResultSet rs=selStmt.executeQuery();
                        if(rs.next()) {
                            String currName=rs.getString("st_name");
                            long currMobile=rs.getLong("st_mobile");
                            System.out.print("Want to change name (or press Enter to keep current)? (current name: "+currName+") - ");
                            name=sc.nextLine();
                            name=name.trim().isEmpty() ? rs.getString("st_name") : name;
                            System.out.print("Want to change name (or press Enter to keep current)? (current name: "+currMobile+") - ");
                            mobile=Long.parseLong(sc.nextLine());
                            mobile=String.valueOf(mobile).trim().isEmpty() ? rs.getLong("st_mobile") : mobile;
                            prog.modifyData(conn,name,mobile,id);
                        } else
                            System.out.println("No DATA Found!!!");
                        break;
                    case 4:
                        // Display
                        prog.retrieveData(conn);
                        break;
                    case 5:
                        System.exit(0);
                    default:
                        System.out.println("Wrong Choice!!");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch(SQLException e) {
                e.printStackTrace();
            } finally {
                sc.close();
            }
        }
    }
    private void createTable(Connection conn) 
    {
        String query="CREATE TABLE IF NOT EXISTS student_details(st_id INT PRIMARY KEY,st_name VARCHAR(50),st_mobile NUMERIC(15))";
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void dropTable(Connection conn) 
    {
        String query="DROP TABLE IF EXISTS student_details";
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void modifyData(Connection conn,String st_name,long st_mobile,long st_id)
    {
        String query="UPDATE student_details SET st_name=?, st_mobile=? WHERE st_id=?";
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.setString(1,st_name);
            pstmt.setLong(2,st_mobile);
            pstmt.setLong(3,st_id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteARow(Connection conn,long st_id)
    {
        String query="DELETE FROM student_details WHERE st_id=?";
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.setLong(1,st_id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void insertData(Connection conn,StudentDetails details)
    {
        String query="INSERT INTO student_details VALUE(?,?,?)";
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.setLong(1,details.getSt_id());
            pstmt.setString(2,details.getSt_name());
            pstmt.setLong(3,details.getSt_mobile());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void retrieveData(Connection conn)
    {
        try {
            String query="SELECT * FROM student_details";
            PreparedStatement pstmt=conn.prepareStatement(query);
            ResultSet rs=pstmt.executeQuery();
            if(rs.isBeforeFirst()) {
                System.out.println("st_id\tst_mobile\tst_name");
                while(rs.next())
                {
                    System.out.println(rs.getString("st_id")+"\t"+rs.getString("st_mobile")+"\t"+rs.getString("st_name"));
                }
            } else
                System.out.println("No Data");
        } 
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
