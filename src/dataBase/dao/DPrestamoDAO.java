/*
 Operaciones a la base de datos en la tabla detalles de prestamo
 */
package dataBase.dao;
import dataBase.Conexion;
import java.sql.*;

public class DPrestamoDAO extends Conexion { // Para llamar procedimientos almacenados
    
       public int saveDPrestamo (String idPrestamo, int idLibro){ // Guarda un libro y  se conecta a la base de datos
        conectar();
        try{

            sentenciaSQL = " INSERT INTO DETALLES_PRESTAMO VALUES (?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idPrestamo);
            ps.setInt(2, idLibro);
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDPrestamo");
            return 0;
        }
       finally{
           desconectar();
       }
    }  
}
