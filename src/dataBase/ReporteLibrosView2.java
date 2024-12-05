package dataBase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

/* 
    Clase que genera el reporte para visualizarlo en una ventana, hereda de la
    clase conexión para ahorrar codigo
*/

// TODO: implementar el reporte Libros.jrxml

public class ReporteLibrosView2 extends Conexion {
    
    public JRViewer getReport(){
        // Conecta a la base de datos
       conectar();
       try{
           // Lee el archivo de reporte
           JasperDesign desing = JRXmlLoader.load(getClass().getResource("/reportes/Consulta5_examen.jrxml").getPath());
           // Compila el reporte
           JasperReport jr = JasperCompileManager.compileReport(desing);
           // Paramatros del reporte, aun que no se le mandan parametros, es necesario para las imagenes 
           // que usa el reporte
           Map<String, Object> parametros = new HashMap<>();
           // Ruta de las imagenes que usa el reporte
           String reportsDirPath = getClass().getResource("/reportes").getPath();
           File reportsDir = new File(reportsDirPath);
           // Configuracíón del parametro
           parametros.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));
           // Genera el reporte
           JasperPrint jp = JasperFillManager.fillReport(jr, parametros,conn);
           // Muestra el reporte graficamente se usa la clase MyJRViewer en lugar de JRViewer para 
           // que solo guarde PDF´s
           MyJRViewer myjrv = new MyJRViewer(jp);
           JRViewer jrv = myjrv;
           return jrv;
       }
       catch (JRException ex){
           System.out.println(ConfigDataBase.DB_T_ERROR + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getReport");
           return null;
       }
    }
}