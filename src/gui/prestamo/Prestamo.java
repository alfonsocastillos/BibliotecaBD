package gui.prestamo;

import dataBase.dao.ClienteDAO;
import dataBase.dao.DPrestamoDAO;
import dataBase.dao.PrestamoDAO;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tools.UtilsTable;

/**
 * Ventana que registra un prestamo de libros
 */

public class Prestamo  extends javax.swing.JInternalFrame {
       String Empleado_id;
    String Sucursal_id;
    String Cliente_id;
    String EmpleadoNombre;
    boolean editando = false;
    
    // Para consultar los datos de la DB
    ClienteDAO Cliente_DAO;
    // para guardar los datos consultados
    Object Libros_Lista [][];
    Object clientes[][];
    Object generos[][];
   
    // Ventana que agrega peliculas
    AddLibrosPrestamo addLibrosPrestamo;
    DefaultTableModel modeloLibros;

    /**
     * Creates new form Libros
     * @param Empleado_id
     * @param Sucursal_id
     * @param EmpleadoNombre
     */
    public Prestamo (String Empleado_id, String Sucursal_id, String EmpleadoNombre) {
        initComponents();
        // se obtienen los datos del empleado y sucursal
        this.Empleado_id = Empleado_id;
        this.Sucursal_id = Sucursal_id;
        this.EmpleadoNombre = EmpleadoNombre;
        
        // pone el nombre del empleado
        lblEmpleadoIn.setText(this.EmpleadoNombre);
        // Consulta los datos
        ClienteDAO = new ClienteDAO();
        // crea instancia de las ventanas
        addLibrosPrestamo = new AddLibrosPrestamo(null, true);        
        // llena los datos
        llenaDatos();
    }
    
    private void llenaDatos(){
        llenaClientes();
        llenaTablaLibros();
    }
    
    private void llenaTablaLibros(){    
        // obtiene fecha
        Calendar c = Calendar.getInstance();
        // Pone la fecha de prestamo
        lblFechaOut.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        c.add(Calendar.DAY_OF_YEAR, 3);
        // Pone la fecha de entrega 
        lblFechaIn.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        // Configuración de la tabla donde se mostraran los prestamos
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT, javax.swing.SwingConstants.RIGHT}};
        int[][] cellSize = {{0,0},
                            {1,640},
                            {2,50}};
        String[] T_LIBRO = {"","Título" };
        // llena la tabla 
        UtilsTable.llenaTabla(tableList,null, T_LIBRO, cellAlignment, cellSize);
        // obtiene el modelo de la tabla (datos)
        modeloLibros = (DefaultTableModel) tableList.getModel();
    }    
    
    private void llenaClientes(){
        // Consulta datos de los clientes
        clientes = ClienteDAO.getClientes();
        cmbCliente.addItem("Seleccione un cliente...");
        for (Object[] Cliente : clientes) {
            // agrega datos de los clientes al combo
            cmbCliente.addItem(Cliente[1] + " " + Cliente[2]);
        }
    }
    
    // Valida si se llenaron los datos
    private boolean estanLlenos(){        
        if (cmbCliente.getSelectedIndex() == 0){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.", "Aviso",2);
            
            return false;
        }
        else if (tableList.getRowCount() == 0){
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "No hay Libros en el prestamo.","Aviso",2);
            return false;
        }
        else
            return true;
    }
    
    private void showReporte(String id){
        // muestra el reporte y le manda el id de la renta
        VentanaReportePrestamo reporteVenta = new VentanaReportePrestamo(id);
        reporteVenta.setVisible(true);
        // Limpia campos
        limpia();
    }
    
    private void limpia(){
        cmbCliente.setSelectedIndex(0);
        Calendar c = Calendar.getInstance();
        lblFechaOut.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        c.add(Calendar.DAY_OF_YEAR, 3);
        lblFechaIn.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        UtilsTable.limpiaTabla(tableList);
    }
    
    private boolean verificaRepetido(String Libro_id){
        for (int i = 0; i < tableList.getRowCount(); i++){
            if (tableList.getValueAt(i, 0).toString().equals(Libro_id))
                return false;
        }
        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        btnAdd = new javax.swing.JButton();
        btnGuarda = new javax.swing.JButton();
        lblCliente = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        lblFechaOut = new javax.swing.JLabel();
        lblEmpleadoIn = new javax.swing.JLabel();
        lblEntrega = new javax.swing.JLabel();
        lblFechaIn = new javax.swing.JLabel();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tableList = new javax.swing.JTable();
        btnAddLibro = new javax.swing.JButton();
        btnBorrarLib = new javax.swing.JButton();
        pnlTableList3 = new javax.swing.JPanel();
        scpTableList2 = new javax.swing.JScrollPane();
        
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Prestamo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/renta.png"))); // NOI18N
        setName(""); // NOI18N

        pnlPrestamo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlPrestamo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAdd.setToolTipText("Nuevo Prestamo");
        btnAdd.setFocusable(false);

        pnlPrestamo.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 40, 40));

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardaer renta");
        btnGuarda.setFocusable(false);

        pnlPrestamo.add(btnGuarda, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, 40, 40));

        lblCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblCliente.setText("Cliente:");
        pnlPrestamo.add(lblCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 20));

        lblFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFecha.setText("Fecha:");
        pnlPrestamo.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, -1, 20));

        lblEmpleado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEmpleado.setText("Empleado:");
        pnlPrestamo.add(lblEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        pnlPrestamo.add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 280, -1));

        lblFechaOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaOut.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblFechaOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 90, 20));

        lblEmpleadoIn.setAlignmentX(0.5F);
        lblEmpleadoIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblEmpleadoIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 280, 20));

        lblEntrega.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEntrega.setText("Entrega:");
        pnlPrestamo.add(lblEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, 20));

        lblFechaIn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblFechaIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 90, 20));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle de renta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tableList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
            }
        ));
        tableList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tableListMouseExited(evt);
            }
        });
        tableList.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                tableListComponentMoved(evt);
            }
        });
        scpTableList.setViewportView(tableList);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 710, 120));

        btnAddLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAddLibro.setToolTipText("Agregar Libro");
        btnAddLibro.setFocusable(false);

        pnlTableList.add(btnAddLibro, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 40, 40));

        btnBorrarLib.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnBorrarLib.setToolTipText("Borrar Libro");
        btnBorrarLib.setFocusable(false);

        pnlTableList.add(btnBorrarLib, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 40, 40));

        scpTableList2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlTableList3.add(scpTableList2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 130));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlTableList3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTableList3, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void btnGuardaActionPerforme(java.awt.event.ActionEvent evt) {                                          
//         Guarda y actualiza los datos
        if (estanLlenos()){
                // Guarda datos de los controles en un arreglo de objetos
                Object[] Prestamo = new Object[5];
                Prestamo[0] = Sucursal_id;  
                Prestamo[1] = lblFechaOut.getText();
                Prestamo[2] = clientes[cmbCliente.getSelectedIndex()-1][0].toString();
                Prestamo[3] = lblFechaIn.getText();
                Prestamo[4] = Empleado_id;
                // Guarda la renta y recupera el id
                PrestamoDAO PrestamoDAO = new PrestamoDAO();
                String id = PrestamoDAO.savePrestamo(Prestamo);
                if (id != null){        
                    // si guardo el prestamo, guarda los datos del detalle
                    DPrestamoDAO DPrestamoDAO = new DPrestamoDAO();
                    int i;
                    for (i = 0; i < tableList.getRowCount(); i++)
                        DPrestamoDAO.saveDPrestamo(id,Integer.parseInt(tableList.getValueAt(i, 0).toString()));
                    }
                else
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar","Información",1);
            }
    }                                         

    private void btnAddActionPerformed (java.awt.event.ActionEvent evt) {                                       
        // Limpia para agragar
        limpia();
    }                                      

    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {                                       
     
    }                                      

    private void tableListMouseExited(java.awt.event.MouseEvent evt) {                                      
    }                                     

    private void tableListComponentMoved(java.awt.event.ComponentEvent evt) {                                         
    }                                        

    private void btnBorrarLibActionPerformed (java.awt.event.ActionEvent evt) {                                             
        // Borrar libro del detalle
        // valida si selecciono la fila
         if (tableList.getSelectedRow() < 0){
             // Suena un beep
             Toolkit.getDefaultToolkit().beep();
             javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.","Aviso",2);
        }
    }                                            

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddLibro;
    private javax.swing.JButton btnBorrarLib;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblEmpleadoIn;
    private javax.swing.JLabel lblEntrega;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaIn;
    private javax.swing.JLabel lblFechaOut;
    private javax.swing.JPanel pnlPrestamo;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList3;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JScrollPane scpTableList2;
    private javax.swing.JTable tableList;
    // End of variables declaration                   
 
}
