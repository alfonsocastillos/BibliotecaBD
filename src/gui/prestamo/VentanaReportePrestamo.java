package gui.prestamo;

//import dataBase.ReportePrestamoView;
import java.awt.BorderLayout;
import java.awt.Toolkit;
/**
 * Ventana que muestra el reporte de la venta
 */

public class VentanaReportePrestamo extends javax.swing.JFrame {
   String id;

    /**
     * Creates new form Ventana
     * @param id
     */
    public VentanaReportePrestamo(String id) {
        // id del prestamos a mostrar
        this.id = id;
        initComponents();
        conf();
        
        // configuración para cargar el reporte
        pnlReporte.setLayout(new BorderLayout());
        
        // ejecuta el reporte y lo muestra en la ventana
//        ReportePrestamoView reportePrestamo = new ReportePrestamoView();    
//      pnlReporte.add(reportePrestamo.getReport(id)); 
    }
    
    private void conf(){        
        // localización del prestamo
        setLocationRelativeTo(null);	
        // Titulo del prestamo
        setTitle("Ticket");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pnlReporte = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generando ticket...");
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("img/renta.png")));

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
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify                     
    private javax.swing.JPanel pnlReporte;
    // End of variables declaration                        
}