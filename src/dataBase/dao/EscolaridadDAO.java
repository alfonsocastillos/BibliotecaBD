/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Escolaridad
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class EscolaridadDAO extends Conexion {
    
    // Crear una Escolaridad
    public int SaveEscolaridad(String escolaridad) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            int id = 0;
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(ESCOLARIDAD_ID) + 1 FROM ESCOLARIDAD";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO ESCOLARIDAD VALUES(?, ?)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, id);       // Id (calculado)
            ps.setString(2, escolaridad);  // Pais (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveEscolaridad");
            return 0;
        }
        finally{
           desconectar();
       }

    }
    
     // Modificar una escolaridad (id, escolaridad)
    public int UpdateEscolaridad(Object[] escolaridad)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE ESCOLARIDAD SET " +
                          "NIVEL = ?" +
                          "WHERE ESCOLARIDAD_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, escolaridad[1].toString());    // Nuevo nombre Escolaridad
            ps.setInt(2, (Integer) escolaridad[0]);        // Id del Escolaridad
            ps.executeUpdate();
            return (Integer) escolaridad[0];               // Regresa el id Escolaridad
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateEscolaridad");
            return 0;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos las escolaridades (id, escolaridad)
    public Object [][] GetAllEscolaridad(){
        conectar();
        Object [][] escolaridad;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM ESCOLARIDAD";      // Numero de paises           
            ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
            rs = ps.executeQuery();                          // Resultado de la consulta

            // Numero de idiomas (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los paises (ID, pais)
            escolaridad = new Object[count][2];           
            sentenciaSQL  = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();

            // Agregar a todos los paises al arreglo (migrar de rs a paises)
            while (rs.next()){
                escolaridad[i][0] = (rs.getInt(1));      // Id de la ESCOLARIDAD
                escolaridad[i][1] = (rs.getString(2));   // Nombre de la ESCOLARIDAD
                i++;
            }           
            return escolaridad;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllEscolaridad");
            return null;
        }
        finally{
           desconectar();           
       }
    }
    
    
    // Consulta la escolaridad para un id (id, nivel)
    public Object[] GetEscolaridadById(int escolaridad_id)
    {
        conectar();
        Object [] escolaridad = new Object[2];
       
        try{                    
            sentenciaSQL  = "SELECT ESCOLARIDAD_ID, NIVEL FROM ESCOLARIDAD WHERE ESCOLARIDAD_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, escolaridad_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo pais con el resultado
            while (rs.next()){
                escolaridad[0] = (rs.getInt(1));
                escolaridad[1] = (rs.getString(2));  
            }           
            return escolaridad;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEscolariadById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    
    // Consulta la escolariad de un cliente
    public Object[] GetEscolariadByCliente(int id_cliente){
        // Conecta a la base de datos
        conectar();
        Object[] escolaridad = new Object[2];
        // Contador
        try{
            sentenciaSQL  = "SELECT ESCOLARIDAD_ID, NIVEL " +
                            "FROM CLIENTE " +
                            "JOIN ESCOLARIDAD USING (ESCOLARIDAD_ID) " +
                            "WHERE CLIENTE_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, id_cliente);
            rs = ps.executeQuery();

            // Recorre el result set para obtener los datos y asignarlos al arreglo
            while (rs.next()){
                escolaridad[0] = rs.getInt(1);     // Id de la escolaridad
                escolaridad[1] = rs.getInt(2);     // escolaridad
            }     
            return escolaridad;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetEscolariadByCliente");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    
    // Consultar todos los clientes de una escolariad
    public Object[][] GetClientesByEscolariad(int escolaridad_id){
       conectar();
       Object[][] clientes;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero
           sentenciaSQL =   "SELECT COUNT (ESCOLARIDAD_ID) " +
                            "FROM ESCOLARIDAD " +
                            "JOIN CLIENTE USING (ESCOLARIDAD_ID) " +
                            "WHERE ESCOLARIDAD_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setInt(1, escolaridad_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           clientes = new Object[count][2];           
           sentenciaSQL  =  "SELECT CLIENTE_ID, NOMBRE " +
                            "FROM CLIENTE " +
                            "WHERE ESCOLARIDAD_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setInt(1, escolaridad_id);
           rs = ps.executeQuery();
           while (rs.next()){
               clientes[i][0] = (rs.getInt(1));       // Id del libro
               clientes[i][1] = (rs.getString(2));    // Titulos del libro
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
    
    
     // Consultar paises con nombre parecido (id, pais)
    public Object[][] GetEscolaridadByNombre(String escolaridad){
       conectar();
       Object[][] escolaridades;
       int i = 0;
       int count = 0;
       escolaridad = "%" + escolaridad + "%";
       try{
            // Cuenta todos los actores que tengan un nombre parecido
            sentenciaSQL =  "SELECT COUNT(ESCOLARIDAD_ID) " +
                            "FROM ESCOLARIDAD " +
                            "WHERE UPPER (NIVEL) LIKE UPPER(?)";   // Todos los paises que tengan un nombre parecido


             ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
             ps.setString(1, escolaridad);
             rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         

            if (rs.next()){
                count = rs.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo
            escolaridades = new Object[count][2];           
            sentenciaSQL =  "SELECT ESCOLARIDAD_ID, NIVEL " +
                            "FROM ESCOLARIDAD " +
                            "WHERE UPPER (NIVEL) LIKE UPPER(?) " +
                            "ORDER BY NIVEL";

            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, escolaridad);
            rs = ps.executeQuery();
           
            while (rs.next()){
               escolaridades[i][0] = (rs.getInt(1));
               escolaridades[i][1] = (rs.getString(2));
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
    
    
    
    // Eliminar una escolaridad
    public int DeleteEscolaridad(int escolaridad_id){
        // Conecta a la base de datos
        conectar();
        try{            
            // Borrar el pais
            sentenciaSQL = "DELETE FROM ESCOLARIDAD " +
                            "WHERE ESCOLARIDAD_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, escolaridad_id);
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
