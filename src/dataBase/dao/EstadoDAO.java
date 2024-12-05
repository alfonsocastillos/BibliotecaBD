/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Estado
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class EstadoDAO extends Conexion {
    
    // Crear un estado en un pais
    public int SaveEstado(Object[] estado) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id        
            sentenciaSQL =  "SELECT PAIS_ID || LPAD(COALESCE(MAX(TO_NUMBER(SUBSTR(ESTADO_ID, 3, 2))), 0) + 1, 2, '0') " +
                            "FROM PAIS " +
                            "LEFT JOIN ESTADO USING (PAIS_ID) " +
                            "WHERE PAIS_ID = ? " +
                            "GROUP BY  PAIS_ID";
            ps = conn.prepareStatement(sentenciaSQL);     
            ps.setString(1, estado[1].toString()); // Id del pais
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO ESTADO VALUES(?, ?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, Integer.valueOf(id));      // Id (calculado)
            ps.setString(2, estado[0].toString());  // Estado (provisto)   
            ps.setInt(3, (int) estado[1]);          // Id Pais (provisto)   
            ps.executeUpdate();
            return Integer.valueOf(id);
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveEstado");
            return 0;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un estado
    public int UpdateEstado(Object[] estado)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE ESTADO SET " +
                          "ESTADO = ? " +
                          "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, estado[1].toString());  // Nuevo nombre estado
            ps.setInt(2, (int) estado[0]);          // Id del estado
            ps.executeUpdate();
            return (int) estado[0];                 // Regresa el id del estado
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateEstado");
            return 0;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los estados de un pais
    public Object [][] GetAllEstadosByPais(int pais_id){
       conectar();
       Object [][] estados;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL =   "SELECT COUNT(*) FROM ESTADO " +
                            "WHERE PAIS_ID = ?";            // Numero de estados en el pais
           ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
           ps.setInt(1, pais_id);
           rs = ps.executeQuery();                          // Resultado de la consulta
           
           // Numero de idiomas (COUNT)
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Arreglo de todos los estados del pais (ID, estado)
           estados = new Object[count][2];           
           sentenciaSQL  = "SELECT ESTADO_ID, ESTADO FROM ESTADO WHERE PAIS_ID = ? ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setInt(1, pais_id);
           rs = ps.executeQuery();
           
           // Agregar a todos los estados al arreglo (migrar de rs a actores)
           while (rs.next()){
               estados[i][0]=(rs.getString(1));
               estados[i][1]=(rs.getString(2));  
               i++;
            }           
           return estados;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllEstadosByPais");
            return null;
        }
        finally{
           desconectar();           
        }
    }  

    // Consulta el estado para un id
    public Object[] GetEstadoById(int estado_id)
    {
        conectar();
        Object [] estado = new Object[3];
       
        try{
                    
            sentenciaSQL  = "SELECT ESTADO_ID, ESTADO, PAIS_ID FROM ESTADO WHERE ESTADO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, estado_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo estado con el resultado
            while (rs.next()){
                estado[0] = (rs.getString(1));
                estado[1] = (rs.getString(2));  
                estado[2] = (rs.getString(2));  
            }           
            return estado;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEstadoById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
           
    // Consultar estados con nombre parecido
    public Object [][] GetEstadosByNombre(int pais_id, String estado){
       conectar();
       Object [][] estados;
       int i = 0;
       int count = 0;
       estado = "%" + estado + "%";
       try{
            // Cuenta todos los actores que tengan un nombre parecido
            sentenciaSQL =  "SELECT COUNT(ESTADO_ID) " +
                            "FROM ESTADO " +
                            "WHERE UPPER (ESTADO) LIKE UPPER(?) ";   // Todos los estados que tengan un nombre parecido
            if(pais_id > 0) {
               sentenciaSQL += "AND PAIS_ID = ?";               
            }
                                        
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, estado);
            if(pais_id > 0) {
               ps.setInt(2, pais_id);
            }
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
            if (rs.next()){
               count = rs.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo
            estados = new Object[count][2];
            sentenciaSQL =   "SELECT ESTADO_ID, ESTADO " +
                             "FROM ESTADO " +
                             "WHERE UPPER (ESTADO) LIKE UPPER(?)";
            if(pais_id > 0) {
               sentenciaSQL += "AND PAIS_ID = ?";               
            }
            sentenciaSQL += "ORDER BY 2";
            
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado);
            if(pais_id > 0) {
               ps.setInt(2, pais_id);
            }
            rs = ps.executeQuery();

            while (rs.next()){
                estados[i][0] = (rs.getInt(1));
                estados[i][1] = (rs.getString(2));
                i++;
            }           
            return estados;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEstadosByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Eliminar un estado
    public int DeleteEstado(int estado_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL el estado de cualquier direccion con el estado a borrar
            sentenciaSQL =  "UPDATE DIRECCION " +
                            "SET ESTADO_ID = NULL " +
                            "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, estado_id);
            ps.executeUpdate();
            
            // Borrar el estado
            sentenciaSQL = "DELETE FROM ESTADO " +
                            "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, estado_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteEstado");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}
