/*
   Operaciones a la base de datos en la tabla cliente
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class DaoCustomer extends Conexion {
     
    public Object [][] getCustomers(){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object[][] customers; 
        // Contador
        int i = 0;
        int cant = 0;
        
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM CUSTOMER";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();

            if (rs.next()){
                cant = rs.getInt(1);
            }

            customers = new Object[cant][3];
            sentenciaSQL  = "SELECT CUSTOMER_ID, FIRST_NAME, LAST_NAME FROM CUSTOMER";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                customers[i][0]=(rs.getInt(1));
                customers[i][1]=(rs.getString(2));  
                customers[i][2]=(rs.getString(3));  
                i++;
            }           
            return customers;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getCustomers");
            return null;
        }
        finally{
            desconectar();           
        }
    }
}