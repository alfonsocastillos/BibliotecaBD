package gui.clientes;

import dataBase.dao.PaisDAO;
import dataBase.dao.EstadoDAO;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import java.awt.Component;
import tools.UtilsTable;

// TODO: arreglar cuando se crea un Pais e inmediatamente se crea un Estado, no es posible

/**
 * Ventana encargada de mostar las opciones sobre Pais y Estado.
 * @author alfonso
 */
public class AddPais extends javax.swing.JDialog {
    int paisId;
    int estadoId;
    PaisDAO paisDAO;
    
    // Para listar todos los Paises y Estados.
    Object[][] paisesLista; 
    Object[][] estadosLista;
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
        enableEstados(false);
       
        paisDAO = new PaisDAO();        
        llenaPaises();
    }  
    
    /**
     * Llena y despliega la tabla de Paises.
     */
    private void llenaPaises() {     
        paisesLista = paisDAO.getPaisesByNombre(txtPais.getText().trim());
       
        // Titulos de la tabla.
        String[] columnasNombre = {"","Pais"};
       
        // Alineación de las celdas.
        int[][] cellAlignment = {{0, javax.swing.SwingConstants.LEFT}};
       
        // Tamaño de las celdas.
        int[][] cellSize = {{0, 0}, {1, 400}};
       
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblPaises, paisesLista);
        UtilsTable.quitarColumna(0, tblPaises);
    }
    
    /**
     * Llena y despliega la tabla de Estados para el Pais seleccionado.
     */
    private void llenaEstados() {
        EstadoDAO estadoDAO = new EstadoDAO();
        estadosLista = estadoDAO.getEstadosByNombre(paisId, txtEstado.getText().trim());
        UtilsTable.limpiaTabla(tblEstados);                        
        String[] columnasNombre = {"", "Estado"};
       
        // Alineación de las celdas.
        int[][] cellAlignment = {{0, javax.swing.SwingConstants.LEFT}};
       
        // Tamaño de las celdas.
        int[][] cellSize = {{0, 0}, {1, 400}};
       
        UtilsTable.llenaTabla(cellAlignment, cellSize, columnasNombre, tblEstados, estadosLista);    
        UtilsTable.quitarColumna(0, tblEstados);
    }
    
    private void enableEstados(boolean enable) {
        Component[] componentes = pnlEstados.getComponents();
        for(Component componente : componentes) {
            componente.setEnabled(enable);
        }
    }          
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlTableList = new javax.swing.JPanel();
        txtPais = new javax.swing.JTextField();
        lblFiltroPais = new javax.swing.JLabel();
        lblPais = new javax.swing.JLabel();
        scpTableList1 = new javax.swing.JScrollPane();
        tblPaises = new javax.swing.JTable();
        btnNewPais = new javax.swing.JButton();
        btnEditPais = new javax.swing.JButton();
        btnDeletePais = new javax.swing.JButton();
        pnlEstados = new javax.swing.JPanel();
        lblEstados = new javax.swing.JLabel();
        lblFiltroEstados = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        scpTableList = new javax.swing.JScrollPane();
        tblEstados = new javax.swing.JTable();
        btnNewEstado = new javax.swing.JButton();
        btnEditEstado = new javax.swing.JButton();
        btnDeleteEstado = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/libros.png")));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPais.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtPais.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPaisKeyReleased(evt);
            }
        });
        pnlTableList.add(txtPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 340, 25));

        lblFiltroPais.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFiltroPais.setText("Filtrar:");
        pnlTableList.add(lblFiltroPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 25));

        lblPais.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPais.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPais.setText("Paises");
        pnlTableList.add(lblPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 110, 25));

        scpTableList1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblPaises.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblPaises.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblPaises.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPaisesMouseClicked(evt);
            }
        });
        scpTableList1.setViewportView(tblPaises);

        pnlTableList.add(scpTableList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 390, 170));

        btnNewPais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnNewPais.setToolTipText("Registrar nuevo idioma");
        btnNewPais.setFocusable(false);
        btnNewPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPaisActionPerformed(evt);
            }
        });
        pnlTableList.add(btnNewPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 40, 40));

        btnEditPais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEditPais.setToolTipText("Editar idioma");
        btnEditPais.setFocusable(false);
        btnEditPais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditPaisActionPerformed(evt);
            }
        });
        pnlTableList.add(btnEditPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 40, 40));

        btnDeletePais.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDeletePais.setToolTipText("Borrar idioma");
        btnDeletePais.setFocusable(false);
        btnDeletePais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePaisActionPerformed(evt);
            }
        });
        pnlTableList.add(btnDeletePais, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 40, 40));

        lblEstados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEstados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstados.setText("Estados");

        lblFiltroEstados.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFiltroEstados.setText("Filtrar:");

        txtEstado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEstadoKeyReleased(evt);
            }
        });

        scpTableList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableList.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblEstados.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblEstados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblEstados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstadosMouseClicked(evt);
            }
        });
        scpTableList.setViewportView(tblEstados);

        btnNewEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/add.png"))); // NOI18N
        btnNewEstado.setToolTipText("Registrar nuevo idioma");
        btnNewEstado.setFocusable(false);
        btnNewEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewEstadoActionPerformed(evt);
            }
        });

        btnEditEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/editar.png"))); // NOI18N
        btnEditEstado.setToolTipText("Editar idioma");
        btnEditEstado.setFocusable(false);
        btnEditEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEstadoActionPerformed(evt);
            }
        });

        btnDeleteEstado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/borrar.png"))); // NOI18N
        btnDeleteEstado.setToolTipText("Borrar idioma");
        btnDeleteEstado.setFocusable(false);
        btnDeleteEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEstadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEstadosLayout = new javax.swing.GroupLayout(pnlEstados);
        pnlEstados.setLayout(pnlEstadosLayout);
        pnlEstadosLayout.setHorizontalGroup(
            pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEstadosLayout.createSequentialGroup()
                        .addGroup(pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEstadosLayout.createSequentialGroup()
                                .addComponent(lblFiltroEstados)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scpTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlEstadosLayout.createSequentialGroup()
                        .addComponent(btnNewEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138)
                        .addComponent(btnDeleteEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(pnlEstadosLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(lblEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlEstadosLayout.setVerticalGroup(
            pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFiltroEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeleteEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTableList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Abre una ventana que posibilida crear un Pais.
     * @param evt evento que dispara la funcion.
     */
    private void btnNewPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPaisActionPerformed
        AddNewPais addNewPais = new AddNewPais(parent, true);        
       
        // Aspectos graficos de la ventana.
        addNewPais.setLocationRelativeTo(this);
        addNewPais.setVisible(true);        
        
        // Cuando cierra la ventana agrega el pais a la tabla y lo selecciona.
        llenaPaises();
        paisId = (int) addNewPais.paisId;
        llenaEstados();
        enableEstados(true);
        UtilsTable.mueveTabla(paisId, tblPaises);
    }//GEN-LAST:event_btnNewPaisActionPerformed

    /**
     * Llena la tabla de paises cada que se escribe una letra.
     * @param evt evento que dispara la funcion.
     */
    private void txtPaisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaisKeyReleased
        llenaPaises();
    }//GEN-LAST:event_txtPaisKeyReleased

    /**
     * Elimina al pais seleccionado de la tabla de Pais.
     * @param evt evento que dispara la funcion.
     */
    private void btnDeletePaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePaisActionPerformed
        if(tblPaises.getSelectedRow() < 0) {
          
            // Suena un beep y se muestra un mensaje.
            java.awt.Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Aviso", 2);
        } else {
            
            // Suena un beep y se muestra un mensaje de confirmacion.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + UtilsTable.obtenerValor(tblPaises.getSelectedRow(), 1, tblPaises).toString() + "?",
                "Seleccione", JOptionPane.YES_NO_OPTION);

            // Si la respuesta es afirmativa, elimina el registro.
            if(res == 0) {
                String msj = "";
                int ret = paisDAO.deletePais((Integer) UtilsTable.obtenerValor(tblPaises.getSelectedRow(), 0, tblPaises));
                if(ret != 1) {
                    msj = "No se pudo eliminar por que tiene registros asignados.";
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                } else {
                    // Reinicia controles y parametros.
                    paisId = 0;
                    estadoId = 0;
                    llenaPaises();
                    llenaEstados();
                    enableEstados(false);
                }                                
            }
        }
    }//GEN-LAST:event_btnDeletePaisActionPerformed

    /**
     * Abre una ventana para poder editar al pais seleccionado.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditPaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditPaisActionPerformed
        if(tblPaises.getSelectedRow() < 0) {

            // Suena un beep y se muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Información", 1);
        } else {            
            paisId = (int) UtilsTable.obtenerValor(tblPaises.getSelectedRow(), 0, tblPaises);
            enableEstados(true);
           
            // Abre la ventana para editar pais
            AddNewPais editPais = new AddNewPais(parent, true);                   
            editPais.setLocationRelativeTo(this);            
            editPais.SetEditId(paisId);
            editPais.setVisible(true);
            
            // Cuando cierra la ventana agrega el Pais a la tabla y lo selecciona.
            llenaPaises();
            llenaEstados();
            UtilsTable.mueveTabla(UtilsTable.getRow(editPais.paisId, paisesLista), tblPaises);
        }
    }//GEN-LAST:event_btnEditPaisActionPerformed

    /**
     * Llena la tabla de Estados al seleccionar un pais.
     * @param evt evento que dispara la funcion.
     */
    private void tblPaisesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPaisesMouseClicked
        if(evt.getClickCount() == 2) {
            paisId = (int) UtilsTable.obtenerValor(tblPaises.getSelectedRow(), 0, tblPaises);
            enableEstados(true);
            llenaEstados();
        }
    }//GEN-LAST:event_tblPaisesMouseClicked

    /**
     * Filtra la tabla de Estados al escribir una letra en el filtro.
     * @param evt evento que dispara la funcion.
     */
    private void txtEstadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyReleased
        llenaEstados();
    }//GEN-LAST:event_txtEstadoKeyReleased

    /**
     * Abre una ventana para agregar un Estado.
     * @param evt evento que dispara la funcion.
     */
    private void btnNewEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewEstadoActionPerformed
        AddNewEstado addNewEstado = new AddNewEstado(parent, true);        
        addNewEstado.setPaisId(paisId);
        addNewEstado.setLocationRelativeTo(this);
        addNewEstado.setVisible(true);               
        
        // Cuando cierra la ventana agrega el Estado a la tabla y lo selecciona.
        llenaEstados();
        estadoId = UtilsTable.getRow(addNewEstado.estadoId, estadosLista);
        UtilsTable.mueveTabla(estadoId, tblEstados);
    }//GEN-LAST:event_btnNewEstadoActionPerformed

    /**
     * Abre una ventana para editar un Estado.
     * @param evt evento que dispara la funcion.
     */
    private void btnEditEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEstadoActionPerformed
        if(tblEstados.getSelectedRow() < 0) {
           
            // Suena un beep y muestra un mensaje.
            Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila", "Información", 1);
        } else {
            estadoId = (int) UtilsTable.obtenerValor(tblEstados.getSelectedRow(), 0, tblEstados);
            AddNewEstado addNewEstado = new AddNewEstado(parent, true);        
            addNewEstado.setPaisId(paisId);
            addNewEstado.setEditId(estadoId);          
            addNewEstado.setLocationRelativeTo(this);
            addNewEstado.setVisible(true);               

            // Cuando cierra la ventana agrega el Estado a la tabla y lo selecciona.
            llenaEstados();
            UtilsTable.mueveTabla(UtilsTable.getRow(addNewEstado.estadoId, estadosLista), tblEstados);
        }
    }//GEN-LAST:event_btnEditEstadoActionPerformed

    /**
     * Abre una ventana para editar un Estado al seleccionarlo en la tabla.
     * @param evt evento que dispara la funcion.
     */
    private void tblEstadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstadosMouseClicked
        if(evt.getClickCount() == 2) {
            estadoId = (int) UtilsTable.obtenerValor(tblEstados.getSelectedRow(), 0, tblEstados);
            btnEditEstadoActionPerformed(null);
        }                    
    }//GEN-LAST:event_tblEstadosMouseClicked

    /**
     * Elimina de la base de datos el Estado seleccionado.
     * @param evt evento que dispara la funcion.
     */
    private void btnDeleteEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEstadoActionPerformed
        if(tblEstados.getSelectedRow() < 0) {
            
            // Suena un beep y se muestra un mensaje.
            java.awt.Toolkit.getDefaultToolkit().beep();
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila","Aviso", 2);
        } else {
            
            // Suena un beep y se muestra un mensaje.
            java.awt.Toolkit.getDefaultToolkit().beep();
            int res = javax.swing.JOptionPane.showConfirmDialog(this, "¿Eliminar " + UtilsTable.obtenerValor(tblEstados.getSelectedRow(), 1, tblEstados).toString() + "?",
                 "Seleccione", JOptionPane.YES_NO_OPTION);
            
            // Si la respuesta es afirmativa, elimina el registro.
            if(res == 0) {
                String msj = "";                
                EstadoDAO estadoDAO = new EstadoDAO();
                int ret = estadoDAO.deleteEstado((int) UtilsTable.obtenerValor(tblEstados.getSelectedRow(), 0, tblEstados));
                if(ret != 1) {
                    msj = "No se pudo eliminar por que tiene registros asignados.";
                    javax.swing.JOptionPane.showMessageDialog(this, msj, "Información", 1);
                }                
                
                // Reinicia controles y parametros.
                estadoId = 0;
                llenaEstados();
            }
        }
    }//GEN-LAST:event_btnDeleteEstadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteEstado;
    private javax.swing.JButton btnDeletePais;
    private javax.swing.JButton btnEditEstado;
    private javax.swing.JButton btnEditPais;
    private javax.swing.JButton btnNewEstado;
    private javax.swing.JButton btnNewPais;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEstados;
    private javax.swing.JLabel lblFiltroEstados;
    private javax.swing.JLabel lblFiltroPais;
    private javax.swing.JLabel lblPais;
    private javax.swing.JPanel pnlEstados;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JScrollPane scpTableList;
    private javax.swing.JScrollPane scpTableList1;
    private javax.swing.JTable tblEstados;
    private javax.swing.JTable tblPaises;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtPais;
    // End of variables declaration//GEN-END:variables
}