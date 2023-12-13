/*
   Operaciones a la base de datos en la tabla clasificación
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DaoRate extends Conexion {
    
    public Object [][] getRates(){
       conectar();
       Object [][] rates;
       int i = 0;
       int cant = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM RATING";
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           rates = new Object[cant][3];           
           sentenciaSQL  = "SELECT * FROM RATING";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           while (rs.next()){
               rates[i][0]=(rs.getInt(1));
               rates[i][1]=(rs.getString(2));  
               rates[i][2]=(rs.getString(3));  
               i++;
           }           
           return rates;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getRates");
            return null;
        }
       finally{
           desconectar();           
       }
    }
}