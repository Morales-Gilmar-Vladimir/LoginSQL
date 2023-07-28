import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrarUsuario {
    private JLabel Contra;
    private JTextField usuario;
    private JLabel Nombre;
    private JButton guardarButton;
    private JPasswordField clave;
    JPanel rootPanel;
    private JLabel ID;
    private List<String> usuarios;
    private List<String> claves;

    private static final String DB_URL = "jdbc:mysql://localhost/Registro";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";
    private static final String QUERY = "SELECT * FROM Usuarios";

    public RegistrarUsuario() {
        usuarios = new ArrayList<>();
        claves = new ArrayList<>();

        cargarUsuariosDesdeBaseDeDatos();


        if (!usuarios.isEmpty()) {
            int lastInsertedID = usuarios.size()+1;
            ID.setText("ID: " + lastInsertedID);
        }

        guardarButton.addActionListener(e -> {
            String usuarioText = usuario.getText();
            String claveText = new String(clave.getPassword());


            int lastInsertedID = guardarUsuarioEnBaseDeDatos(usuarioText, claveText);

            ID.setText("ID: " + lastInsertedID);

            usuario.setText("");
            clave.setText("");
        });
    }

    private void cargarUsuariosDesdeBaseDeDatos() {
        usuarios.clear();
        claves.clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY)) {

            while (rs.next()) {
                usuarios.add(rs.getString("Usuario"));
                claves.add(rs.getString("Clave"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int guardarUsuarioEnBaseDeDatos(String usuario, String clave) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Usuarios (Usuario, Clave) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario);
            stmt.setString(2, clave);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Get the last inserted ID
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}