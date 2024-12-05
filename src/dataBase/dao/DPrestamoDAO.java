/*
 Operaciones a la base de datos en la tabla detalles de prestamo
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DPrestamoDAO extends Conexion { // Para llamar procedimientos almacenados    
    public int saveDPrestamo(int idPrestamo, int idLibro) {
        conectar();
        try {

            sentenciaSQL = "INSERT INTO DETALLES_PRESTAMO VALUES (?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, idLibro);
            ps.setInt(2, idPrestamo);            
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDPrestamo");
            return 0;
        }
        finally {
            desconectar();
        }
    }                             
}
