/*
   Operaciones a la base de datos en la tabla detalle renta
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;

public class DaoDRenta extends Conexion {
    // Para llamar procedimientos almacenados
    
    
    public int saveDRenta(String idRenta, int idPelicula){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{

             // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = " INSERT INTO RENTAL_DETAIL VALUES (?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idRenta);
            ps.setInt(2, idPelicula);
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveDRenta");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
}