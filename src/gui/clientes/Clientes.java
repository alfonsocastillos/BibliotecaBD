package gui.clientes;
import dataBase.dao.ClienteDAO;
import java.util.List;
import dataBase.dao.EscolaridadDAO;
import dataBase.dao.CredencialDAO;

/**
 *
 * @author Jordi
 * Ventana que registra clientes
 */
public class Clientes extends javax.swing.JInternalFrame {
    boolean editando = false;
    int cliente_id;
    // Dao´s que traen los datos de la DB
    CredencialDAO credencial_dao;
    EscolaridadDAO escolaridad_dao;
    
    
    // Ventanas para agregar clientes, credenciales, etc.
    AddCredenciales add_credencial;               // Para crear, editar y eliminar paises
    AddEscolaridad add_escolaridad;
    
    public Clientes() {
        // Constructor
        initComponents();
        
        // Un 0 significa error en el id
        cliente_id = 0;
        
        // se instancian todos los DAO
        credencial_dao = new CredencialDAO();
        escolaridad_dao = new EscolaridadDAO();
        
        
        // Instanciando ventanas para agregar Credenciales, clientes, etc.
        add_credencial = new AddCredenciales((java.awt.Frame)this.getParent(), true);
        add_escolaridad = new AddEscolaridad((java.awt.Frame)this.getParent(), true);
        
        // localización de la ventana
        setLocale(null);
        
    }
    
    
    
    
    
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlClientes = new javax.swing.JPanel();
        btnGuarda = new javax.swing.JButton();
        lblCliente = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        lblTotal2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TextFieldNombre = new javax.swing.JTextField();
        TextFieldApellidoPat = new javax.swing.JTextField();
        TextFieldApellidoMat = new javax.swing.JTextField();
        TextFieldCorreo = new javax.swing.JTextField();
        TextFieldAlcaldia = new javax.swing.JTextField();
        TextFieldCP = new javax.swing.JTextField();
        TextFieldCalle = new javax.swing.JTextField();
        TextFieldNoExterior = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TextFieldNoInterior = new javax.swing.JTextField();
        cmbEscolaridad = new javax.swing.JComboBox<>();
        pnlTableListCredenciales = new javax.swing.JPanel();
        EditCredencial = new javax.swing.JButton();
        pnlTableList4 = new javax.swing.JPanel();
        EditEscolaridad = new javax.swing.JButton();
        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tableList = new javax.swing.JTable();
        lblTitulo1 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnBorrar = new javax.swing.JButton();
        btnBorrarFiltro = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Clientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user_icon.png"))); // NOI18N
        setName(""); // NOI18N

        pnlClientes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agregar Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlClientes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuarda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuarda.setToolTipText("Guardar Cliente");
        btnGuarda.setFocusable(false);
        btnGuarda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaActionPerformed(evt);
            }
        });
        pnlClientes.add(btnGuarda, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 60, 60));

        lblCliente.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblCliente.setText("Nombre:");
        pnlClientes.add(lblCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, 20));

        lblFecha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFecha.setText("Apellido Paterno:");
        pnlClientes.add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 20));

        lblTotal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal.setText("Alcaldia:");
        pnlClientes.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, 20));

        lblTotal1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal1.setText("Apellido Materno:");
        pnlClientes.add(lblTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 20));

        lblTotal2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTotal2.setText("Correo:");
        pnlClientes.add(lblTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jLabel1.setText("CP:");
        pnlClientes.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jLabel2.setText("Calle:");
        pnlClientes.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, -1));

        jLabel3.setText("No. Exterior:");
        pnlClientes.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        jLabel4.setText("Escolaridad:");
        pnlClientes.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, -1, -1));

        TextFieldNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 250, 20));

        TextFieldApellidoPat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldApellidoPat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 250, 20));

        TextFieldApellidoMat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldApellidoMat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 250, 20));

        TextFieldCorreo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 250, 20));

        TextFieldAlcaldia.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TextFieldAlcaldia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldAlcaldiaActionPerformed(evt);
            }
        });
        pnlClientes.add(TextFieldAlcaldia, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 250, 20));

        TextFieldCP.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 250, 20));

        TextFieldCalle.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldCalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 250, 20));

        TextFieldNoExterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNoExterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 250, 20));

        jLabel5.setText("No. Interior:");
        pnlClientes.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, -1));

        TextFieldNoInterior.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlClientes.add(TextFieldNoInterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 250, 20));

        cmbEscolaridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEscolaridadActionPerformed(evt);
            }
        });
        pnlClientes.add(cmbEscolaridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 250, -1));

        pnlTableListCredenciales.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Credenciales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableListCredenciales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditCredencial.setText("Editar Credenciales");
        EditCredencial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditCredencialActionPerformed(evt);
            }
        });
        pnlTableListCredenciales.add(EditCredencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlClientes.add(pnlTableListCredenciales, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 190, 60));

        pnlTableList4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escolaridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EditEscolaridad.setText("Editar países");
        EditEscolaridad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditEscolaridadActionPerformed(evt);
            }
        });
        pnlTableList4.add(EditEscolaridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 140, -1));

        pnlClientes.add(pnlTableList4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 190, 190, 60));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
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
        });
        scpTableList.setViewportView(tableList);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 810, 240));

        lblTitulo1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblTitulo1.setText("Filtrar:");
        pnlTableList.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 25));

        txtFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });
        pnlTableList.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 230, 25));

        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnBorrar.setToolTipText("Borrar libro");
        btnBorrar.setFocusable(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 20, 40, 40));

        btnBorrarFiltro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/cancelarm.png"))); // NOI18N
        btnBorrarFiltro.setToolTipText("Borrar filtro");
        btnBorrarFiltro.setFocusable(false);
        btnBorrarFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarFiltroActionPerformed(evt);
            }
        });
        pnlTableList.add(btnBorrarFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 28, 30, 30));

        jLabel6.setText("Numero de clientes:");
        pnlTableList.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, 30));

        lblCantidad.setText("000");
        pnlTableList.add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, -1, 30));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEdit.setToolTipText("Editar");
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        pnlTableList.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 20, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlClientes.getAccessibleContext().setAccessibleName("Clientes");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaActionPerformed
    // Obtener datos de los campos de texto
    String nombre = TextFieldNombre.getText();
    String apellidoPat = TextFieldApellidoPat.getText();
    String apellidoMat = TextFieldApellidoMat.getText();
    String correo = TextFieldCorreo.getText();
    String alcaldia = TextFieldAlcaldia.getText();
    String cp = TextFieldCP.getText();
    String calle = TextFieldCalle.getText();
    String noExterior = TextFieldNoExterior.getText();
    String noInterior = TextFieldNoInterior.getText();
    
    // Obtener la escolaridad seleccionada en el ComboBox
    String escolaridadSeleccionada = cmbEscolaridad.getSelectedItem().toString();

    // Crear un objeto para representar la información del nuevo cliente
    Object[] nuevoCliente = {nombre, apellidoPat, apellidoMat, correo, alcaldia, cp, calle, noExterior, noInterior, escolaridadSeleccionada};

    // Insertar en la base de datos y obtener el nuevo ID del cliente
    ClienteDAO clienteDAO = new ClienteDAO();
    clienteDAO.saveCliente(nuevoCliente);

    // Imprimir el ID del nuevo cliente (puedes hacer algo más con este valor)
    System.out.println("Nuevo cliente insertado satisfactoriamente.");
    }//GEN-LAST:event_btnGuardaActionPerformed

    private void TextFieldAlcaldiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldAlcaldiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldAlcaldiaActionPerformed

    private void cmbEscolaridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEscolaridadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEscolaridadActionPerformed

    private void EditCredencialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditCredencialActionPerformed
        // Hace visible la ventana para agregar paises
        add_credencial.setLocationRelativeTo(this);
        add_credencial.setVisible(true);
    }//GEN-LAST:event_EditCredencialActionPerformed

    private void EditEscolaridadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditEscolaridadActionPerformed
        // Hace visible la ventana para agregar paises
        add_escolaridad.setLocationRelativeTo(this);
        add_escolaridad.setVisible(true);
    }//GEN-LAST:event_EditEscolaridadActionPerformed

    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
        // Muestra un ballon si es necesarios
        if (evt.getClickCount() == 2)
        btnEditActionPerformed(null);
    }//GEN-LAST:event_tableListMouseClicked

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased

    }//GEN-LAST:event_txtFiltroKeyReleased

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed

    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnBorrarFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarFiltroActionPerformed
        txtFiltro.setText("");
    }//GEN-LAST:event_btnBorrarFiltroActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    
    }//GEN-LAST:event_btnEditActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditCredencial;
    private javax.swing.JButton EditEscolaridad;
    private javax.swing.JTextField TextFieldAlcaldia;
    private javax.swing.JTextField TextFieldApellidoMat;
    private javax.swing.JTextField TextFieldApellidoPat;
    private javax.swing.JTextField TextFieldCP;
    private javax.swing.JTextField TextFieldCalle;
    private javax.swing.JTextField TextFieldCorreo;
    private javax.swing.JTextField TextFieldNoExterior;
    private javax.swing.JTextField TextFieldNoInterior;
    private javax.swing.JTextField TextFieldNombre;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBorrarFiltro;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnGuarda;
    private javax.swing.JComboBox<String> cmbEscolaridad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JLabel lblTotal2;
    private javax.swing.JPanel pnlClientes;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList4;
    private javax.swing.JPanel pnlTableListCredenciales;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JTable tableList;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables

    private void btnEditActionPerformed(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
