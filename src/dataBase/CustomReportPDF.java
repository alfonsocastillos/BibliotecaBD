package dataBase;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

// TODO:    implementar el reporte Libros.jrxml
//          hacer generica la clase

/**
 * Clase que genera el reporte directamente en PDF y lo abre con Acrobat.
 * @author Carlos Cortés Bazán
 */
public class LibrosReportPDF extends CustomConnection {
   
    /**
     * Genera un reporte de Jasper Reports.
     */
    public void generateReport() {
        
        // Conecta a la base de datos.
        connect();
        try {
           
           // Lee el archivo de reporte y compila el reporte.
           JasperReport report = JasperCompileManager.compileReport(getClass().getResource("/reportes/Libros.jrxml").getPath());
           
            // Paramatros del reporte, aun que no se le mandan parametros, es necesario para las imagenes que usa el reporte.
            Map<String, Object> parameters = new HashMap<>();
            
            // Ruta de las imagenes que usa el reporte.
            String reportsPath = getClass().getResource("/reportes").getPath();
            File reportsFile = new File(reportsPath);
                
            // Configuracíón del parametro.
            parameters.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsFile));
            
            // Genera el reporte.
            JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
            
            // Exporta el reporte con el nombre Archivo.PDF.
            JasperExportManager.exportReportToPdfFile(print, "ReporteLibros.PDF");
            
            // Crea un file con el reporte para poder abrirlo.
            File path = new File ("ReporteLibros.PDF");
            
            // Abre el reporte.
            Desktop.getDesktop().open(path);
        } catch(JRException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
        } catch(IOException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
        }      
    }
}