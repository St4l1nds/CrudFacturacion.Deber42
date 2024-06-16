package deber42;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class Deber42 {

      private JComboBox<String> comboBoxProductos;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
    public void run() {
Facturaframe frame = new Facturaframe();
frame.setVisible(true);
frame.setDefaultCloseOperation(Facturaframe.EXIT_ON_CLOSE);
frame.setLocationRelativeTo(null);
 }
});
// Instancia de la conexión a la base de datos
        Conexion postgresConnection = new Conexion();
        // Obtener la conexión
        Connection con = postgresConnection.getConnection();

       
        
        
        
    }
    //Verifica si el cliente existe y pone automaticamete el nombre si este existe
public String verificarCliente(Connection con, String idCliente) {
     String nombreCompleto = null;

        // Consulta SQL para verificar la existencia del cliente y obtener su nombre y apellido
        String query = "SELECT CLINOMBRE1, CLIAPELLIDO1 FROM CLIENTES WHERE CLIIDENTIFICACION = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, idCliente); // Establecer el valor del parámetro

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si el cliente existe, obtener el nombre y apellido
                    String nombre = rs.getString("CLINOMBRE1");
                    String apellido = rs.getString("CLIAPELLIDO1");
                    nombreCompleto = nombre + " " + apellido;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
         
        }

        return nombreCompleto;
}
    
    //Carga la descripcion de los productos en el combo box
   public void cargarProductosEnComboBox(JComboBox<String> comboBoxProductos) {
        Conexion postgresConnection = new Conexion();
        Connection con = postgresConnection.getConnection();

        String query = "SELECT PRODESCRIPCION FROM PRODUCTOS";
        
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            comboBoxProductos.removeAllItems(); // Limpiar el comboBox antes de cargarlo

            while (rs.next()) {
                String descripcionProducto = rs.getString("PRODESCRIPCION");
                comboBoxProductos.addItem(descripcionProducto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
   }
   //Busca el producto con el boton y rellena los datos automaticamente
    public boolean buscarProducto(Connection con, String idProducto, JComboBox<String> comboBoxProductos, JTextField unidadMedidaField, JTextField valorUnidadField) {
        boolean encontrado = false;

        String query = "SELECT PRODESCRIPCION, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PROCODIGO = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    encontrado = true;
                    String descripcionProducto = rs.getString("PRODESCRIPCION");
                    String unidadMedida = rs.getString("PROUNIDADMEDIDA");
                    String valorUnidad = rs.getString("PROPRECIOUM");

                    // Actualizar comboBox y campos de texto
                    comboBoxProductos.setSelectedItem(descripcionProducto);
                    unidadMedidaField.setText(unidadMedida);
                    valorUnidadField.setText(valorUnidad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return encontrado;
    }
    
// Trabaja con combobox desproductos para rellenar los otros campos
        public void obtenerInformacionProducto(Connection con, String descripcionProducto, JTextField fidproducto, JTextField funidadmedida, JTextField fvalorunidad) {
        String query = "SELECT PROCODIGO, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PRODESCRIPCION = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, descripcionProducto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String idProducto = rs.getString("PROCODIGO");
                    String unidadMedida = rs.getString("PROUNIDADMEDIDA");
                    String valorUnidad = rs.getString("PROPRECIOUM");

                    SwingUtilities.invokeLater(() -> {
                        fidproducto.setText(idProducto);
                        funidadmedida.setText(unidadMedida);
                        fvalorunidad.setText(valorUnidad);
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
       public String generarSiguienteCodigoFactura() {
        String siguienteCodigo = "FAC-001"; // Valor por defecto si no hay registros en la base de datos
        Conexion postgresConnection = new Conexion();
        Connection con = postgresConnection.getConnection();

        String query = "SELECT MAX(FACNUMERO) AS ULTIMOCODIGO FROM FACTURAS";
        
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String ultimoCodigo = rs.getString("ULTIMOCODIGO");
                if (ultimoCodigo != null && !ultimoCodigo.isEmpty()) {
                    // Extraer el número del formato "FAC-XXX"
                    int numeroActual = Integer.parseInt(ultimoCodigo.replace("FAC-", ""));
                    int siguienteNumero = numeroActual + 1;
                    // Formatear el nuevo código con ceros a la izquierda
                    siguienteCodigo = String.format("FAC-%03d", siguienteNumero);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return siguienteCodigo;
    } 
        
}
