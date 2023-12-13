/*
   Operaciones a la base de datos en la tabla Categoria
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DaoCategory extends Conexion {
    
    public Object [][] getCategories(){
       conectar();
        Object [][] categories;
       int i = 0;
       int cant = 0;
       try{
           // consulta
           sentenciaSQL = "SELECT COUNT(*) FROM CATEGORY";
           // prepara la consulta
           ps = conn.prepareStatement(sentenciaSQL);
           // ejecuta la consulta
           rs = ps.executeQuery();
           // Ontiene la cantidad de registros
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           // consulta los datos
           categories = new Object[cant][2];
           sentenciaSQL  = "SELECT * FROM CATEGORY ORDER BY CATEGORY";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           while (rs.next()){
               //guarda los tados para regresarlos
               categories[i][0] = rs.getInt(1);
               categories[i][1] = rs.getString(2);
               i++;
           }           
           return categories;   
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getCategories");
            return null;
        }
       finally{
           desconectar();           
       }
    }
}