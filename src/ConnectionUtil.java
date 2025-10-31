import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
class ConnectionUtil 
{
    public static Connection getConnection() throws SQLException
    {
        Connection connection=null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");          
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3406/virju","root","");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println("message for connection open "+connection);
        return connection;
    }
}
