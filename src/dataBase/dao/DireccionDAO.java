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
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveDireccion");
            return null;
        }
        finally{
           desconectar();
       }

    }

    // Modificar una direccion
    public String UpdateDireccion(Object[] dir)
    {       
        // se conecta a la base de datos
        conectar();
        try{
           // actualiza los datos
           sentenciaSQL =   "UPDATE DIRECCION SET " +
                            "ALCALDIA = ?, " +
                            "CP = ?, " +
                            "CALLE = ?, " +
                            "EXTERIOR = ?, " +
                            "INTERIOR = ?, " +
                            "ESTADO_ID = ?, " +
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, dir[1].toString());     // Nueva alcaldia
            ps.setString(2, dir[2].toString());     // Nuevo CP
            ps.setString(3, dir[3].toString());     // Nueva calle
            ps.setString(4, dir[4].toString());     // Nuevo numero exterior
            ps.setString(5, dir[5].toString());     // Nuevo numero interior
            ps.setString(6, dir[6].toString());     // Nuevo id de estado
            ps.setString(7, dir[0].toString());     // Id de la direccion a modificar
            ps.executeUpdate();
            return dir[0].toString();               // Regresa el id de la direcion
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateDireccion");
            return null;
        }
        finally{
           desconectar();
       }
    }

    // Consultar todas las direcciones
    public Object [][] GetAllDireccionesByEstado(String estado_id){
        conectar();
        Object [][] dirs;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM DIRECCIONES WHERE ESTADO_ID = ?";     // Numero de direcciones en ese estado
            ps = conn.prepareStatement(sentenciaSQL);       // Convierte el str a un sentencia utilizable en SQL        
            ps.setString(1, estado_id);                     // Nuevo numero exterior
            rs = ps.executeQuery();                         // Resultado de la consulta

            // Numero de direcciones en ese estado (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todas las direcciones (ID, alcaldia, cp, calle, no. exterior, no. interior, estado id)
            dirs = new Object[count][7];           
            sentenciaSQL  = "SELECT DIRECCION_ID, ALCALDIA, CP, CALLE, EXTERIOR, INTERIOR, ESTADO_ID " +
                            "FROM DIRECCION WHERE ESTADO_ID = ? ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado_id);
            rs = ps.executeQuery();

            // Agregar a todas las direcciones al arreglo (migrar de rs a dirs)
            while (rs.next()){
                dirs[i][0] = (rs.getString(1));     // Id
                dirs[i][1] = (rs.getString(2));     // Alcaldia
                dirs[i][2] = (rs.getString(3));     // CP
                dirs[i][3] = (rs.getString(4));     // Calle
                dirs[i][4] = (rs.getString(5));     // Exterior
                dirs[i][5] = (rs.getString(6));     // Interior
                dirs[i][6] = (rs.getString(7));     // Estado id
                i++;
            }           
            return dirs;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllDireccionesByEstado");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consultar una direccion por id
    public Object[] GetDireccionById(String direccion_id)
    {
        conectar();
        Object [] direccion = new Object[7];
       
        try{                    
            sentenciaSQL  = "SELECT DIRECCION_ID, ALCALDIA, CP, CALLE, EXTERIOR, INTERIOR, ESTADO_ID FROM DIRECCION " + 
                            "WHERE DIRECCION_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, direccion_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo actor con el resultado
            while (rs.next()){
                direccion[0] = (rs.getString(1));
                direccion[1] = (rs.getString(2));  
                direccion[2] = (rs.getString(3));
                direccion[3] = (rs.getString(4));  
                direccion[4] = (rs.getString(5));
                direccion[5] = (rs.getString(6));  
                direccion[6] = (rs.getString(7));
            }           
            return direccion;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetDireccionById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Borrar una direccion
    public int DeleteDireccion(String direccion_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL la direccion de cualquier empleado/sucursal/cliente con la direccion a borrar
            sentenciaSQL =  "UPDATE CLIENTE" +
                            "SET DIRECCION_ID = NULL " +
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, direccion_id);
            ps.executeUpdate();
            
             sentenciaSQL =  "UPDATE EMPLEADO" +
                            "SET DIRECCION_ID = NULL " +
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, direccion_id);
            ps.executeUpdate();
            
            sentenciaSQL =  "UPDATE SUCURSAL" +
                            "SET DIRECCION_ID = NULL " +
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, direccion_id);
            ps.executeUpdate();
            
            // Borrar el idioma
            sentenciaSQL = "DELETE FROM DIRECCION " +
                            "WHERE DIRECCION_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, direccion_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteDireccion");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }

}
