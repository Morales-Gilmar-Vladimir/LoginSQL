import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginUsuario {
    private JLabel Nombre;
    private JLabel Contra;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JButton inicioBoton;
    private JButton registrarUsuarioButton;
    private JPanel rootPanel;

    private List<String> usuarios;
    private List<String> claves;


    private static final String DB_URL = "jdbc:mysql://localhost/Registro";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";
    private static final String QUERY = "SELECT * FROM Usuarios";

    public LoginUsuario() {
        usuarios = new ArrayList<>();
        claves = new ArrayList<>();

        inicioBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cargarUsuariosDesdeBaseDeDatos();

                String nombreUsuario = textField1.getText();
                String claveUsuario = new String(passwordField1.getPassword());

                int indiceUsuario = usuarios.indexOf(nombreUsuario);
                if (indiceUsuario != -1 && claves.get(indiceUsuario).equals(claveUsuario)) {
                    mostrarFormularioUsuario(nombreUsuario);
                } else {
                    JOptionPane.showMessageDialog(null, "Nombre de usuario o contraseña incorrectos.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }

                textField1.setText("");
                passwordField1.setText("");
            }
        });

        registrarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Registrar Usuario");
                frame.setContentPane(new RegistrarUsuario().rootPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private void cargarUsuariosDesdeBaseDeDatos() {
        usuarios.clear();
        claves.clear();

        try (ResultSet rs = getUsersFromDatabase()) {
            while (rs.next()) {
                usuarios.add(rs.getString("Usuario"));
                claves.add(rs.getString("Clave"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarFormularioUsuario(String nombreUsuario) {
        JFrame frame = new JFrame("Formulario de Usuario");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLayout(new FlowLayout());

        JLabel lblUsuario = new JLabel("Usuario: " + nombreUsuario);
        frame.add(lblUsuario);

        frame.setVisible(true);
    }

    private ResultSet getUsersFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login de Usuario");
        frame.setContentPane(new LoginUsuario().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}