package gui.libro;

import dataBase.dao.PaisDAO;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import tools.UtilsTable;

/**
 *
 * @author Alfonso
 * Ventana que permite agregar paises
 */
public class AddPais extends javax.swing.JDialog {
    // Para agregar el paises
    int pais_id;
    PaisDAO pais_dao;
    // Para listar todos los paises 
    Object lista_paises [][];  // Id, pais
   // public Object film[];
    java.awt.Frame parent;    

    /**
     * Creates new form ???
     * @param parent
     * @param modal
     */
    public AddPais(java.awt.Frame parent, boolean modal) {
        // ventana modal
        super(parent, modal);   // Llama al constructor del padre
        this.parent= parent;
        setTitle("Paises");
        // inicia los componentes
        initComponents();
        // Crea el dao
        pais_dao = new PaisDAO();
        // llena la tabla
        LlenaTabla();
    }
    
    // ? ? ? 
    public void SetPaisId(int id_pais){   
        // Asigna el id del libro
        this.pais_id = id_pais;
        txtFiltro.setText("");
        LlenaTabla();
    }
    
    // Llena y despliega la tabla de autores 
    private void LlenaTabla(){     
        // Consulta todos los autores (id, nombre apellido)
        lista_paises = pais_dao.GetPaisesByNombre(txtFiltro.getText().trim());
        // Titulos de la tabla
        String[] T_PAIS = {"","Pais"};
        // alineación de las celdas
        int[][] cellAlignment = {{0,javax.swing.SwingConstants.LEFT}};
        // Tamaño de las celdas
        int[][] cellSize = {{0,0},
                            {1,170}};
        /*
            Metodo que llena las tablas, recibe la tabla, los datos, los titulos,
            la alineación y el tamaño de las celdas
        */
       
        UtilsTable.llenaTabla(tableList,lista_paises, T_PAIS, cellAlignment, cellSize);
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
        tableList = new javax.swing.JTable();
        txtFiltro = new javax.swing.JTextField();
        lblFiltro = new javax.swing.JLabel();
        btnNewPais = new javax.swing.JButton();
        btnDelPais = new javax.swing.JButton();
        btnEditPais = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/query.png")));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
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

        btnNewPais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnNewPais.setToolTipText("Registrar nuevo idioma");
        btnNewPais.setFocusable(false);
        btnNewPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPaisActionPerformed(evt);
            }
        });

        btnDelPais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDelPais.setToolTipText("Borrar idioma");
        btnDelPais.setFocusable(false);
        btnDelPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelPaisActionPerformed(evt);
            }
        });

        btnEditPais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEditPais.setToolTipText("Editar idioma");
        btnEditPais.setFocusable(false);
        btnEditPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPaisActionPerformed(evt);
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
                        .addComponent(btnNewPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnEditPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(btnDelPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNewPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditPais, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Hacer doble click no hace nada
    private void tableListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListMouseClicked
       
    }//GEN-LAST:event_tableListMouseClicked

    // Abre una ventana que posibilida crear un PAIS
    private void btnNewPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPaisActionPerformed
        AddNewPais add_new_pais = new AddNewPais(parent, true);        
        // Localizacvión de la ventana        
        add_new_pais.setLocationRelativeTo(this);
        // hace visible la ventana
        add_new_pais.setVisible(true);        
        LlenaTabla();
        // cuando cierra la ventana agrega el pais a la tabla y lo selecciona
        UtilsTable.mueveTabla(tableList, UtilsTable.getRow(lista_paises, add_new_pais.pais_id));
    }//GEN-LAST:event_btnNewPaisActionPerformed

    // Llena la tabla de paises cada que se escribe una letra
    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
        // Filtra paises  
        LlenaTabla();
    }//GEN-LAST:event_txtFiltroKeyReleased

    // Elimina al pais seleccionado de la tabla de PAIS
    private void btnDelPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelPaisActionPerformed
        // Eliminar un registro
        // si no selecciona fila, le avisa al usuario
        if (tableList.getSelectedRow() < 0){
             // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila","Aviso", 2);
        }
        else{
            // suena un beep
            java.awt.Toolkit.getDefaultToolkit().beep();
            // pregunta si quiere eliminar el registro y camtura la respuesta
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + lista_paises[tableList.getSelectedRow()][1].toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            // evalua la respuesta 
            if (res == 0){
                String msj = "";
                // si la respuesta es afirmativa, elimina el registro
                int ret = pais_dao.DeletePais((Integer) tableList.getValueAt(tableList.getSelectedRow(), 0));
                if (ret == 1){
                    msj = "No se pudo eliminar por que tiene registros asignados.";
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                }                
                // Reinicia controles y parametros
                LlenaTabla();
            }
        }
    }//GEN-LAST:event_btnDelPaisActionPerformed

    // Abre una ventana para poder editar al pais seleccionado
    private void btnEditPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPaisActionPerformed
        // Botón que edita el registro selecionado de la tabla
        if (tableList.getSelectedRow() < 0){
            // Suena un beep
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Información", 1);
        }
        else{
            // Obtiene el id del pais seleccionado
            pais_id = (Integer) tableList.getValueAt(tableList.getSelectedRow(), 0);
            // Abre la ventana para editar pais
            // Ventana para editar pais
            AddNewPais edit_pais = new AddNewPais(parent, true);        
            // Localización de la ventana
            edit_pais.setLocationRelativeTo(this);            
            edit_pais.SetEditId(pais_id);
            // hace visible la ventana
            edit_pais.setVisible(true);
            LlenaTabla();
            // cuando cierra la ventana agrega el pais a la tabla y lo selecciona
            UtilsTable.mueveTabla(tableList, UtilsTable.getRow(lista_paises, edit_pais.pais_id));
        }
    }//GEN-LAST:event_btnEditPaisActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelPais;
    private javax.swing.JButton btnEditPais;
    private javax.swing.JButton btnNewPais;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JTable tableList;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}