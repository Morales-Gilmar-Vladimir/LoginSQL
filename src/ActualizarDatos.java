import javax.swing.*;
import java.sql.*;

public class ActualizarDatos {
    private JTextField usuarioFile;
    private JButton actualizarDatosButton;
    private JTextField newcontraFile;
    JPanel rootPanel;
    private JTextField newUsuarioFile;

    private static final String DB_URL = "jdbc:mysql://localhost/Registro";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";

    public ActualizarDatos() {
        actualizarDatosButton.addActionListener(e -> {
            String usuario = usuarioFile.getText();
            String contra = newcontraFile.getText();

            if (verificarDatos(usuario, contra)) {
                // Si los datos son válidos, actualizar la base de datos
                VentanaDeActualizacion ventanaActualizacion = new VentanaDeActualizacion(usuario, contra);
                ventanaActualizacion.actualizarDatosEnBaseDeDatos(newUsuarioFile.getText(), newcontraFile.getText());
            } else {
                // Si los datos no son válidos, mostrar un mensaje de error
                JOptionPane.showMessageDialog(null, "Datos inválidos. Verifica los campos.");
            }
        });
    }

    private boolean verificarDatos(String usuario, String contra) {
        // Aquí puedes realizar las verificaciones necesarias
        // por ejemplo, verificar si el usuario y la contraseña cumplen con ciertas condiciones

        // Por ejemplo, en este caso, solo verificamos que los campos no estén vacíos
        return !usuario.isEmpty() && !contra.isEmpty();
    }

    // Clase de ejemplo para la ventana de actualización
    public static class VentanaDeActualizacion {
        private String usuario;
        private String contra;

        public VentanaDeActualizacion(String usuario, String contra) {
            this.usuario = usuario;
            this.contra = contra;
        }

        public void actualizarDatosEnBaseDeDatos(String nuevoUsuario, String nuevaContra) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement("UPDATE Usuarios SET Usuario = ?, Clave = ? WHERE Usuario = ?")) {

                stmt.setString(1, nuevoUsuario);
                stmt.setString(2, nuevaContra);
                stmt.setString(3, usuario);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Datos actualizados exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar los datos. Usuario no encontrado.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}