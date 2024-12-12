package gui.libro;

import dataBase.dao.PaisDAO;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import tools.UtilsTable;

/**
 * Ventana que permite agregar Paises.
 * @author alfonso
 */
public class AddPais extends javax.swing.JDialog {
    int paisId;
    PaisDAO paisDAO;
    Object[][] paisesLista;
    java.awt.Frame parent;    

    /**
     * Creates new form AddPais.
     * @param parent ventana padre.
     * @param modal determina si la ventana no cede el foco a otra.
     */
    public AddPais(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent= parent;
        setTitle("Paises");
        
        // Inicia los componentes.
        initComponents();
        paisDAO = new PaisDAO();
        llenaTabla();
    }
    
    /**
     * Establece el id del pais siendo editado.
     * @param paisId Id del Pais a editar. 
     */
    public void setPaisId(int paisId) {   
        this.paisId = paisId;
        txtFiltro.setText("");
        llenaTabla();
    }
    
    /**
     * Llena y despliega la tabla de Paises.
     */
    private void llenaTabla() {     
        paisesLista = paisDAO.getPaisesByNombre(txtFiltro.getText().trim());
        
        // Titulos de la tabla.
        String[] columnasNombre = {"", "Pais"};
       
        // Alineación de las celdas.
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        
        // Tamaño de las celdas.
        int[][] cellSize = {{0, 0}, {1, 170}};   
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblPaises, paisesLista);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTableList = new javax.swing.JPanel();
        scpTableList = new javax.swing.JScrollPane();
        tblPaises = new javax.swing.JTable();
        txtFiltro = new javax.swing.JTextField();
        lblFiltro = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/libros.png")));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblPaises.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblPaises.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scpTableList.setViewportView(tblPaises);

        pnlTableList.add(scpTableList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 180, 170));

        txtFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });
        pnlTableList.add(txtFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 140, 25));

        lblFiltro.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFiltro.setText("Filtrar:");
        pnlTableList.add(lblFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 25));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnNew.setToolTipText("Registrar nuevo idioma");
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDelete.setToolTipText("Borrar idioma");
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEdit.setToolTipText("Editar idioma");
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Abre una ventana que posibilida crear un Pais.
     * @param evt evento que dispara la funcion.
     */
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        AddNewPais addNewPais = new AddNewPais(parent, true);               
        addNewPais.setLocationRelativeTo(this);
        addNewPais.setVisible(true);        
        
        // Cuando cierra la ventana agrega el pais a la tabla y lo selecciona.
        llenaTabla();
        UtilsTable.mueveTabla(UtilsTable.getRow(addNewPais.paisId, paisesLista), tblPaises);
    }//GEN-LAST:event_btnNewActionPerformed

    /**
     * Llena la tabla de Paises cada que se escribe una letra.
     * @param evt evento que dispara la funcion.
     */
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        llenaTabla();
    }//GEN-LAST:event_txtFiltroKeyReleased
    
    /**
     * Elimina al Pais seleccionado de la tabla de Pais.
     * @param evt evento que dispara la funcion.
     */    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        // Si no selecciona fila, le avisa al usuario.
        if(tblPaises.getSelectedRow() < 0) {
             
            // Suena un beep y se muestra un mensaje.
            java.awt.Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Aviso", 2);
        } else {
            
            // Suena un beep y se muestra un mensaje de confirmacion.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + paisesLista[tblPaises.getSelectedRow()][1].toString() + "?",
                "Seleccione", JOptionPane.YES_NO_OPTION);
            
            // Si la respuesta es afirmativa, elimina el registro.
            if(res == 0) {
                String msj = "";                
                int ret = paisDAO.deletePais((Integer) tblPaises.getValueAt(tblPaises.getSelectedRow(), 0));
                if(ret != 1) {
                    msj = "No se pudo eliminar por que tiene registros asignados.";
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                }               
                
                // Reinicia controles y parametros.
                llenaTabla();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * Abre una ventana para poder editar al Pais seleccionado.
     * @param evt evento que dispara la funcion.
     */    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(tblPaises.getSelectedRow() < 0) {
            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Información", 1);
        } else {
            
            // Abre la ventana para editar pais.
            paisId = (int) tblPaises.getValueAt(tblPaises.getSelectedRow(), 0);           
            AddNewPais editPais = new AddNewPais(parent, true);        
            editPais.setLocationRelativeTo(this);            
            editPais.SetEditId(paisId);
            editPais.setVisible(true);
            
            // Cuando cierra la ventana agrega el pais a la tabla y lo selecciona.
            llenaTabla();
            UtilsTable.mueveTabla(UtilsTable.getRow(editPais.paisId, paisesLista), tblPaises);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JTable tblPaises;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}