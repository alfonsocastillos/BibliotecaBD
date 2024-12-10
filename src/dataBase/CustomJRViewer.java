package dataBase;

import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.swing.JRViewerToolbar;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;

/**
 * Clase encargada de visualizar los reportes de Jasper Reports.
 * @author Carlos Cortés Bazán
 */
public class CustomJRViewer extends JRViewer {    
    
    /**
     * Constructor de la clase.
     * @param jasperPrint reporte a visualizar.
     */
    public CustomJRViewer(JasperPrint jasperPrint) {
        super(jasperPrint);
    }

    /**
     * Crea un visualizador para los reportes de Jasper Reports.
     * @return el Toolbar de Jasper Reports creado.
     */
    @Override
    protected JRViewerToolbar createToolbar() {
        JRViewerToolbar toolbar = super.createToolbar();
        
        // Información regional.
        Locale locale = viewerContext.getLocale();
        
        // Contiene objetos que varían conforme al Locale.
        ResourceBundle resBundle = viewerContext.getResourceBundle();
        
        // Especifica que el reporte se hará en PDF con el Locale apropiado.
        JRPdfSaveContributor pdf = new JRPdfSaveContributor(locale, resBundle);
        toolbar.setSaveContributors(new JRSaveContributor[] {pdf});
        return toolbar;
    }   
}