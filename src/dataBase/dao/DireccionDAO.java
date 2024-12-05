/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Direccion
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class DireccionDAO extends Conexion {
    // Crear una direccion
    public String SaveDireccion(Object[] dir) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id (año + mes + 3 num)       
            sentenciaSQL =  "SELECT LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(DIRECCION_ID, 5, 3))) + 1, 1), 3, '0') " +
                            "FROM DIRECCION " +
                            "WHERE SUBSTR(DIRECCION_ID, 1, 4) = LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0')";
            
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO DIRECCION VALUES(?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                        // Id (calculado)
            ps.setString(2, dir[0].toString());         // Alcaldia (provisto)
            ps.setString(3, dir[1].toString());         // CP (provisto)
            ps.setString(4, dir[2].toString());         // Calle (provisto)
            ps.setString(5, dir[3].toString());         // Numero exterior (provisto)
            ps.setString(6, dir[4].toString());         // Numero interior (NULL)
            ps.setString(7, dir[5].toString());         // Id del estado (provisto)            
            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveDireccion");
            return null;
        }
        finally{
           desconectar();
       }

    }               
}
