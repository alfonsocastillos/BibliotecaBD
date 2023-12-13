/*
   Operaciones a la base de datos en la tabla detalle renta
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;

public class DaoPayment extends Conexion {
    // Para llamar procedimientos almacenados
    
    
    public int savePayment(String idRenta, int idMethodPay, float monto){
        // Guarda una pelicula
        // Se conecta a la base de datos
        conectar();
        try{

             // inseta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = " INSERT INTO PAYMENT VALUES (?,?,?)";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, idRenta);
            ps.setInt(2, idMethodPay);
            ps.setFloat(3, monto);
            ps.executeUpdate();
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "savePayment");
            return 0;
        }
       finally{
           desconectar();
       }
    }
    
}