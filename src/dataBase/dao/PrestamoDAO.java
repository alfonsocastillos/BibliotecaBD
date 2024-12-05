/*
   Clase encargada de consultar, insertar, eliminar y modificar tabla de prestamo
 */
package dataBase.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;
   
public class PrestamoDAO extends Conexion {     
    public int SavePrestamo(Object[] prestamo) {        
        conectar();
        try {
            String id = null;
         
            // genera el nuevo id
            sentenciaSQL = "SELECT SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')||LPAD(NVL(MAX(TO_NUMBER(SUBSTR(PRESTAMO_ID,7,3)))+1,1),3,'0') \n" +
                           " FROM PRESTAMO\n" +
                           " WHERE SUBSTR(PRESTAMO_ID,1,6) LIKE SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()) {
                id = rs.getString(1);
            }
            
            sentenciaSQL = "INSERT INTO PRESTAMO VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, Integer.valueOf(id));          // Id del prestamo
            ps.setString(2, prestamo[0].toString());    // Fecha inicio
            ps.setString(3, prestamo[1].toString());    // Fecha entrega
            ps.setInt(4, (int) prestamo[2]);            // Id del cliente 
            ps.setString(5, prestamo[3].toString());    // Id del empleado
            ps.executeUpdate();
            return Integer.valueOf(id);
        }
        catch (SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveLibro");
            return 0;
        }
        finally{
            desconectar();
        }
    }      
        
    // Consultar un prestamo  por id
    public Object[][] GetPrestamosByCliente(int cliente_id) {
        conectar();
        try{                    
            sentenciaSQL  = "SELECT COUNT(*) FROM PRESTAMO " +
                            "JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID) " +
                            "WHERE CLIENTE_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();

            int count = 0;
            while(rs.next()) {
                count = rs.getInt(1);                
            }
            
            Object[][] prestamos = new Object[count][3];
            sentenciaSQL =  "SELECT TITULO, FECHA_PRESTAMO, FECHA_ENTREGA FROM PRESTAMO " +
                            "JOIN DETALLES_PRESTAMO USING (PRESTAMO_ID)" +
                            "JOIN LIBRO USING (LIBRO_ID) " +
                            "WHERE CLIENTE_ID = ? ORDER BY 2";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, cliente_id);
            rs = ps.executeQuery();
            
            int i = 0;
            while(rs.next()) {
                prestamos[i][0] = rs.getString(1);                      // Titulo del libro     
                                 
                DateTimeFormatter from_format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.A");
                DateTimeFormatter to_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime fecha_prestamo = LocalDateTime.parse(rs.getString(2), from_format);
                prestamos[i][1] = to_format.format(fecha_prestamo);   // Fecha prestamo
                
                LocalDateTime fecha_entrega = LocalDateTime.parse(rs.getString(3), from_format);
                prestamos[i][2] = to_format.format(fecha_entrega);    // Fecha entrega
                i++;
            }           
            return prestamos;
        }
        catch (SQLException ex ){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetPrestamoById");
            return null;
        }
        finally {
            desconectar();           
        }
    }
    
    // Borrar un prestamo
    public int DeletePrestamo(int prestamo_id){
        conectar(); // Conecta a la base de datos
        try{
            // Borrar cualquier detalle relacionado
            sentenciaSQL =  "DELETE FROM DETALLES_PRESTAMO " +
                            "WHERE PRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, prestamo_id);
            
            // Borrar el prestamo
            sentenciaSQL = "DELETE FROM PRESTAMO " +
                            "WHERE PRESTAMO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, prestamo_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeletePrestamo");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }

    public String savePrestamo(Object[] Prestamo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
