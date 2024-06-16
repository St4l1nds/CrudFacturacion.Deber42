/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deber42;

/**
 *
 * @author stali
 */
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Date;


import config.Conexion;
import java.sql.Connection;
import deber42.Deber42;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Facturaframe extends javax.swing.JFrame {
   
   // private JComboBox<String> fprodescrcombobox;

        private DefaultTableModel tableModel;
        
    /**
     * Creates new form Facturaframe
     */
    public Facturaframe() {
        initComponents();
        //Pasa los parametros del .java Deber42
        Deber42 deber = new Deber42();
        //Importa el metodo y lo utiliza en el combo box para inicializar el combo box
        deber.cargarProductosEnComboBox(fprodescrcombobox);
        
        
       // Crear el modelo de tabla con los nombres de las columnas
        String[] columnNames = {"IDProducto", "Descripción", "UnidadMedida", "Cantidad", "Valor Unitario", "Subtotal"};
         
        tableModel = new DefaultTableModel(columnNames, 0);
        facturaciontabla.setModel(tableModel);
        
        
          // Obtener y establecer el siguiente código de factura
        String siguienteCodigoFactura = deber.generarSiguienteCodigoFactura();
        fnfactura.setText(siguienteCodigoFactura);
        
        
        
        
        
        
        
        
        
        
        //Llama al metodo para cargar los clientes en la tabla de clietnes del segundo frame
        cargarClientesEnTabla();
        //Carga los productos en la tabla
         cargarProductos();
        //Modelo del spinner para que empieze en 1, el minimo sea 1, no tenga maximo y se incremente 1 cada vez
        SpinnerNumberModel n = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        fcantidad.setModel(n);
        
    }
    //Carga los productos en la tabla productos en el 3panel
    private void cargarProductos() {
    DefaultTableModel model = (DefaultTableModel) prtabla.getModel();
    model.setRowCount(0); // Limpiar la tabla

    String query = "SELECT * FROM PRODUCTOS";

    try (Connection con = new Conexion().getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            Object[] row = {
                rs.getString("PROCODIGO"),
                rs.getString("PRODESCRIPCION"),
                rs.getString("PROUNIDADMEDIDA"),
                rs.getBigDecimal("PROSALDOINICIAL"),
                rs.getBigDecimal("PROINGRESOS"),
                rs.getBigDecimal("PROEGRESOS"),
                rs.getBigDecimal("PROAJUSTES"),
                rs.getBigDecimal("PROSALDOFINAL"),
                rs.getBigDecimal("PROCOSTOUM"),
                rs.getBigDecimal("PROPRECIOUM"),
                rs.getString("PROSTATUS")
            };
            model.addRow(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los productos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    //Cargar la tabla clientes en el 2do panel
    private void cargarClientesEnTabla() {
   String query = "SELECT * FROM CLIENTES";

    try (Connection con = new Conexion().getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        // Obtener los metadatos de la consulta
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Crear el modelo de la tabla basado en los metadatos
        DefaultTableModel model = new DefaultTableModel();
        cltabla.setModel(model);

        // Agregar las columnas al modelo de la tabla
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(metaData.getColumnLabel(i));
        }

        // Agregar las filas al modelo de la tabla
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar clientes", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
  /*  private void generarFacturaPDF(String facNumero, String cliCodigo, String facFecha, double facSubTotal, double facIva, double facTotal) throws DocumentException {
    Document document = new Document();
    try {
        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\stali\\Downloads\\Factura_" + facNumero + ".pdf"));
        document.open();
        document.add(new Paragraph("Factura Numero: " + facNumero));
        document.add(new Paragraph("Codigo Cliente: " + cliCodigo));
        document.add(new Paragraph("Fecha: " + facFecha));
        document.add(new Paragraph("Subtotal: " + facSubTotal));
        document.add(new Paragraph("IVA: " + facIva));
        document.add(new Paragraph("Total: " + facTotal));
        
        PdfPTable table = new PdfPTable(4); // 4 columnas
        table.setWidthPercentage(100);
        PdfPCell cell;
        
        cell = new PdfPCell(new Paragraph("Codigo Producto"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Cantidad"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Valor"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Subtotal"));
        table.addCell(cell);
        
        for (int i = 0; i < facturaciontabla.getRowCount(); i++) {
            String proCodigo = (String) facturaciontabla.getValueAt(i, 0);
            String cantidad = (String) facturaciontabla.getValueAt(i, 3);
            String valor = (String) facturaciontabla.getValueAt(i, 4);
            String subtotal = String.valueOf(Double.parseDouble(cantidad) * Double.parseDouble(valor));
            
            table.addCell(proCodigo);
            table.addCell(cantidad);
            table.addCell(valor);
            table.addCell(subtotal);
        }
        
        document.add(table);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        document.close();
    }
}
*/
private void generarFacturaXML(String facNumero, String cliCodigo, String facFecha, double facSubTotal, double facIva, double facTotal) throws Exception {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    
    Document document = documentBuilder.newDocument();
    Element root = document.createElement("Factura");
    document.appendChild(root);
    
    Element numero = document.createElement("Numero");
    numero.appendChild(document.createTextNode(facNumero));
    root.appendChild(numero);
    
    Element codigoCliente = document.createElement("CodigoCliente");
    codigoCliente.appendChild(document.createTextNode(cliCodigo));
    root.appendChild(codigoCliente);
    
    Element fecha = document.createElement("Fecha");
    fecha.appendChild(document.createTextNode(facFecha));
    root.appendChild(fecha);
    
    Element subtotal = document.createElement("Subtotal");
    subtotal.appendChild(document.createTextNode(String.valueOf(facSubTotal)));
    root.appendChild(subtotal);
    
    Element iva = document.createElement("IVA");
    iva.appendChild(document.createTextNode(String.valueOf(facIva)));
    root.appendChild(iva);
    
    Element total = document.createElement("Total");
    total.appendChild(document.createTextNode(String.valueOf(facTotal)));
    root.appendChild(total);
    
    Element productos = document.createElement("Productos");
    root.appendChild(productos);
    
    for (int i = 0; i < facturaciontabla.getRowCount(); i++) {
        Element producto = document.createElement("Producto");
        
        Element codigo = document.createElement("Codigo");
        codigo.appendChild(document.createTextNode((String) facturaciontabla.getValueAt(i, 0)));
        producto.appendChild(codigo);
        
        Element cantidad = document.createElement("Cantidad");
        cantidad.appendChild(document.createTextNode((String) facturaciontabla.getValueAt(i, 3)));
        producto.appendChild(cantidad);
        
        Element valor = document.createElement("Valor");
        valor.appendChild(document.createTextNode((String) facturaciontabla.getValueAt(i, 4)));
        producto.appendChild(valor);
        
        Element subtotalProducto = document.createElement("Subtotal");
        subtotalProducto.appendChild(document.createTextNode(String.valueOf(Double.parseDouble((String) facturaciontabla.getValueAt(i, 3)) * Double.parseDouble((String) facturaciontabla.getValueAt(i, 4)))));
        producto.appendChild(subtotalProducto);
        
        productos.appendChild(producto);
    }
    
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File("C:\\Users\\stali\\Downloads\\Factura_" + facNumero + ".xml"));
    
    transformer.transform(domSource, streamResult);
}
    
    
    
    //Metodo para el boton generar Factura para obtener los datos de los texfield y demas
private String obtenerCodigoCliente(String cliIdentificacion) {
    String cliCodigo = null;
    String query = "SELECT CLICODIGO FROM CLIENTES WHERE CLIIDENTIFICACION = ?";
    
    try (Connection con = new Conexion().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setString(1, cliIdentificacion);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                cliCodigo = rs.getString("CLICODIGO");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cliCodigo;
}
    
    private void insertarFactura(String facNumero, String cliCodigo, String facFecha, double facSubTotal, double facIva, String formaPago) {
    String query = "INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACDESCUENTO, FACIVA, FACICE, FACFORMAPAGO, FACSTATUS) " +
                   "VALUES (?, ?, ?, ?, 0, ?, 0, ?, 'PAG')";
    
    try (Connection con = new Conexion().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setString(1, facNumero);
        ps.setString(2, cliCodigo);
        ps.setString(3, facFecha);
        ps.setDouble(4, facSubTotal);
        ps.setDouble(5, facIva);
        ps.setString(6, formaPago);
        
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    private void insertarProductosEnFactura(String facNumero) {
    String query = "INSERT INTO PXF (FACNUMERO, PROCODIGO, PXFCANTIDAD, PXFVALOR, PXFSUBTOTAL, PXFSTATUS) " +
                   "VALUES (?, ?, ?, ?, ?, 'ACT')";
    
    try (Connection con = new Conexion().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        // Recorrer las filas de la tabla para obtener los datos de cada producto
        for (int i = 0; i < facturaciontabla.getRowCount(); i++) {
            String proCodigo = (String) facturaciontabla.getValueAt(i, 0);
            int cantidad = Integer.parseInt((String) facturaciontabla.getValueAt(i, 3));
            double valor = Double.parseDouble((String) facturaciontabla.getValueAt(i, 4));
            double subtotal = cantidad * valor;
            
            ps.setString(1, facNumero);
            ps.setString(2, proCodigo);
            ps.setInt(3, cantidad);
            ps.setDouble(4, valor);
            ps.setDouble(5, subtotal);
            
            ps.addBatch();
        }
        
        ps.executeBatch();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    //Este metodo actualiza los label de suma iva y total dependiendo si se agrega o se resta porque se los llama en las funciones de agregar o eliminar
    private void actualizarSumas() {
        double suma = 0.0;

        // Recorrer todas las filas de la tabla y sumar los valores de la columna "Subtotal"
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            suma += Double.parseDouble((String) tableModel.getValueAt(i, 5));
        }

        // Obtener el porcentaje de IVA seleccionado en el comboBox
        String ivaString = (String) ivacombobox.getSelectedItem();
        double ivaPorcentaje = 0.0;
        if (ivaString != null && !ivaString.isEmpty()) {
            ivaString = ivaString.replace("%", "").trim();
            ivaPorcentaje = Double.parseDouble(ivaString) / 100.0;
        }

        // Calcular el IVA y el total
        double iva = suma * ivaPorcentaje;
        double total = suma + iva;

        // Actualizar las etiquetas
        sumalabel.setText(String.format("%.2f", suma));
        ivalabel.setText(String.format("%.2f", iva));
        totallabel.setText(String.format("%.2f", total));
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fidcliente = new javax.swing.JTextField();
        btnfverificarcli = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        fnfactura = new javax.swing.JTextField();
        fverificacioncliente = new javax.swing.JTextField();
        ivacombobox = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        facturaciontabla = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        fidproducto = new javax.swing.JTextField();
        btnfbuscarpro = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        funidadmedida = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        fvalorunidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        fcantidad = new javax.swing.JSpinner();
        fpago = new javax.swing.JComboBox<>();
        btnfagregarproducto = new javax.swing.JButton();
        btnfgenerarfactura = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        fnombrecliente = new javax.swing.JTextField();
        fprodescrcombobox = new javax.swing.JComboBox<>();
        frespbuscarpro = new javax.swing.JTextField();
        btneliminarproducto = new javax.swing.JButton();
        btnfverfacturas = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        sumalabel = new javax.swing.JLabel();
        ivalabel = new javax.swing.JLabel();
        totallabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cltabla = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        clid = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        clprimernombre = new javax.swing.JTextField();
        clsegundonombre = new javax.swing.JTextField();
        clprimerapellido = new javax.swing.JTextField();
        clsegundoapellido = new javax.swing.JTextField();
        cldireccion = new javax.swing.JTextField();
        cltelefono = new javax.swing.JTextField();
        clcelular = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        clemail = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cltipo = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        clestatus = new javax.swing.JComboBox<>();
        btnagregarcliente = new javax.swing.JButton();
        btneliminarcliente = new javax.swing.JButton();
        clbuscarcliente = new javax.swing.JButton();
        btnactualizarcliente = new javax.swing.JButton();
        clbtnlimpiarcampos = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        iddelcliente = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        prtabla = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        prcod = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        prdescripcion = new javax.swing.JTextField();
        prsaldoinicial = new javax.swing.JTextField();
        pringresos = new javax.swing.JTextField();
        pregresos = new javax.swing.JTextField();
        prajustes = new javax.swing.JTextField();
        prsaldofinal = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        prcostoproducto = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        prcombboxestatus = new javax.swing.JComboBox<>();
        prbtnagregarproducto = new javax.swing.JButton();
        prbtneliminarproducto = new javax.swing.JButton();
        prbtnbuscarproducto = new javax.swing.JButton();
        prbtnactualizarproducto = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        prprecioalpublico = new javax.swing.JTextField();
        prunidadmedida = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Cooper Black", 0, 24)); // NOI18N
        jLabel1.setText("Sistema de Facturación");

        jLabel2.setText("Cliente ID");

        fidcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fidclienteActionPerformed(evt);
            }
        });

        btnfverificarcli.setText("Verificar Cliente");
        btnfverificarcli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfverificarcliActionPerformed(evt);
            }
        });

        jLabel3.setText("Factura Nº");

        fnfactura.setEditable(false);

        fverificacioncliente.setEditable(false);
        fverificacioncliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fverificacionclienteActionPerformed(evt);
            }
        });

        ivacombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0 %", "12 %", "15 %", "18%" }));

        jLabel44.setText("IVA (Seleccione)");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(fidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnfverificarcli)
                        .addGap(18, 18, 18)
                        .addComponent(fverificacioncliente, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(263, 263, 263)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ivacombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fnfactura, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ivacombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnfverificarcli)
                            .addComponent(fidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fverificacioncliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(fnfactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        facturaciontabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(facturaciontabla);

        jLabel5.setText("Nombre Cliente");

        fidproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fidproductoActionPerformed(evt);
            }
        });

        btnfbuscarpro.setText("Buscar Producto");
        btnfbuscarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfbuscarproActionPerformed(evt);
            }
        });

        jLabel7.setText("Producto Descr.");

        jLabel8.setText("Unidad medida");

        funidadmedida.setEditable(false);
        funidadmedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                funidadmedidaActionPerformed(evt);
            }
        });

        jLabel9.setText("Cantidad");

        jLabel10.setText("Valor Unidad");

        fvalorunidad.setEditable(false);
        fvalorunidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fvalorunidadActionPerformed(evt);
            }
        });

        jLabel11.setText("Forma de Pago");

        fcantidad.setRequestFocusEnabled(false);

        fpago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EFECT", "TARCR", "TARDB", "TRANS", "CHEQ" }));

        btnfagregarproducto.setText("Agregar Producto");
        btnfagregarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfagregarproductoActionPerformed(evt);
            }
        });

        btnfgenerarfactura.setText("Generar Factura ");
        btnfgenerarfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfgenerarfacturaActionPerformed(evt);
            }
        });

        jLabel36.setText("Producto ID");

        fnombrecliente.setEditable(false);
        fnombrecliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fnombreclienteActionPerformed(evt);
            }
        });

        fprodescrcombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fprodescrcombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fprodescrcomboboxActionPerformed(evt);
            }
        });

        frespbuscarpro.setEditable(false);
        frespbuscarpro.setBorder(null);
        frespbuscarpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frespbuscarproActionPerformed(evt);
            }
        });

        btneliminarproducto.setText("Eliminar Producto");
        btneliminarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarproductoActionPerformed(evt);
            }
        });

        btnfverfacturas.setText("Ver todas las Facturas");
        btnfverfacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfverfacturasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fpago, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(btneliminarproducto)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnfgenerarfactura))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(fvalorunidad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                                .addComponent(fidproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnfbuscarpro))
                                            .addComponent(funidadmedida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fcantidad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fprodescrcombobox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(frespbuscarpro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(btnfagregarproducto))))
                        .addContainerGap(17, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnfverfacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(16, 16, 16)
                .addComponent(frespbuscarpro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fidproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfbuscarpro)
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(fprodescrcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(funidadmedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(fcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(fvalorunidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(btnfagregarproducto)
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(fpago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btneliminarproducto)
                    .addComponent(btnfgenerarfactura))
                .addGap(30, 30, 30)
                .addComponent(btnfverfacturas)
                .addGap(46, 46, 46))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jLabel6)
                    .addContainerGap(464, Short.MAX_VALUE)))
        );

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setText("IVA");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel39.setText("SUMA");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel40.setText("TOTAL");

        sumalabel.setText("$0.00");

        ivalabel.setText("$0.00");

        totallabel.setText("$0.00");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(totallabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ivalabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(sumalabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(28, 28, 28))))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(sumalabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(ivalabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(totallabel))))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Generacion Factura", jPanel1);

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel4.setText("Todos los Clientes");

        cltabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        cltabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cltablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cltabla);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Operaciones con Clientes");

        jLabel13.setText("ID Cliente");

        jLabel14.setText("Primer Nombre");

        jLabel15.setText("Segundo Nombre");

        jLabel16.setText("Primer Apellido");

        jLabel17.setText("Segundo Apellido");

        jLabel18.setText("Dirección");

        jLabel19.setText("Telefono");

        jLabel20.setText("Celular");

        clprimerapellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clprimerapellidoActionPerformed(evt);
            }
        });

        jLabel21.setText("Email");

        jLabel22.setText("Tipo");

        cltipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NOR", "EMP", "VIP" }));

        jLabel23.setText("Estatus");

        clestatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACT", "INA" }));

        btnagregarcliente.setText("Agregar Cliente");
        btnagregarcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarclienteActionPerformed(evt);
            }
        });

        btneliminarcliente.setText("Eliminar Cliente");
        btneliminarcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarclienteActionPerformed(evt);
            }
        });

        clbuscarcliente.setText("Buscar Cliente");
        clbuscarcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clbuscarclienteActionPerformed(evt);
            }
        });

        btnactualizarcliente.setText("Actualizar Cliente");
        btnactualizarcliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarclienteActionPerformed(evt);
            }
        });

        clbtnlimpiarcampos.setText("Limpiar Campos");
        clbtnlimpiarcampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clbtnlimpiarcamposActionPerformed(evt);
            }
        });

        jLabel41.setText("Codigo");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnagregarcliente, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(btneliminarcliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnactualizarcliente)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(clemail)
                                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cltelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                                        .addComponent(clcelular)))
                                                .addGap(29, 29, 29))
                                            .addComponent(cltipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(clsegundoapellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                                .addComponent(clprimerapellido, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(clsegundonombre, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(clprimernombre, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(cldireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(clestatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addGap(0, 1, Short.MAX_VALUE)
                                        .addComponent(iddelcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clbtnlimpiarcampos)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(12, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(clbuscarcliente))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72)
                                .addComponent(clid, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel4))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(clbuscarcliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel13))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iddelcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(clprimernombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(clsegundonombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(clprimerapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(clsegundoapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(cldireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(cltelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(clcelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(clemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(cltipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(clestatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnagregarcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnactualizarcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btneliminarcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clbtnlimpiarcampos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Clientes", jPanel2);

        jLabel24.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel24.setText("Todos los Productos");

        prtabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo ", "Descripcion", "Unidad medida", "Saldo inicial"
            }
        ));
        prtabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prtablaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(prtabla);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Operaciones con Productos");

        jLabel26.setText("Codigo Producto");

        jLabel27.setText("Desc. Procucto");

        jLabel28.setText("Unidad de medida");

        jLabel29.setText("Saldo Inicial Producto");

        jLabel30.setText("Ingresos Producto");

        jLabel31.setText("Egresos Producto");

        jLabel32.setText("Ajustes Producto");

        jLabel33.setText("Saldo Final Producto");

        prsaldoinicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prsaldoinicialActionPerformed(evt);
            }
        });

        jLabel34.setText("Costo Producto");

        jLabel35.setText("Estatus");

        prcombboxestatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACT", "INA" }));

        prbtnagregarproducto.setText("Agregar Producto");
        prbtnagregarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prbtnagregarproductoActionPerformed(evt);
            }
        });

        prbtneliminarproducto.setText("Eliminar Producto");
        prbtneliminarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prbtneliminarproductoActionPerformed(evt);
            }
        });

        prbtnbuscarproducto.setText("Buscar Producto");
        prbtnbuscarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prbtnbuscarproductoActionPerformed(evt);
            }
        });

        prbtnactualizarproducto.setText("Actualizar Producto");
        prbtnactualizarproducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prbtnactualizarproductoActionPerformed(evt);
            }
        });

        jLabel37.setText("Precio al Publico");

        prunidadmedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prunidadmedidaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prbtnbuscarproducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(207, 207, 207))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30)
                                .addComponent(prbtneliminarproducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel26)
                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(prbtnagregarproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prcostoproducto)
                                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(prajustes, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                                .addComponent(prsaldofinal, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                            .addComponent(prprecioalpublico))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(pringresos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                            .addComponent(prsaldoinicial, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prdescripcion, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prcod, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(prunidadmedida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(pregresos, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(prcombboxestatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(prbtnactualizarproducto)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24)
                    .addComponent(prbtnbuscarproducto))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(prcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(prdescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(prunidadmedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(prsaldoinicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(pringresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(pregresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(prajustes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(prsaldofinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(prcostoproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(prprecioalpublico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(prcombboxestatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prbtnagregarproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prbtnactualizarproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(prbtneliminarproducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Productos", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fidproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fidproductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fidproductoActionPerformed

    private void funidadmedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_funidadmedidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_funidadmedidaActionPerformed

    private void fvalorunidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fvalorunidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fvalorunidadActionPerformed

    private void clprimerapellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clprimerapellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clprimerapellidoActionPerformed

    private void btnagregarclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnagregarclienteActionPerformed
     // Obtener datos de los textfields
    String clCodigo = clid.getText().trim(); // Asumimos que clid es el código del cliente
    String clPrimerNombre = clprimernombre.getText().trim();
    String clSegundoNombre = clsegundonombre.getText().trim();
    String clPrimerApellido = clprimerapellido.getText().trim();
    String clSegundoApellido = clsegundoapellido.getText().trim();
    String iddelCliente = iddelcliente.getText().trim();
    String clDireccion = cldireccion.getText().trim();
    String clTelefono = cltelefono.getText().trim();
    String clCelular = clcelular.getText().trim();
    String clEmail = clemail.getText().trim();
    
    // Obtener datos de los combobox
    String clTipo = (String) cltipo.getSelectedItem();
    String clEstatus = (String) clestatus.getSelectedItem();

    // Validar datos
    if (clCodigo.isEmpty() || clPrimerNombre.isEmpty() || clPrimerApellido.isEmpty() || clDireccion.isEmpty() || 
        clTelefono.isEmpty() || clCelular.isEmpty() || clTipo.isEmpty() || clEstatus.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Crear el nombre completo
    String clNombreCompleto = clPrimerNombre + " " + (clSegundoNombre.isEmpty() ? "" : clSegundoNombre + " ") + clPrimerApellido + 
                              (clSegundoApellido.isEmpty() ? "" : " " + clSegundoApellido);

    // Insertar cliente en la base de datos
    String query = "INSERT INTO CLIENTES (CLICODIGO, CLINOMBRE1, CLINOMBRE2, CLIAPELLIDO1, CLIAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = new Conexion().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, clCodigo);
        ps.setString(2, clPrimerNombre);
        ps.setString(3, clSegundoNombre);
        ps.setString(4, clPrimerApellido);
        ps.setString(5, clSegundoApellido);
        ps.setString(6, clNombreCompleto);
        ps.setString(7, iddelCliente); // Asumiendo que clid es el campo CLIIDENTIFICACION
        ps.setString(8, clDireccion);
        ps.setString(9, clTelefono);
        ps.setString(10, clCelular);
        ps.setString(11, clEmail);
        ps.setString(12, clTipo);
        ps.setString(13, clEstatus);

        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
         DefaultTableModel model = (DefaultTableModel) cltabla.getModel();
    Object[] newRow = {
        clid.getText().trim(),
        clprimernombre.getText().trim(),
        clsegundonombre.getText().trim(),
        clprimerapellido.getText().trim(),
        clsegundoapellido.getText().trim(),
        clprimernombre.getText().trim() + " " + clprimerapellido.getText().trim(),
        cldireccion.getText().trim(),
        cltelefono.getText().trim(),
        clcelular.getText().trim(),
        clemail.getText().trim(),
        cltipo.getSelectedItem().toString(),
        clestatus.getSelectedItem().toString()
    };
    model.addRow(newRow);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: "+e, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_btnagregarclienteActionPerformed

    private void prsaldoinicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prsaldoinicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prsaldoinicialActionPerformed

    private void prbtnagregarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prbtnagregarproductoActionPerformed
        // Obtener valores de los campos de texto y combobox
    String codigo = prcod.getText().trim();
    String descripcion = prdescripcion.getText().trim();
    String unidadMedida = prunidadmedida.getText().trim();
    String saldoInicialStr = prsaldoinicial.getText().trim();
    String ingresosStr = pringresos.getText().trim();
    String egresosStr = pregresos.getText().trim();
    String ajustesStr = prajustes.getText().trim();
    String saldoFinalStr = prsaldofinal.getText().trim();
    String costoProductoStr = prcostoproducto.getText().trim();
    String precioAlPublicoStr = prprecioalpublico.getText().trim();
    String estatus = prcombboxestatus.getSelectedItem().toString();

    // Validar los datos de entrada
    if (codigo.isEmpty() || descripcion.isEmpty() || unidadMedida.isEmpty() || 
        saldoInicialStr.isEmpty() || ingresosStr.isEmpty() || egresosStr.isEmpty() ||
        ajustesStr.isEmpty() || saldoFinalStr.isEmpty() || costoProductoStr.isEmpty() ||
        precioAlPublicoStr.isEmpty() || estatus.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Convertir valores numéricos
        double saldoInicial = Double.parseDouble(saldoInicialStr);
        double ingresos = Double.parseDouble(ingresosStr);
        double egresos = Double.parseDouble(egresosStr);
        double ajustes = Double.parseDouble(ajustesStr);
        double saldoFinal = Double.parseDouble(saldoFinalStr);
        double costoProducto = Double.parseDouble(costoProductoStr);
        double precioAlPublico = Double.parseDouble(precioAlPublicoStr);

        // Verificar restricciones
        if (saldoInicial < 0 || ingresos < 0 || egresos < 0 || ajustes < 0 || saldoFinal < 0 || costoProducto < 0 || precioAlPublico < 0) {
            JOptionPane.showMessageDialog(this, "Los valores numéricos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insertar datos en la base de datos
        String query = "INSERT INTO PRODUCTOS (PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS, PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = new Conexion().getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, codigo);
            pst.setString(2, descripcion);
            pst.setString(3, unidadMedida);
            pst.setDouble(4, saldoInicial);
            pst.setDouble(5, ingresos);
            pst.setDouble(6, egresos);
            pst.setDouble(7, ajustes);
            pst.setDouble(8, saldoFinal);
            pst.setDouble(9, costoProducto);
            pst.setDouble(10, precioAlPublico);
            pst.setString(11, estatus);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
    }
     // Actualizar la tabla
    cargarProductos();
    }//GEN-LAST:event_prbtnagregarproductoActionPerformed

    private void prbtneliminarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prbtneliminarproductoActionPerformed
       // Obtener el código del producto del campo de texto
    String codigo = prcod.getText().trim();

    // Validar que el campo no esté vacío
    if (codigo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el código del producto.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mostrar cuadro de confirmación
    int response = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

    // Si el usuario confirma la eliminación
    if (response == JOptionPane.YES_OPTION) {
        String query = "DELETE FROM PRODUCTOS WHERE PROCODIGO = ?";

        try (Connection con = new Conexion().getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, codigo);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar los campos del formulario
                prcod.setText("");
                prdescripcion.setText("");
                prunidadmedida.setText("");
                prsaldoinicial.setText("");
                pringresos.setText("");
                pregresos.setText("");
                prajustes.setText("");
                prsaldofinal.setText("");
                prcostoproducto.setText("");
                prprecioalpublico.setText("");
                prcombboxestatus.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     // Actualizar la tabla
    cargarProductos();
    }//GEN-LAST:event_prbtneliminarproductoActionPerformed

    private void fnombreclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fnombreclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fnombreclienteActionPerformed

    private void fverificacionclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fverificacionclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fverificacionclienteActionPerformed

    private void btnfverificarcliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfverificarcliActionPerformed
     String idCliente = fidcliente.getText().trim();

        if (idCliente.isEmpty()) {
            fverificacioncliente.setText("Por favor ingrese un ID de cliente.");
            return;
        }

        Conexion postgresConnection = new Conexion();
        Connection con = postgresConnection.getConnection();

        Deber42 deber = new Deber42();

        String nombreCompleto = deber.verificarCliente(con, idCliente);

        if (nombreCompleto != null) {
            fverificacioncliente.setText("Cliente Encontrado");
            fnombrecliente.setText(nombreCompleto);
        } else {
            fverificacioncliente.setText("El cliente no existe, cárguelo a la base de datos");
            fnombrecliente.setText("");
        }
    

    }//GEN-LAST:event_btnfverificarcliActionPerformed

    private void fidclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fidclienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fidclienteActionPerformed

    private void btnfbuscarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfbuscarproActionPerformed
 String idProducto = fidproducto.getText().trim();
        if (idProducto.isEmpty()) {
            frespbuscarpro.setText("Ingrese un ID de producto.");
            return;
        }

        // Instancia de la conexión a la base de datos
        Conexion postgresConnection = new Conexion();
        Connection con = postgresConnection.getConnection();

        // Llamar al método para buscar el producto y actualizar los campos
        Deber42 deber = new Deber42();
        boolean encontrado = deber.buscarProducto(con, idProducto, fprodescrcombobox, funidadmedida, fvalorunidad);

        if (!encontrado) {
            frespbuscarpro.setText("Producto no Encontrado");
            funidadmedida.setText("");
            fvalorunidad.setText("");
            
        }else{frespbuscarpro.setText("Producto Disponible");}
    }//GEN-LAST:event_btnfbuscarproActionPerformed

    private void fprodescrcomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fprodescrcomboboxActionPerformed
          String descripcionProducto = (String) fprodescrcombobox.getSelectedItem();
        if (descripcionProducto == null || descripcionProducto.isEmpty()) {
            return;
        }

        Conexion postgresConnection = new Conexion();
        Connection con = postgresConnection.getConnection();

        Deber42 deber = new Deber42();
        deber.obtenerInformacionProducto(con, descripcionProducto, fidproducto, funidadmedida, fvalorunidad);

        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_fprodescrcomboboxActionPerformed

    private void frespbuscarproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frespbuscarproActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_frespbuscarproActionPerformed

    private void btnfagregarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfagregarproductoActionPerformed
          String idProducto = fidproducto.getText().trim();
        String descripcion = (String) fprodescrcombobox.getSelectedItem();
        String unidadMedida = funidadmedida.getText().trim();
        
        // Obtener cantidad del JSpinner
        int cantidad = (int) fcantidad.getValue();
        
        String valorUnitario = fvalorunidad.getText().trim();

        // Calcular el subtotal
        double subtotal = 0.0;
        try {
            double valor = Double.parseDouble(valorUnitario);
            subtotal = valor * cantidad;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Crear un arreglo con los datos del producto
        String[] rowData = {idProducto, descripcion, unidadMedida, String.valueOf(cantidad), valorUnitario, String.valueOf(subtotal)};

        // Agregar la fila al modelo de la tabla
        tableModel.addRow(rowData);
             actualizarSumas();
    }//GEN-LAST:event_btnfagregarproductoActionPerformed

    private void btneliminarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarproductoActionPerformed
        int selectedRow = facturaciontabla.getSelectedRow();
        if (selectedRow != -1) { // Verifica si hay una fila seleccionada
            tableModel.removeRow(selectedRow); // Elimina la fila del modelo de la tabla
        } else {
            // Muestra un mensaje si no hay fila seleccionada
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        actualizarSumas();
    }//GEN-LAST:event_btneliminarproductoActionPerformed

    private void btnfgenerarfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfgenerarfacturaActionPerformed
     
   
 // Obtener datos del formulario
    String facNumero = fnfactura.getText().trim();
    String cliIdentificacion = fidcliente.getText().trim();
   String formaPago = (String) fpago.getSelectedItem();

    // Obtener el código del cliente a partir de su identificación
    String cliCodigo = obtenerCodigoCliente(cliIdentificacion);

    // Si no se encuentra el cliente, mostrar un mensaje de error
    if (cliCodigo == null) {
        JOptionPane.showMessageDialog(this, "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener la fecha actual
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String facFecha = sdf.format(new Date());

    // Obtener la suma de los subtotales, el IVA y el total
    double facSubTotal = Double.parseDouble(sumalabel.getText().replace(",", ""));
    double facIva = Double.parseDouble(ivalabel.getText().replace(",", ""));
    double facTotal = facSubTotal + facIva;

    // Insertar la factura en la tabla FACTURAS
    insertarFactura(facNumero, cliCodigo, facFecha, facSubTotal, facIva, formaPago);

    // Insertar cada producto en la tabla PXF
    insertarProductosEnFactura(facNumero);

    // Generar PDF y XML
    try {
        
        generarFacturaXML(facNumero, cliCodigo, facFecha, facSubTotal, facIva, facTotal);
        JOptionPane.showMessageDialog(this, "Ejecutado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al generar XML", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnfgenerarfacturaActionPerformed

    private void btnactualizarclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarclienteActionPerformed
      // Obtener datos de los textfields
    String clCodigo = clid.getText().trim(); // Asumimos que clid es el código del cliente
    String clPrimerNombre = clprimernombre.getText().trim();
    String clSegundoNombre = clsegundonombre.getText().trim();
    String clPrimerApellido = clprimerapellido.getText().trim();
    String clSegundoApellido = clsegundoapellido.getText().trim();
    String clDireccion = cldireccion.getText().trim();
    String clTelefono = cltelefono.getText().trim();
    String clCelular = clcelular.getText().trim();
    String clEmail = clemail.getText().trim();
    
    // Obtener datos de los combobox
    String clTipo = (String) cltipo.getSelectedItem();
    String clEstatus = (String) clestatus.getSelectedItem();

    // Validar datos
    if (clCodigo.isEmpty() || clPrimerNombre.isEmpty() || clPrimerApellido.isEmpty() || clDireccion.isEmpty() || 
        clTelefono.isEmpty() || clCelular.isEmpty() || clTipo.isEmpty() || clEstatus.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Crear el nombre completo
    String clNombreCompleto = clPrimerNombre + " " + (clSegundoNombre.isEmpty() ? "" : clSegundoNombre + " ") + clPrimerApellido + 
                              (clSegundoApellido.isEmpty() ? "" : " " + clSegundoApellido);

    // Actualizar cliente en la base de datos
    String query = "UPDATE CLIENTES SET CLINOMBRE1 = ?, CLINOMBRE2 = ?, CLIAPELLIDO1 = ?, CLIAPELLIDO2 = ?, CLINOMBRE = ?, " +
                   "CLIDIRECCION = ?, CLITELEFONO = ?, CLICELULAR = ?, CLIEMAIL = ?, CLITIPO = ?, CLISTATUS = ? " +
                   "WHERE CLICODIGO = ?";

    try (Connection con = new Conexion().getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, clPrimerNombre);
        ps.setString(2, clSegundoNombre);
        ps.setString(3, clPrimerApellido);
        ps.setString(4, clSegundoApellido);
        ps.setString(5, clNombreCompleto);
        ps.setString(6, clDireccion);
        ps.setString(7, clTelefono);
        ps.setString(8, clCelular);
        ps.setString(9, clEmail);
        ps.setString(10, clTipo);
        ps.setString(11, clEstatus);
        ps.setString(12, clCodigo);

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el cliente con el código proporcionado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar cliente", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnactualizarclienteActionPerformed

    private void btneliminarclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarclienteActionPerformed
      // Obtener el código del cliente desde el textfield
    String clCodigo = clid.getText().trim();

    // Validar que el código no esté vacío
    if (clCodigo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el código del cliente", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mostrar cuadro de diálogo de confirmación
    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el cliente con código: " + clCodigo + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    
    // Si el usuario confirma la eliminación
    if (confirm == JOptionPane.YES_OPTION) {
        // Eliminar cliente de la base de datos
        String query = "DELETE FROM CLIENTES WHERE CLICODIGO = ?";

        try (Connection con = new Conexion().getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, clCodigo);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                //variable de seleccion de fila
                int selectedRow = cltabla.getSelectedRow();
                
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                //Remueve la tabla seleccionada solo si se ejecuta la accion
                DefaultTableModel model = (DefaultTableModel) cltabla.getModel();
                 model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente con el código proporcionado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     int selectedRow = cltabla.getSelectedRow();
    
    }//GEN-LAST:event_btneliminarclienteActionPerformed

    private void cltablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cltablaMouseClicked
   int selectedRow = cltabla.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel) cltabla.getModel();

    // Asegúrate de que los índices de las columnas coincidan con la base de datos
    clid.setText(model.getValueAt(selectedRow, 0).toString());
    clprimernombre.setText(model.getValueAt(selectedRow, 1).toString());
    clsegundonombre.setText(model.getValueAt(selectedRow, 2).toString());
    clprimerapellido.setText(model.getValueAt(selectedRow, 3).toString());
    clsegundoapellido.setText(model.getValueAt(selectedRow, 4).toString());
    cldireccion.setText(model.getValueAt(selectedRow, 6).toString());
    cltelefono.setText(model.getValueAt(selectedRow, 9).toString());
    clcelular.setText(model.getValueAt(selectedRow, 8).toString());
    clemail.setText(model.getValueAt(selectedRow, 7).toString());
    cltipo.setSelectedItem(model.getValueAt(selectedRow, 10).toString());
    clestatus.setSelectedItem(model.getValueAt(selectedRow, 11).toString());
    }//GEN-LAST:event_cltablaMouseClicked

    private void clbuscarclienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clbuscarclienteActionPerformed
String clienteID = clid.getText().trim();
    
    if (clienteID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String query = "SELECT * FROM CLIENTES WHERE CLICODIGO = ?";

    try (Connection con = new Conexion().getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setString(1, clienteID);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                clprimernombre.setText(rs.getString("CLINOMBRE1"));
                clsegundonombre.setText(rs.getString("CLINOMBRE2"));
                clprimerapellido.setText(rs.getString("CLIAPELLIDO1"));
                clsegundoapellido.setText(rs.getString("CLIAPELLIDO2"));
                 iddelcliente.setText(rs.getString("CLIIDENTIFICACION"));//
                cldireccion.setText(rs.getString("CLIDIRECCION"));
                cltelefono.setText(rs.getString("CLITELEFONO"));
                clcelular.setText(rs.getString("CLICELULAR"));
                clemail.setText(rs.getString("CLIEMAIL"));
                cltipo.setSelectedItem(rs.getString("CLITIPO"));
                clestatus.setSelectedItem(rs.getString("CLISTATUS"));
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al buscar cliente", "Error", JOptionPane.ERROR_MESSAGE);
    }        
    }//GEN-LAST:event_clbuscarclienteActionPerformed

    private void clbtnlimpiarcamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clbtnlimpiarcamposActionPerformed
    clid.setText("");
    clprimernombre.setText("");
    clsegundonombre.setText("");
    clprimerapellido.setText("");
    clsegundoapellido.setText("");
    cldireccion.setText("");
    cltelefono.setText("");
    clcelular.setText("");
    clemail.setText("");
    cltipo.setSelectedIndex(-1); // O un índice válido, si prefieres
    clestatus.setSelectedIndex(-1); // O un índice válido, si prefieres
    }//GEN-LAST:event_clbtnlimpiarcamposActionPerformed

    private void prunidadmedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prunidadmedidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prunidadmedidaActionPerformed

    private void prbtnactualizarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prbtnactualizarproductoActionPerformed
      // Obtener valores de los campos de texto y combobox
    String codigo = prcod.getText().trim();
    String descripcion = prdescripcion.getText().trim();
    String unidadMedida = prunidadmedida.getText().trim();
    String saldoInicialStr = prsaldoinicial.getText().trim();
    String ingresosStr = pringresos.getText().trim();
    String egresosStr = pregresos.getText().trim();
    String ajustesStr = prajustes.getText().trim();
    String saldoFinalStr = prsaldofinal.getText().trim();
    String costoProductoStr = prcostoproducto.getText().trim();
    String precioAlPublicoStr = prprecioalpublico.getText().trim();
    String estatus = prcombboxestatus.getSelectedItem().toString();

    // Validar los datos de entrada
    if (codigo.isEmpty() || descripcion.isEmpty() || unidadMedida.isEmpty() || 
        saldoInicialStr.isEmpty() || ingresosStr.isEmpty() || egresosStr.isEmpty() ||
        ajustesStr.isEmpty() || saldoFinalStr.isEmpty() || costoProductoStr.isEmpty() ||
        precioAlPublicoStr.isEmpty() || estatus.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Convertir valores numéricos
        double saldoInicial = Double.parseDouble(saldoInicialStr);
        double ingresos = Double.parseDouble(ingresosStr);
        double egresos = Double.parseDouble(egresosStr);
        double ajustes = Double.parseDouble(ajustesStr);
        double saldoFinal = Double.parseDouble(saldoFinalStr);
        double costoProducto = Double.parseDouble(costoProductoStr);
        double precioAlPublico = Double.parseDouble(precioAlPublicoStr);

        // Verificar restricciones
        if (saldoInicial < 0 || ingresos < 0 || egresos < 0 || ajustes < 0 || saldoFinal < 0 || costoProducto < 0 || precioAlPublico < 0) {
            JOptionPane.showMessageDialog(this, "Los valores numéricos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Actualizar datos en la base de datos
        String query = "UPDATE PRODUCTOS SET PRODESCRIPCION = ?, PROUNIDADMEDIDA = ?, PROSALDOINICIAL = ?, PROINGRESOS = ?, PROEGRESOS = ?, PROAJUSTES = ?, PROSALDOFINAL = ?, PROCOSTOUM = ?, PROPRECIOUM = ?, PROSTATUS = ? WHERE PROCODIGO = ?";

        try (Connection con = new Conexion().getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, descripcion);
            pst.setString(2, unidadMedida);
            pst.setDouble(3, saldoInicial);
            pst.setDouble(4, ingresos);
            pst.setDouble(5, egresos);
            pst.setDouble(6, ajustes);
            pst.setDouble(7, saldoFinal);
            pst.setDouble(8, costoProducto);
            pst.setDouble(9, precioAlPublico);
            pst.setString(10, estatus);
            pst.setString(11, codigo);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un producto con el código especificado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_prbtnactualizarproductoActionPerformed

    private void prbtnbuscarproductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prbtnbuscarproductoActionPerformed
// Obtener el código del producto del campo de texto
    String codigo = prcod.getText().trim();

    // Validar que el campo no esté vacío
    if (codigo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el código del producto.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consulta para obtener los datos del producto
    String query = "SELECT PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS, PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS FROM PRODUCTOS WHERE PROCODIGO = ?";

    try (Connection con = new Conexion().getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {
        
        pst.setString(1, codigo);
        
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // Rellenar los campos con los datos obtenidos
                prdescripcion.setText(rs.getString("PRODESCRIPCION"));
                prunidadmedida.setText(rs.getString("PROUNIDADMEDIDA"));
                prsaldoinicial.setText(rs.getString("PROSALDOINICIAL"));
                pringresos.setText(rs.getString("PROINGRESOS"));
                pregresos.setText(rs.getString("PROEGRESOS"));
                prajustes.setText(rs.getString("PROAJUSTES"));
                prsaldofinal.setText(rs.getString("PROSALDOFINAL"));
                prcostoproducto.setText(rs.getString("PROCOSTOUM"));
                prprecioalpublico.setText(rs.getString("PROPRECIOUM"));
                prcombboxestatus.setSelectedItem(rs.getString("PROSTATUS"));
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al buscar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
    }     
    }//GEN-LAST:event_prbtnbuscarproductoActionPerformed

    private void prtablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prtablaMouseClicked
        int selectedRow = prtabla.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel) prtabla.getModel();

    prcod.setText(model.getValueAt(selectedRow, 0).toString());
    prdescripcion.setText(model.getValueAt(selectedRow, 1).toString());
    prunidadmedida.setText(model.getValueAt(selectedRow, 2).toString());
    prsaldoinicial.setText(model.getValueAt(selectedRow, 3).toString());
    pringresos.setText(model.getValueAt(selectedRow, 4).toString());
    pregresos.setText(model.getValueAt(selectedRow, 5).toString());
    prajustes.setText(model.getValueAt(selectedRow, 6).toString());
    prsaldofinal.setText(model.getValueAt(selectedRow, 7).toString());
    prcostoproducto.setText(model.getValueAt(selectedRow, 8).toString());
    prprecioalpublico.setText(model.getValueAt(selectedRow, 9).toString());
    prcombboxestatus.setSelectedItem(model.getValueAt(selectedRow, 10).toString());
    }//GEN-LAST:event_prtablaMouseClicked

    private void btnfverfacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfverfacturasActionPerformed
        JFrame facturasFrame = new JFrame("Todas las Facturas");
    facturasFrame.setSize(800, 600);
    facturasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    facturasFrame.setLocationRelativeTo(null);

    JTable facturaTable = new JTable();
    JScrollPane scrollPane = new JScrollPane(facturaTable);
    facturasFrame.add(scrollPane, BorderLayout.CENTER);

    DefaultTableModel model = new DefaultTableModel(new String[]{
        "Número", "Código Cliente", "Fecha", "Subtotal", "Descuento", "IVA", "ICE", "Forma de Pago", "Estado"
    }, 0);
    facturaTable.setModel(model);

    String query = "SELECT * FROM FACTURAS";

    try (Connection con = new Conexion().getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("FACNUMERO"),
                rs.getString("CLICODIGO"),
                rs.getDate("FACFECHA"),
                rs.getBigDecimal("FACSUBTOTAL"),
                rs.getBigDecimal("FACDESCUENTO"),
                rs.getBigDecimal("FACIVA"),
                rs.getBigDecimal("FACICE"),
                rs.getString("FACFORMAPAGO"),
                rs.getString("FACSTATUS")
            });
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar las facturas.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    facturasFrame.setVisible(true);
    }//GEN-LAST:event_btnfverfacturasActionPerformed
   
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizarcliente;
    private javax.swing.JButton btnagregarcliente;
    private javax.swing.JButton btneliminarcliente;
    private javax.swing.JButton btneliminarproducto;
    private javax.swing.JButton btnfagregarproducto;
    private javax.swing.JButton btnfbuscarpro;
    private javax.swing.JButton btnfgenerarfactura;
    private javax.swing.JButton btnfverfacturas;
    private javax.swing.JButton btnfverificarcli;
    private javax.swing.JButton clbtnlimpiarcampos;
    private javax.swing.JButton clbuscarcliente;
    private javax.swing.JTextField clcelular;
    private javax.swing.JTextField cldireccion;
    private javax.swing.JTextField clemail;
    private javax.swing.JComboBox<String> clestatus;
    private javax.swing.JTextField clid;
    private javax.swing.JTextField clprimerapellido;
    private javax.swing.JTextField clprimernombre;
    private javax.swing.JTextField clsegundoapellido;
    private javax.swing.JTextField clsegundonombre;
    private javax.swing.JTable cltabla;
    private javax.swing.JTextField cltelefono;
    private javax.swing.JComboBox<String> cltipo;
    private javax.swing.JTable facturaciontabla;
    private javax.swing.JSpinner fcantidad;
    private javax.swing.JTextField fidcliente;
    private javax.swing.JTextField fidproducto;
    private javax.swing.JTextField fnfactura;
    private javax.swing.JTextField fnombrecliente;
    private javax.swing.JComboBox<String> fpago;
    private javax.swing.JComboBox<String> fprodescrcombobox;
    private javax.swing.JTextField frespbuscarpro;
    private javax.swing.JTextField funidadmedida;
    private javax.swing.JTextField fvalorunidad;
    private javax.swing.JTextField fverificacioncliente;
    private javax.swing.JTextField iddelcliente;
    private javax.swing.JComboBox<String> ivacombobox;
    private javax.swing.JLabel ivalabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField prajustes;
    private javax.swing.JButton prbtnactualizarproducto;
    private javax.swing.JButton prbtnagregarproducto;
    private javax.swing.JButton prbtnbuscarproducto;
    private javax.swing.JButton prbtneliminarproducto;
    private javax.swing.JTextField prcod;
    private javax.swing.JComboBox<String> prcombboxestatus;
    private javax.swing.JTextField prcostoproducto;
    private javax.swing.JTextField prdescripcion;
    private javax.swing.JTextField pregresos;
    private javax.swing.JTextField pringresos;
    private javax.swing.JTextField prprecioalpublico;
    private javax.swing.JTextField prsaldofinal;
    private javax.swing.JTextField prsaldoinicial;
    private javax.swing.JTable prtabla;
    private javax.swing.JTextField prunidadmedida;
    private javax.swing.JLabel sumalabel;
    private javax.swing.JLabel totallabel;
    // End of variables declaration//GEN-END:variables

    
}
