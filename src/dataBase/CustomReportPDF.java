package dataBase;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
public class CustomReportPDF extends CustomConnection {
   
    /**
     * Genera un reporte de Jasper Reports.
     * @param reportUrl URL en la que se encuentra el archivo descriptor del reporte.
     * @param parameters Parametros a utilizar en el reporte.
     */
    public void generateReport(String reportUrl, Map<String, Object> parameters) {
        connect();
        try {
           
           // Lee el archivo de reporte y compila el reporte.
           JasperReport report = JasperCompileManager.compileReport(getClass().getResource(reportUrl).getPath());
            
            // Ruta de las imagenes que usa el reporte.
            String reportsPath = getClass().getResource("/reportes").getPath();
            File reportsFile = new File(reportsPath);
                
            // Configuracíón del parametro.
            parameters.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsFile));
            
            // Genera el reporte.
            JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);
            
            // Exporta el reporte con el nombre Archivo.PDF.
            String pdfName;
            pdfName = reportUrl.substring(reportUrl.lastIndexOf("/") + 1, reportUrl.indexOf(".") - 1);
            JasperExportManager.exportReportToPdfFile(print, pdfName);
            
            // Crea un file con el reporte para poder abrirlo.
            File path = new File (pdfName);
            
            // Abre el reporte.
            Desktop.getDesktop().open(path);
        } catch(JRException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
        } catch(IOException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
        }      
    }
}