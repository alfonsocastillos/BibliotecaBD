package gui.reportes;

import dataBase.ReporteLibrosPDF1;
import dataBase.ReporteLibrosPDF2;
/**
 *
 * @author Jordi
 * Ventana que registra clientes
 */
public class Reportes extends javax.swing.JInternalFrame {
    
    public Reportes() {
        // Constructor
        initComponents();    
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
        btnReporte2 = new javax.swing.JButton();
        btnReportePDF2 = new javax.swing.JButton();
        pnlTableList1 = new javax.swing.JPanel();
        btnReporte1 = new javax.swing.JButton();
        btnReportePDF1 = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Reportes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/img/query.png"))); // NOI18N
        setName(""); // NOI18N

        pnlTableList.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "La lista de géneros y la cantidad de préstamos de cada uno.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        pnlTableList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReporte2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/reporte.png"))); // NOI18N
        btnReporte2.setToolTipText("Mostrar reporte");
        btnReporte2.setFocusable(false);
        btnReporte2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporte2ActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReporte2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 60, 60));

        btnReportePDF2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/pdf-icon.png"))); // NOI18N
        btnReportePDF2.setToolTipText("Mostrar en PDF");
        btnReportePDF2.setFocusable(false);
        btnReportePDF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePDF2ActionPerformed(evt);
            }
        });
        pnlTableList.add(btnReportePDF2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 60, 60));

        pnlTableList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "El empleado que realiza más prestamos.", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnlTableList1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReporte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/reporte.png"))); // NOI18N
        btnReporte1.setToolTipText("Mostrar reporte");
        btnReporte1.setFocusable(false);
        btnReporte1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporte1ActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnReporte1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 60, 60));

        btnReportePDF1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Acciones/pdf-icon.png"))); // NOI18N
        btnReportePDF1.setToolTipText("Mostrar en PDF");
        btnReportePDF1.setFocusable(false);
        btnReportePDF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportePDF1ActionPerformed(evt);
            }
        });
        pnlTableList1.add(btnReportePDF1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 60, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnlTableList1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(pnlTableList, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(pnlTableList1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReporte2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporte2ActionPerformed
        VentanaReporte2 reporte_libros2 = new VentanaReporte2();
        reporte_libros2.setVisible(true);
    }//GEN-LAST:event_btnReporte2ActionPerformed

    private void btnReportePDF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDF2ActionPerformed
        ReporteLibrosPDF2 reporte = new ReporteLibrosPDF2();
        reporte.genReport();
    }//GEN-LAST:event_btnReportePDF2ActionPerformed
       
    private void btnReporte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporte1ActionPerformed
        // Empleado que realiza mas préstamos
        VentanaReporte1 reporte_libros1 = new VentanaReporte1();
        reporte_libros1.setVisible(true);
    }//GEN-LAST:event_btnReporte1ActionPerformed

    private void btnReportePDF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportePDF1ActionPerformed
        // Empleado que realiza mas prestamos
        // genera y ejecuta el reporte en PDF
        ReporteLibrosPDF1 reporte = new ReporteLibrosPDF1();
        reporte.genReport();
        
    }//GEN-LAST:event_btnReportePDF1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReporte1;
    private javax.swing.JButton btnReporte2;
    private javax.swing.JButton btnReportePDF1;
    private javax.swing.JButton btnReportePDF2;
    private javax.swing.JPanel pnlTableList;
    private javax.swing.JPanel pnlTableList1;
    // End of variables declaration//GEN-END:variables
}
