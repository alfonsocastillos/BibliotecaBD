/*
   Operaciones a la base de datos en la tabla empleado
*/
package dataBase.dao;

import dataBase.Conexion;
import java.sql.*;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/

public class DaoEmployee extends Conexion {
    
    public Object[] getEmployeeByUsr(String usr, char psw[]){
        // Busca un empleado usando como parametro su usuario y contraseña
        // Se conecta a la base de datos
       conectar();
       // variable que guardara los datos del empleado
       Object employee [];
       try{       
            employee = new Object[5];
            // consulta
            sentenciaSQL  = "SELECT STAFF_ID, STORE_ID, STORE_NAME, FIRST_NAME, LAST_NAME " +
                            "FROM STAFF " +
                            "JOIN STORE USING (STORE_ID) " +
                            "WHERE USERNAME LIKE ? " +
                            "AND PASSWORD LIKE ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            // asigna los parametros
            ps.setString(1, usr);
            ps.setString(2, String.valueOf(psw));
            rs = ps.executeQuery();
            if (rs.next()){
                // Recupera los valores
                employee [0] = rs.getString(1);
                employee [1] = rs.getString(2);
                employee [2] = rs.getString(3);
                employee [3] = rs.getString(4);
                employee [4] = rs.getString(5);
                
            }
            else 
                employee [0] = 0;
            return employee;
       }
       catch (SQLException ex){
           System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getEmployeeByUsr");
            return null;
        }
       finally{
           desconectar();           
       }
    }
}