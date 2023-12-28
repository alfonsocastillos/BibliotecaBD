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
    public String SaveEstado(Object[] estado) 
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
                            "GROUP BY  PAIS_ID;";
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
            ps.setString(1, id);                    // Id (calculado)
            ps.setString(2, estado[0].toString());  // Estado (provisto)   
            ps.setString(2, estado[1].toString());  // Id Pais (provisto)   
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveEstado");
            return null;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un estado
    public String UpdateEstado(Object[] estado)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE ESTADO SET " +
                          "ESTADO = ?, " +
                          "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, estado[1].toString());  // Nuevo nombre estado
            ps.setString(2, estado[0].toString());  // Id del estado
            ps.executeUpdate();
            return estado[0].toString();            // Regresa el id del estado
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateEstado");
            return null;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los estados de un pais
    public Object [][] GetAllEstadosByPais(String pais_id){
       conectar();
       Object [][] estados;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL =   "SELECT COUNT(*) FROM ESTADO " +
                            "WHERE PAIS_ID = ?";            // Numero de estados en el pais
           ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
           ps.setString(1, pais_id);
           rs = ps.executeQuery();                          // Resultado de la consulta
           
           // Numero de idiomas (COUNT)
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Arreglo de todos los estados del pais (ID, estado)
           estados = new Object[count][2];           
           sentenciaSQL  = "SELECT ESTADO_ID, ESTADO FROM ESTADO WHERE PAIS_ID = ? ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, pais_id);
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
    public Object[] GetEstadoById(String estado_id)
    {
        conectar();
        Object [] estado = new Object[3];
       
        try{
                    
            sentenciaSQL  = "SELECT ESTADO_ID, ESTADO, PAIS_ID FROM ESTADO WHERE ESTADO_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
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
    
    // Consulta el estado de una direccion
    public String GetEstadoByDireccion(String id_dir){
        // Conecta a la base de datos
       conectar();
       String estado = "";
       // Contador
       try{
           sentenciaSQL  =  "SELECT ESTADO " +
                            "FROM DIRECCION " +
                            "JOIN ESTADO USING (ESTADO_ID) " +
                            "WHERE LIBRO_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id_dir);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos al arreglo
           while (rs.next()){
               estado = rs.getString(1);
           }     
           return estado;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEstadoByDireccion");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar todas las dir en un estado
    public Object [][] GetDireccionesByEstado(String estado_id){
       conectar();
       Object [][] dirs;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (DIRECCION_ID) " +
                            "FROM DIRECCION " +                            
                            "WHERE ESTADO_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, estado_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           dirs = new Object[count][2];           
           sentenciaSQL  =  "SELECT DIRECCION_ID, CALLE || ' ' || ALCALDIA " +
                            "FROM DIRECCION " +
                            "WHERE ESTADO_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, estado_id);
           rs = ps.executeQuery();
           while (rs.next()){
               dirs[i][0] = (rs.getString(1));  // Ids de direcciones
               dirs[i][1] = (rs.getString(2));  // Direcciones
               i++;
           }           
           return dirs;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetDireccionesByEstado");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar estados con nombre parecido
    public Object [][] GetEstadosByNombre(String estado){
       conectar();
       Object [][] estados;
       int i = 0;
       int count = 0;
       estado = "%" + estado + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(ESTADO_ID) " +
                            "FROM ESTADO " +
                            "WHERE UPPER (ESTADO) LIKE UPPER(?)";   // Todos los estados que tengan un nombre parecido
                            
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, estado);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           estados = new Object[count][2];
           sentenciaSQL =   "SELECT ESTADO_ID, ESTADO " +
                            "FROM ESTADO" +
                            "WHERE UPPER (ESTADO) LIKE UPPER(?)";
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, estado);
           rs = ps.executeQuery();
           
           while (rs.next()){
               estados[i][0] = (rs.getString(1));
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
    public int DeleteEstado(String estado_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL el estado de cualquier direccion con el estado a borrar
            sentenciaSQL =  "UPDATE DIRECCION " +
                            "SET ESTADO_ID = NULL " +
                            "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado_id);
            ps.executeUpdate();
            
            // Borrar el estado
            sentenciaSQL = "DELETE FROM ESTADO " +
                            "WHERE ESTADO_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
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
