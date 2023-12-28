/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Escolaridad
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class EscolaridadDAO extends Conexion {
    
    // Crear un nivel escolar
    public String SaveEscolaridad(String nivel)
    {
        // Se conecta a la base de datos
        conectar();
        try{
            String id = "";
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(ESCOLARIDAD_ID) + 1 FROM ESCOLARIDAD";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getString(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO ESCOLARIDAD VALUES(?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setString(1, id);                    // Id (calculado)
            ps.setString(2, nivel);                 // Nivel (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveEscolaridad");
            return null;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar una escolaridad
    public String UpdateEscolaridad(Object[] escolaridad)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE ESCOLARIDAD SET " +
                          "NIVEL = ?, " +
                          "WHERE ESCOLARIDAD_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, escolaridad[1].toString());     // Nuevo nombre nivel
            ps.setString(2, escolaridad[0].toString());     // Id de la escolaridad
            ps.executeUpdate();
            return escolaridad[0].toString();               // Regresa el id de la escolaridad
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateEscolaridad");
            return null;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos las escolaridades
    public Object [][] GetAllEscolaridades(){
       conectar();
       Object [][] escolaridades;
       int i = 0;
       int count = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM ESCOLARIDAD";       // Numero de escolaridades
           ps = conn.prepareStatement(sentenciaSQL);                // Convierte el str a un sentencia utilizable en SQL           
           rs = ps.executeQuery();                                  // Resultado de la consulta
           
           // Numero de idiomas (COUNT)
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Arreglo de todos las escolaridades (ID, nivel)
           escolaridades = new Object[count][2];           
           sentenciaSQL  = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD ORDER BY 2";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           // Agregar a todos las escolaridades al arreglo (migrar de rs a actores)
           while (rs.next()){
               escolaridades[i][0]=(rs.getString(1));
               escolaridades[i][1]=(rs.getString(2));  
               i++;
            }           
           return escolaridades;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllEscolaridades");
            return null;
        }
       finally{
           desconectar();           
       }
    }  

    // Consulta el nivel para un id
    public Object[] GetEscolaridadById(String escolaridad_id)
    {
        conectar();
        Object [] escolaridad = new Object[2];
       
        try{
                    
            sentenciaSQL  = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD WHERE ESCOLARIDAD_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, escolaridad_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo nivel con el resultado
            while (rs.next()){
                escolaridad[0] = (rs.getString(1));
                escolaridad[1] = (rs.getString(2));  
            }           
            return escolaridad;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEscolaridadById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Consulta el nivel escolar de un cliente
    public String GetEscolaridadByCliente(String id_cliente){
        // Conecta a la base de datos
       conectar();
       String nivel = "";
       // Contador
       try{
           sentenciaSQL  =  "SELECT NIVEL " +
                            "FROM CLIENTE " +
                            "JOIN ESCOLARIDAD USING (ESCOLARIDAD_ID) " +
                            "WHERE CLIENTE_ID = ?";           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, id_cliente);
           rs = ps.executeQuery();
              
           // Recorre el result set para obtener los datos y asignarlos al arreglo
           while (rs.next()){
               nivel = rs.getString(1);
           }     
           return nivel;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEscolaridadByCliente");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar todos los clientes con un nivel escolar
    public Object [][] GetClientesByEscolaridad(String escolaridad_id){
       conectar();
       Object [][] clientes;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (ESCOLARIDAD_ID) " +
                            "FROM ESCOLARIDAD " +
                            "JOIN CLIENTE USING (ESCOLARIDAD_ID) " +
                            "WHERE ESCOLARIDAD_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, escolaridad_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           clientes = new Object[count][3];           
           sentenciaSQL  =  "SELECT CLIENTE_ID, NOMBRE || ' ' || APELLIDO_PAT || ' ' || APELLIDO_MAT, CREDENCIAL_ID " +
                            "FROM CLIENTE " +
                            "WHERE ESCOLARIDAD_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, escolaridad_id);
           rs = ps.executeQuery();
           while (rs.next()){
               clientes[i][0] = (rs.getString(1));  // Id del cliente
               clientes[i][1] = (rs.getString(1));  // Nombre del cliente
               clientes[i][2] = (rs.getString(1));  // Credencial del cliente
               i++;
           }           
           return clientes;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetClientesByEscolaridad");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar escolaridades con nombre parecido
    public Object [] GetEscolaridadByNombre(String nivel){
       conectar();
       Object [] escolaridades;
       int i = 0;
       int count = 0;
       nivel = "%" + nivel + "%";
       try{
           // Cuenta todos los actores que tengan un nombre parecido
           sentenciaSQL =   "SELECT COUNT(ESCOLARIDAD_ID) " +
                            "FROM ESCOLARIDAD " +
                            "WHERE UPPER (NIVEL) LIKE UPPER(?)";   // Todos los niveles que tengan un nombre parecido
                            
            
            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setString(1, nivel);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         
           
           if (rs.next()){
               count = rs.getInt(1);
           }
           
           // Misma consulta, pero ahora puede ser guardada en el arreglo
           escolaridades = new Object[count];           
           sentenciaSQL =   "SELECT PAIS " +
                            "FROM PAIS" +
                            "WHERE UPPER (PAIS) LIKE UPPER(?)";
           
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setString(1, nivel);
           rs = ps.executeQuery();
           
           while (rs.next()){
               escolaridades[i] = (rs.getString(1));
               i++;
           }           
           return escolaridades;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEscolaridadByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Eliminar un nivel escolar
    public int DeleteEscolaridad(String escolaridad_id){
        // Conecta a la base de datos
        conectar();
        try{
            // Cambiar a NULL el nivel de cualquier cliente con el nivel a borrar
            sentenciaSQL =  "UPDATE LIBRO " +
                            "SET ESCOLARIDAD_ID = NULL " +
                            "WHERE ESCOLARIDAD_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, escolaridad_id);
            ps.executeUpdate();
            
            // Borrar el nivel
            sentenciaSQL = "DELETE FROM ESCOLARIDAD " +
                            "WHERE ESCOLARIDAD_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, escolaridad_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteEscolaridad");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}
