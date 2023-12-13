/*
   Operaciones a la base de datos en la tabla tipos de pagos
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/

public class DaoTPagos extends Conexion {
     // Para llamar procedimientos almacenados
    
    public Object [][] getTPagos(){
        // Obtiene todos lo idiomas
        // Conecta a la base de datos
       conectar();
       // Variable para guardar los idiomas
       Object[][] tpagos;
       // Contador
       int i = 0;int cant = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM PAYMENT_METHOD";
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           tpagos = new Object[cant][2];           
           sentenciaSQL  = "SELECT PAYMENT_METHOD_ID, PAYMENT_METHOD FROM PAYMENT_METHOD";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
             
           // Recorre el result set para obtener los datos y asignarlos
           // al arreglo
           while (rs.next()){
               tpagos[i][0] = rs.getInt(1);
               tpagos[i][1] = rs.getString(2);  
               i++;
           }     
           return tpagos;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getTPagos");
            return null;
        }
       finally{
           desconectar();           
       }
    }
}