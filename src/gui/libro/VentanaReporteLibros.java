package gui.libro;

import dataBase.ReporteLibrosView;
import java.awt.BorderLayout;
import java.awt.Toolkit;

/**
 *
 * @author Carlos
 * Ventana que muestra el reporte de peliculas
 */
public class VentanaReporteLibros extends javax.swing.JFrame {

    public VentanaReporteLibros() {
        initComponents();
        conf();
        // configuración para cargar el reporte
        pnlReporte.setLayout(new BorderLayout());
        // ejecuta el reporte y lo muestra en la ventana
        ReporteLibrosView reporte_libros = new ReporteLibrosView();    
        pnlReporte.add(reporte_libros.getReport()); 
    }
    
    private void conf(){      
        // localización de la ventana
        setLocationRelativeTo(null);	
        // titulo de la ventana
        setTitle("Libros");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlReporte = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generando...");
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/peliculas.png")));

        javax.swing.GroupLayout pnlReporteLayout = new javax.swing.GroupLayout(pnlReporte);
        pnlReporte.setLayout(pnlReporteLayout);
        pnlReporteLayout.setHorizontalGroup(
            pnlReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 846, Short.MAX_VALUE)
        );
        pnlReporteLayout.setVerticalGroup(
            pnlReporteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlReporte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlReporte;
    // End of variables declaration//GEN-END:variables
}
