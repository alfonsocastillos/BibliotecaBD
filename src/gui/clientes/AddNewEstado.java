package gui.clientes;

import dataBase.dao.EstadoDAO;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 * Ventana que permite crear, editar y eliminar Estados de un Pais.
 * @author alfonso
 */
public class AddNewEstado extends javax.swing.JDialog {
    int estadoId;
    int paisId;
    EstadoDAO estadoDAO;

    /**
     * Creates new form AddNewEstado.
     * @param parent ventana padre.
     * @param modal determina si la ventana no cede el foco a otra.
     */
    public AddNewEstado(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
       
        // Inicia los componentes.
        initComponents();
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        
        // Crea el dao para acceder a la tabla Estado.
        estadoDAO = new EstadoDAO();
        getRootPane().setDefaultButton(btnGuardar);                      
    }
    /**
     * Borra el texto
     */
    private void borrarTextos() {        
        txtEstado.setText("");
    }
    
    /**
     * Asignar el Id del Estado siendo editada.
     * @param estadoId Id del estado siendo editado.
     */
    public void setEditId(int estadoId) {
        this.estadoId = estadoId;
        Object[] estadoEdit = estadoDAO.getEstadoById(estadoId);
        
        // Muestra los datos en los controles.
        txtEstado.setText(estadoEdit[1].toString());        
    }
    
    /**
     * Asignar el Id del Pais al que pertenece el Estado.
     * @param paisId Id del pais al que pertenece el Estado.
     */
    public void setPaisId(int paisId) {
        this.paisId = paisId;
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
        txtEstado = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Estado");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage( getClass().getResource("/img/libros.png")));

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtEstado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pnlTableList.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 140, 25));

        lblNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblNombre.setText("Estado:");
        pnlTableList.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 25));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.setFocusable(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        pnlTableList.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Guarda la informacion introducida en la base de datos.
     * @param evt evento que dispara la funcion.
     */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String estadoNombre = txtEstado.getText().trim();
        if(estadoNombre.length() == 0) {
           
            // Suena un beep y muestra un mesaje.
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Escriba el estado", "Aviso", 2);
        } else {                                  
            if(estadoId == 0) {
                Object[] estado = {estadoNombre, paisId};
                estadoId = estadoDAO.saveEstado(estado);                
            } else {
                Object[] estado_obj = {estadoId, estadoNombre};                
                estadoId = estadoDAO.updateEstado(estado_obj);                
            }
            
            if(estadoId == 0) {
                
                // Suena un beep y muestra un mesaje.
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(this, "Error al guardar el estado", "Error", 0);                
            } else {
                borrarTextos();
                dispose();
            }            
        }  
    }//GEN-LAST:event_btnGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JTextField txtEstado;
    // End of variables declaration//GEN-END:variables
}