/*
    Operaciones a la base de datos en la tabla de prestamo
 */
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;
   
public class PrestamoDAO extends Conexion { // Para llamar procedimientos almacenados
  
    public String savePrestamo (Object[] film){ // Guarda una pelicula y se conecta a la base de datos
        
        conectar();
        try{
            String id = null;
            
            // genera el nuevo id
            sentenciaSQL = "SELECT SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')||LPAD(NVL(MAX(TO_NUMBER(SUBSTR(PRESTAMO_ID,7,3)))+1,1),3,'0') \n" +
                           " FROM PRESTAMO\n" +
                           " WHERE SUBSTR(PRESTAMO_ID,1,6) LIKE SUBSTR (EXTRACT(YEAR FROM SYSDATE),3,2)||LPAD(EXTRACT(MONTH FROM SYSDATE),2,'0') || LPAD(EXTRACT(DAY FROM SYSDATE),2,'0')";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO PRESTAMO VALUES (?,?,?,?,?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, id);
            // Asigna los valores del arreglo            
            ps.setString(2, film [0].toString());
            ps.setString(3,film [1].toString());
            ps.setString(4,film [2].toString());
            ps.setString(5,film [3].toString());
            ps.setString(6,film [4].toString());        
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveLibro");
            return null;
        }
       finally{
           desconectar();
       }
    }    
}
