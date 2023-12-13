/*
   Operaciones a la base de datos en la tabla tienda
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DaoStore extends Conexion {
    
    // Cuenta el numero de alumnos en toda la DB
    public Object[][] getStores(){
       conectar();
       Object[][] stores;
       int i = 0;
       int cant = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM STORE";
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           stores = new Object [cant][8];
           
           sentenciaSQL  = "SELECT STR_ID, ADS_LINE1,ADS_LINE2, ADS_DISTRICT,CTY_NAME,ADS_POSTAL_CODE,ADS_PHONE,ADS_EMAIL " +
                            "FROM STORE, ADDRESS, CITY " +
                            "WHERE STR_ADS_ID = ADS_ID " +
                            "AND ADS_CTY_ID = CTY_ID";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           while (rs.next()){
               stores[i][0]= (rs.getInt(1));
               stores[i][1]=(rs.getString(2));
               if (rs.getString(3) == null)
                   stores[i][2]=("");
               else
                   stores[i][2]=(rs.getString(3));
               stores[i][3]=(rs.getString(4));
               stores[i][4]=(rs.getString(5));
               stores[i][5]=(rs.getString(6));
               stores[i][6]=(rs.getString(7));
               stores[i][7]=(rs.getString(8));    
               i++;
           }           
           return stores;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getStores");
            return null;
        }
       finally{
           desconectar();           
       }
    }
}