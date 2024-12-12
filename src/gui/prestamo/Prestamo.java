package gui.prestamo;

import dataBase.dao.ClienteDAO;
import dataBase.dao.DetallePrestamoDAO;
import dataBase.dao.PrestamoDAO;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import tools.UtilsTable;

/**
 * Ventana que registra un prestamo de libros.
 * @author alfonso
 */
public class Prestamo extends javax.swing.JInternalFrame {
    int clienteId;
    String empleadoId;
    String clienteNombre;
    String empleadoNombre;
    Object[][] prestamos;
    
    // Para consultar los datos de la DB.
    ClienteDAO clienteDAO;    
    PrestamoDAO prestamoDAO;
    AddLibrosPrestamo addLibrosPrestamo;
    SelectCliente selectCliente;

    /**
     * Creates new form Prestamo
     * @param empleadoId
     * @param sucursalId
     * @param empleadoNombre
     */
    public Prestamo(String empleadoId, String sucursalId, String empleadoNombre) {
        initComponents();
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
        lblEmpleadoIn.setText(this.empleadoNombre);
        
        DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");                
        String today = date_format.format(LocalDateTime.now());
        lblFechaPrestamo.setText(today);
        
        clienteDAO = new ClienteDAO();
        prestamoDAO = new PrestamoDAO();
        addLibrosPrestamo = new AddLibrosPrestamo(null, true);        
        selectCliente = new SelectCliente(null, true);
        llenaDatos();
    }
    
    /**
     * Llena las tablas de Libros y Prestamos.
     */
    private void llenaDatos() {
        llenaTablaLibros();
        llenaTablaPrestamos();
    }
    
    /**
     * Llena la tabla de Libros en el Prestamo.
     */
    private void llenaTablaLibros() {            
        int[][] cellAlignment = {{0, javax.swing.SwingConstants.LEFT, javax.swing.SwingConstants.RIGHT}};
        int[][] cellSize = {{0, 0}, {1, 700}};
        String[] columnasNombre = {"", "Título"};
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblLibrosPrestamo, null);
        UtilsTable.quitarColumna(0, tblLibrosPrestamo);        
    }
    
    /**
     * Llena la tabla de Prestamos.
     */
    private void llenaTablaPrestamos() {
        prestamos = prestamoDAO.getPrestamosByCliente(clienteId);
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT, javax.swing.SwingConstants.RIGHT}};
        int[][] cellSize = {{0, 460}, {1, 120}, {2, 120}};
        String[] columnasNombre = {"Titulo", "Fecha Prestamo", "Fecha Vencimiento"};
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblPrestamos, prestamos);              
    }
    
    /**
     * Valida si se llenaron los datos.
     * @return boolean con la evaluacion de si todos los campos estan llenos.
     */
    private boolean estanLlenos() {
        boolean llenos = true;
        if(lblNombreCliente.getText().equals("Seleccione un cliente")) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.", "Aviso", 2);            
            llenos = false;
        } else if(tblLibrosPrestamo.getRowCount() == 0) {
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "No hay libros en prestamo.", "Aviso", 2);
            llenos = false;
        }                
        return llenos;
    }       
    
    /**
     * Elimina la informacion introducida en los campos.
     */
    private void cancelaEdit() {
        lblNombreCliente.setText("Seleccione un cliente");     
        lblFechaEntrega.setText("");
        UtilsTable.limpiaTabla(tblLibrosPrestamo);
        UtilsTable.limpiaTabla(tblPrestamos);
    }
    
    /**
     * Verifica si el Libro que se intenta agregar ya existe en el Prestamo.
     * @param libroId
     * @return boolean con el resultado de la verificacion.
     */
    private boolean VerificaRepetido(int libroId) {
        boolean repetido = true;
        for(int i = 0; i < tblLibrosPrestamo.getRowCount(); i++) {            
            if((int) UtilsTable.obtenerValor(i, 0, tblLibrosPrestamo) == libroId) {
                repetido = false;
                break;
            }               
        }
        return repetido;
    }
           
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrestamo = new javax.swing.JPanel();
        btnGuarda = new javax.swing.JButton();
        lblCliente = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        lblFechaPrestamo = new javax.swing.JLabel();
        lblEmpleadoIn = new javax.swing.JLabel();
        lblEntrega = new javax.swing.JLabel();
        lblFechaEntrega = new javax.swing.JLabel();
        lblNombreCliente = new javax.swing.JLabel();
        btnCancela = new javax.swing.JButton();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tblLibrosPrestamo = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnlTableListPrestamos = new javax.swing.JPanel();
        scpTableList1 = new javax.swing.JScrollPane();
        tblPrestamos = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Prestamo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prestamo_libro.png"))); // NOI18N
        setName(""); // NOI18N

        pnlPrestamo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlPrestamo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardar prestamo");
        btnGuarda.setFocusable(false);
        btnGuarda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaActionPerformed(evt);
            }
        });
        pnlPrestamo.add(btnGuarda, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 40, 40));

        lblCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblCliente.setText("Cliente");
        pnlPrestamo.add(lblCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 20));

        lblFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFecha.setText("Fecha:");
        pnlPrestamo.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, 20));

        lblEmpleado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEmpleado.setText("Empleado:");
        pnlPrestamo.add(lblEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        lblFechaPrestamo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaPrestamo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblFechaPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 190, 20));

        lblEmpleadoIn.setAlignmentX(0.5F);
        lblEmpleadoIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblEmpleadoIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 280, 20));

        lblEntrega.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblEntrega.setText("Entrega:");
        pnlPrestamo.add(lblEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, -1, 20));

        lblFechaEntrega.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaEntrega.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlPrestamo.add(lblFechaEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 110, 190, 20));

        lblNombreCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreCliente.setText("Seleccione un cliente");
        lblNombreCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblNombreCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNombreClienteMouseClicked(evt);
            }
        });
        pnlPrestamo.add(lblNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 280, 20));

        btnCancela.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelar.png"))); // NOI18N
        btnCancela.setToolTipText("Cancela prestamo");
        btnCancela.setFocusable(false);
        btnCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelaActionPerformed(evt);
            }
        });
        pnlPrestamo.add(btnCancela, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 40, 40));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle de prestamo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblLibrosPrestamo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblLibrosPrestamo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList.setViewportView(tblLibrosPrestamo);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 710, 120));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnAdd.setToolTipText("Agregar libro");
        btnAdd.setFocusable(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        pnlTableList.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 40, 40));
        btnAdd.getAccessibleContext().setAccessibleDescription("Agregar libros");

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDelete.setToolTipText("Borrar libro");
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnlTableList.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 40, 40));

        pnlTableListPrestamos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamos del cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableListPrestamos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblPrestamos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList1.setViewportView(tblPrestamos);

        pnlTableListPrestamos.add(scpTableList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 710, 210));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pnlPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlTableListPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableListPrestamos, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Guarda el Prestamo.
     * @param evt evento que dispara la funcion.
     */ 
    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
        if(estanLlenos()) {
            
            // Guarda datos de los controles en un arreglo de objetos.
            Object[] prestamo = new Object[4];
            prestamo[0] = lblFechaPrestamo.getText();
            prestamo[1] = lblFechaEntrega.getText();
            prestamo[2] = clienteId;
            prestamo[3] = empleadoId;
            
            // Guarda la prestamo y recupera el id.
            int prestamoId = prestamoDAO.savePrestamo(prestamo);
            if(prestamoId != 0) {        
                DetallePrestamoDAO detallesPrestamoDAO = new DetallePrestamoDAO();
                int i;
                for(i = 0; i < tblLibrosPrestamo.getRowCount(); i++) {
                    int result = detallesPrestamoDAO.saveDetallePrestamo(prestamoId, (int) UtilsTable.obtenerValor(i, 0, tblLibrosPrestamo));
                    if(result == 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar el detalle.", "Información", 1);
                        prestamoDAO.deletePrestamo(prestamoId);
                        break;
                    }
                }       
                if(i == tblLibrosPrestamo.getRowCount()) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El prestamo se guardó correctamente", "Información", 1);
                    cancelaEdit();
                }                
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar", "Información", 1);
            }                    
        }
    }//GEN-LAST:event_btnGuardaActionPerformed

    /**
     * Agrega un Libro al Prestamo actual.
     * @param evt evento que dispara la funcion.
     */ 
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if(tblLibrosPrestamo.getRowCount()== 3) {
            
            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "No se pueden agregar mas de 3\nlibros al prestamo.", "Aviso", 2);
        } else {
            
            // Abre la ventana para agregar libros
            addLibrosPrestamo.setLocationRelativeTo(this);
            addLibrosPrestamo.setVisible(true);
            
            // Cuando cierra la ventana agrega el Libro seleccionado a la tabla de Detalle de Prestamo:
            if(addLibrosPrestamo.tblLibros.getSelectedRow() >= 0) {
                if(VerificaRepetido((int) addLibrosPrestamo.libro[0])) {
                    UtilsTable.agregarFila(tblLibrosPrestamo, addLibrosPrestamo.libro);                    
                    DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");                
                    String fecha_entrega = date_format.format(LocalDateTime.now().plusDays(3 * tblLibrosPrestamo.getRowCount()));
                    lblFechaEntrega.setText(fecha_entrega);
                }
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    /**
     * Elimina un Libro del Prestamo actual.
     * @param evt evento que dispara la funcion.
     */ 
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(tblLibrosPrestamo.getSelectedRow() < 0) {

            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila.","Aviso",2);
        } else {
            UtilsTable.quitarFila(tblLibrosPrestamo.getSelectedRow(), tblLibrosPrestamo);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * Borra todos los datos de los controladores.
     * @param evt evento que dispara la funcion.
     */ 
    private void btnCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelaActionPerformed
        cancelaEdit();
    }//GEN-LAST:event_btnCancelaActionPerformed

    /**
     * Despliega una ventana para seleccionar el cliente.
     * @param evt evento que dispara la funcion.
     */ 
    private void lblNombreClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNombreClienteMouseClicked
        if(evt.getClickCount() == 2) {  
            selectCliente.setLocationRelativeTo(this);
            selectCliente.setVisible(true);
            
            // Cuando cierra la ventana agrega el Libro seleccionado a la tabla de Detalle de Prestamo.
            clienteId = selectCliente.clienteId;
            llenaTablaPrestamos();
            Object[] cliente = clienteDAO.getClienteById(clienteId);
            clienteNombre = cliente[1] + " " + cliente[2] + " " + cliente[3];
            lblNombreCliente.setText(clienteNombre);
        }
    }//GEN-LAST:event_lblNombreClienteMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancela;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblEmpleadoIn;
    private javax.swing.JLabel lblEntrega;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFechaEntrega;
    private javax.swing.JLabel lblFechaPrestamo;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JPanel pnlPrestamo;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableListPrestamos;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JScrollPane scpTableList1;
    private javax.swing.JTable tblLibrosPrestamo;
    private javax.swing.JTable tblPrestamos;
    // End of variables declaration//GEN-END:variables
}
