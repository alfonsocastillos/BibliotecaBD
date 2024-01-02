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

/* 
    Clase que genera el reporte directamente en PDF y lo abre con acrobat, hereda de la
    clase conexión para ahorrar codigo
*/

// TODO: implementar el reporte Libros.jrxml

public class ReporteLibrosPDF extends Conexion {
   
  public void genReport(){
        // Conecta a la base de datos
       conectar();
       try{
           
           // Lee el archivo de reporte y compila el reporte
           JasperReport report = JasperCompileManager.compileReport(getClass().getResource("/reportes/Libros.jrxml").getPath());
           // Paramatros del reporte, aun que no se le mandan parametros, es necesario para las imagenes 
           // que usa el reporte
           Map<String, Object> parametros = new HashMap<>();
           // Ruta de las imagenes que usa el reporte
           String reportsDirPath = getClass().getResource("/reportes").getPath();
           File reportsDir = new File(reportsDirPath);
           // Configuracíón del parametro
           parametros.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));
           // Genera el reporte
           JasperPrint print = JasperFillManager.fillReport(report,parametros,conn);
           // Exporta el reporte con el nombre Archivo.PDF
           JasperExportManager.exportReportToPdfFile(print, "ReporteLibros.PDF");
           // Crea un file con el reporte para poder abrirlo
           File path = new File ("ReporteLibros.PDF");
           // abre el reporte 
           Desktop.getDesktop().open(path);
       }
       catch (JRException ex){
           System.out.println("Error " + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
       }
       catch (IOException ex){
           System.out.println("Error " + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
       }
      
    }
}