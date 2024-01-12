package gui.prestamo;

import dataBase.dao.ClienteDAO;
import dataBase.dao.DPrestamoDAO;
import dataBase.dao.DaoPayment;
import dataBase.dao.PrestamoDAO;
import dataBase.dao.DaoTPagos;
import java.awt.Frame;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tools.UtilsTable;

/**
 *
 * @author Carlos
 * Ventana que registra una renta de peliculas
 */
public class Prestamo extends javax.swing.JInternalFrame {
    String id_empleado;
    String id_sucursal;
    String idCliente;
    int idTipoPago;
    String empleado_nombre;
    boolean editando = false;
    // Para consultar los datos de la DB
    ClienteDAO daoCliente;
    DaoTPagos daoTPagos;
    // para guardar los datos consultados
    Object filmsLista [][];
    Object languajes[][];
    Object cliente[][];
    Object categories[][];
    Object tiposPago[][];
    // Ventana que agrega peliculas
    AddLibrosPrestamo addLibrosPrestamo;
    //AddPago addPago;
    DefaultTableModel modeloLibros;
    DefaultTableModel modeloPagos;
    float totalSum = 0;

    /**
     * Creates new form Peliculas
     * @param id_empleado
     * @param id_sucursal
     * @param empleado_nombre
     */
    public Prestamo(String id_empleado, String id_sucursal, String empleado_nombre) {
    //public Prestamo() {  
    initComponents();
        // se obtienen los datos del empkleado y tienda
        this.id_empleado = id_empleado;
        this.id_sucursal = id_sucursal;
        this.empleado_nombre = empleado_nombre;
        // pone el nombre del empleado
        lblEmpleadoIn.setText(this.empleado_nombre);
        // Consulta los datos
        daoCliente = new ClienteDAO();
        //daoTPagos = new DaoTPagos();
        // crea instancia de las ventanas
        //addPago = new AddPago(null, true);
        addLibrosPrestamo = new AddLibrosPrestamo(null, true);        
        // llena los datos
        llenaDatos();
    }
    
    private void llenaDatos(){
        llenaClientes();
       // llenaTablaPagos();
        llenaTablaLibros();
    }
    
    private void llenaTablaLibros(){    
        // obtiene fecha
        Calendar c = Calendar.getInstance();
        // Pone la fecha de renta en el control
        lblFechaOut.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH+1) + "/" + c.get(Calendar.YEAR));
        c.add(Calendar.DAY_OF_YEAR, 3);
        // Pone la fecha de entrega en el control
        lblFechaIn.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH+1) + "/" + c.get(Calendar.YEAR));
        // Configuración de la tabla donde se mostraran las peliculas
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT, javax.swing.SwingConstants.RIGHT}};
        int[][] cellSize = {{0,0},
                            {1,670}};
        String[] T_RENTA = {"","Título"};
        // llena la tabla 
        UtilsTable.llenaTabla(tableList,null, T_RENTA, cellAlignment, cellSize);
        // obtiene el modelo de la tabla (datos)
        modeloLibros = (DefaultTableModel) tableList.getModel();
    }    
    
    private void llenaClientes(){
        // Consulta datos de los clientes
        
        cliente = daoCliente.getCustomers();
        
        System.out.println(Arrays.toString(cliente));
        cmbCliente.addItem("Seleccione un cliente...");
        for (Object[] customer : cliente) {
            // agrega datos de los clientes al combo
            cmbCliente.addItem(customer[1] + " " + customer[2]);
        }
    }
    
    private void llenaTablaPagos(){
        // Configuración de la tabla donde se mostraran las peliculas
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT, javax.swing.SwingConstants.RIGHT}};
        int[][] cellSize = {{0,0},
                            {1,120},
                            {2,50}};
        String[] T_PAGOS = {"","Tipo de pago", "Monto"};
        // llena la tabla 
        //UtilsTable.llenaTabla(tableListPagos,null, T_PAGOS, cellAlignment, cellSize);
        // obtiene el modelo de la tabla (datos)
      // modeloPagos = (DefaultTableModel) tableListPagos.getModel();
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
            javax.swing.JOptionPane.showMessageDialog(this, "No hay libros en prestamo.","Aviso",2);
            return false;
        }/*
        else if (cmbTipoPago.getSelectedIndex() == 0){
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un tipo de pago.","Aviso",2);
            return false;
        }*/
        else
            return true;
    }
    
    private void showReporte(String id){
        // muestra el reporte y le manda el id de la renta
        //VentanaReporteVenta reporteVenta = new VentanaReporteVenta(id);
       // reporteVenta.setVisible(true);
        // Limpia campos
        limpia();
    }
    
    private void limpia(){
        cmbCliente.setSelectedIndex(0);
        Calendar c = Calendar.getInstance();
        lblFechaOut.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        c.add(Calendar.DAY_OF_YEAR, 3);
        lblFechaIn.setText(c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        //lblTotalIn.setText("");
        UtilsTable.limpiaTabla(tableList);
       // UtilsTable.limpiaTabla(tableListPagos);
        totalSum = 0;
    }
    
    private boolean verificaRepetido(String idFilm){
        for (int i = 0; i < tableList.getRowCount(); i++){
            if (tableList.getValueAt(i, 0).toString().equals(idFilm))
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRenta = new javax.swing.JPanel();
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
        btnAddPelicula = new javax.swing.JButton();
        btnBorrarPel = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Prestamo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prestamo_libro.png"))); // NOI18N
        setName(""); // NOI18N

        pnlRenta.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlRenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAdd.setToolTipText("Nuevo prestamo");
        btnAdd.setFocusable(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        pnlRenta.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 40, 40));

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardar prestamo");
        btnGuarda.setFocusable(false);
        btnGuarda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaActionPerformed(evt);
            }
        });
        pnlRenta.add(btnGuarda, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 40, 40));

        lblCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblCliente.setText("Cliente");
        pnlRenta.add(lblCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 20));

        lblFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFecha.setText("Fecha:");
        pnlRenta.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, -1, 20));

        lblEmpleado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEmpleado.setText("Empleado:");
        pnlRenta.add(lblEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        cmbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClienteActionPerformed(evt);
            }
        });
        pnlRenta.add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 280, -1));

        lblFechaOut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaOut.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlRenta.add(lblFechaOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 190, 20));

        lblEmpleadoIn.setAlignmentX(0.5F);
        lblEmpleadoIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlRenta.add(lblEmpleadoIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 280, 20));

        lblEntrega.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEntrega.setText("Entrega:");
        pnlRenta.add(lblEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, -1, 20));

        lblFechaIn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlRenta.add(lblFechaIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 190, 20));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle de prestamo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
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

        btnAddPelicula.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAddPelicula.setToolTipText("Agregar libro");
        btnAddPelicula.setFocusable(false);
        btnAddPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPeliculaActionPerformed(evt);
            }
        });
        pnlTableList.add(btnAddPelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 40, 40));
        btnAddPelicula.getAccessibleContext().setAccessibleDescription("Agregar libros");

        btnBorrarPel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnBorrarPel.setToolTipText("Borrar libro");
        btnBorrarPel.setFocusable(false);
        btnBorrarPel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarPelActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrarPel, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlRenta, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlRenta, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
//         Guarda y actualiza los datos
        if (estanLlenos()){
            //if (totalesIguales()){
            System.out.println("holajaco");
                // Guarda datos de los controles en un arreglo de objetos
                Object[] renta = new Object[5];
                renta[0] = id_sucursal;
                System.out.println(renta[0]);
                renta[1] = lblFechaOut.getText();
                renta[2] = cliente[cmbCliente.getSelectedIndex()-1][0].toString();
                renta[3] = lblFechaIn.getText();
                renta[4] = id_empleado;
                // Guarda la renta y recupera el id
                PrestamoDAO daoRenta = new PrestamoDAO();
                String id = daoRenta.SavePrestamo(renta);
                if (id != null){        
                    // si guardo la renta, guarda los datos del detalle
                    DPrestamoDAO daoDRenta = new DPrestamoDAO();
                    int i;
                    for (i = 0; i < tableList.getRowCount(); i++)
                        daoDRenta.saveDPrestamo(id,Integer.parseInt(tableList.getValueAt(i, 0).toString()));
                    
                    // si guardo la renta, guarda los datos del pago
                    /*DaoPayment daoPayment = new DaoPayment();
                    int j;
                    for (j = 0; j < tableListPagos.getRowCount(); j++)
                        daoPayment.savePayment(id,Integer.parseInt(tableListPagos.getValueAt(j, 0).toString()),
                                Float.parseFloat(tableListPagos.getValueAt(j, 2).toString()));*/
                    
                    if (i > 0 ){
                        javax.swing.JOptionPane.showMessageDialog(this, "El prestamo se guardó correctamente","Información",1);
                        showReporte(id);
                    }
                    else
                        javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar el detalle.","Información",1);
                }
                else
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar","Información",1);
            //}
        }
    }//GEN-LAST:event_btnGuardaActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Limpia para agragar
        limpia();
    }//GEN-LAST:event_btnAddActionPerformed

    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
     
    }//GEN-LAST:event_tableListMouseClicked

    private void tableListMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseExited
    }//GEN-LAST:event_tableListMouseExited

    private void tableListComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tableListComponentMoved
    }//GEN-LAST:event_tableListComponentMoved

    private void btnAddPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPeliculaActionPerformed
        // verifica si son menos de 3 peliculas
        //System.out.print("malaria");
         if (tableList.getRowCount()== 3){
             // Suena un beep
             Toolkit.getDefaultToolkit().beep();
             javax.swing.JOptionPane.showMessageDialog(this, "No se pueden agregar mas de 3\nlibros al prestamo.","Aviso",2);
         }
         else{
            // Abre la ventana para agregar peliculas
            // Localizacvión de la ventana
            addLibrosPrestamo.setLocationRelativeTo(this);
            // hace visible la ventana
            addLibrosPrestamo.setVisible(true);
            // cuando cierra la ventana agrega la pelicula seleccionada a la tabla de detalle de renta
            // si selecciono un registro, pone la pelicula
            if (addLibrosPrestamo.tableList.getSelectedRow() >= 0){
                // verifica si la pelicula ya está en la lista
                if (verificaRepetido(addLibrosPrestamo.film[0].toString())){
                    modeloLibros.addRow(addLibrosPrestamo.film);
                    // calcula el monto de la renta
                    //totalSum = totalSum + Float.parseFloat(addLibrosPrestamo.film[2].toString());
                    // pone el monto en el control
                    //sDecimalFormat df = new DecimalFormat("#.00");
                    //lblTotalIn.setText("" + df.format(totalSum));
                }
            }
         }
    }//GEN-LAST:event_btnAddPeliculaActionPerformed

    private void btnBorrarPelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarPelActionPerformed
        // Borrar peliucula del detalle
        // valida si selecciono la fila
         if (tableList.getSelectedRow() < 0){
             // Suena un beep
             Toolkit.getDefaultToolkit().beep();
             javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.","Aviso",2);
         }
        else{
              // calcula en nuevo monto
              //totalSum = totalSum - Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(), 2).toString());
              // muestra el nuevo monto
              //lblTotalIn.setText("$" + totalSum);
              //remueve el elemento del modelo de la tabla
              modeloLibros.removeRow(tableList.getSelectedRow());
         }
    }//GEN-LAST:event_btnBorrarPelActionPerformed

    private void cmbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbClienteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddPelicula;
    private javax.swing.JButton btnBorrarPel;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblEmpleadoIn;
    private javax.swing.JLabel lblEntrega;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaIn;
    private javax.swing.JLabel lblFechaOut;
    private javax.swing.JPanel pnlRenta;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JTable tableList;
    // End of variables declaration//GEN-END:variables
}
