/*
   Operaciones a la base de datos en la tabla empleado
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/* 
    Clase que realiza todas las transacciones a la base de datos, hereda de la
    clase conexión para ahorrar codigo
*/

public class EmpleadoDAO extends Conexion {
    
    public Object[] GetEmpleadoByUsr(String usr, char psw[]){
        // Busca un empleado usando como parametro su usuario y contraseña
        // Se conecta a la base de datos
       conectar();
       // variable que guardara los datos del empleado
       Object employee [];
       try{       
            employee = new Object[5];
            // consulta
            sentenciaSQL  = "SELECT empleado_id, sucursal_id, sucursal.nombre, empleado.nombre, apellido_pat || ' ' || apellido_mat " +
                            "FROM empleado " +
                            "JOIN sucursal USING (sucursal_id) " +
                            "WHERE usuario LIKE ? " +
                            "AND contrasenia LIKE ?";           
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
            else {
                employee [0] = 0;
            }
                
            return employee;
        }
        catch (SQLException ex){
           System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEmpleadoByUsr");
            return null;
        }
        finally{
           desconectar();           
       }
    }
}