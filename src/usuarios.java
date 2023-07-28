import java.sql.*;

public class usuarios {
    static final String DB_URL="jdbc:mysql://localhost/Registro";
    static final String USER="root";
    static final String PASS="root_bas3";
    static final String QUERY="SELECT * FROM Usuarios";

    public static void main(String[] args) {
        try(
                Connection conn= DriverManager.getConnection(DB_URL,USER,PASS);
                Statement stmt= conn.createStatement();
                ResultSet rs= stmt.executeQuery(QUERY);
        ){
            while(rs.next()){
                System.out.println("ID: "+rs.getInt("ID"));
                System.out.println("Usuario: "+rs.getString("Usuario"));
                System.out.println("Contraseña: "+rs.getString("Clave"));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
