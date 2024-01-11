/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Pais
 */
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 *
 * @author alfonso
 */

public class CredencialDAO extends Conexion {
    
    // Crear una Credencial
    // Modificar el método SaveCredencial para aceptar la fecha como parámetro
    public int SaveCredencial(String fechaRenovacion) {
        // Se conecta a la base de datos
        conectar();
        try {
            int id = 0;
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(CREDENCIAL_ID) + 1 FROM CREDENCIAL";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();

            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }

            // inserta mandando todos los datos, incluido el nuevo id y la fecha proporcionada
            sentenciaSQL = "INSERT INTO CREDENCIAL VALUES(?, TO_DATE(?, 'DD/MM/YY'))";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, id);              // Id (calculado)
            ps.setString(2, fechaRenovacion);  // Fecha de Renovación
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SaveCredencial");
            return 0;
        }
        finally{
           desconectar();
        }
    }


    
    // Modificar una Credencial (id, fecha de renovacion)
    public int UpdateCredencial(Object[] credencial_obj) {
        conectar();
        try {
            sentenciaSQL = "UPDATE CREDENCIAL SET FECHA_RENOVACION = TO_DATE(?, 'DD/MM/YY') WHERE CREDENCIAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, credencial_obj[1].toString());
            ps.setInt(2, (Integer) credencial_obj[0]);
            ps.executeUpdate();
            return (Integer) credencial_obj[0];
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getSQLState() + "\n\n" + ex.getMessage() +
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateCredencial");
            return 0;
        } finally {
            desconectar();
        }
    }


    
    // Consultar todos las credenciales (id, fecha_renovacion)
    public Object [][] GetAllCredencial(){
        conectar();
        Object [][] paises;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM CREDENCIAL";      // Numero de paises           
            ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
            rs = ps.executeQuery();                          // Resultado de la consulta

            // Numero de idiomas (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los paises (ID, pais)
            paises = new Object[count][2];           
            sentenciaSQL  = "SELECT CREDENCIAL_ID FROM CREDENCIAL ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();

            // Agregar a todos los paises al arreglo (migrar de rs a paises)
            while (rs.next()){
                paises[i][0] = (rs.getInt(1));      // Id del pais
                paises[i][1] = (rs.getString(2));   // Nombre del pais
                i++;
            }           
            return paises;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllCredenciales");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consulta el pais para un id (id, pais)
    public Object[] GetCredencialById(int pais_id)
    {
        conectar();
        Object [] pais = new Object[2];
       
        try{                    
            sentenciaSQL  = "SELECT PAIS_ID, PAIS FROM PAIS WHERE PAIS_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, pais_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo pais con el resultado
            while (rs.next()){
                pais[0] = (rs.getInt(1));
                pais[1] = (rs.getString(2));  
            }           
            return pais;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetPaisById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Consulta el pais de un libro
    public Object[] GetCredencialByCliente(int id_libro){
        // Conecta a la base de datos
        conectar();
        Object[] pais = new Object[2];
        // Contador
        try{
            sentenciaSQL  = "SELECT CREDENCIAL_ID" +
                            "FROM CLIENTE " +
                            "JOIN CREDENCIAL USING (CREDENCIAL_ID) " +
                            "WHERE CLIENTE_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, id_libro);
            rs = ps.executeQuery();

            // Recorre el result set para obtener los datos y asignarlos al arreglo
            while (rs.next()){
                pais[0] = rs.getInt(1);     // Id del pais
                pais[1] = rs.getInt(2);     // Pais
            }     
            return pais;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetCredencialByCliente");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar todos los libros de un pais
    public Object[][] GetClientesByCredencial(int pais_id){
       conectar();
       Object[][] libros;
       int i = 0;
       int count = 0;
       try{
           // Primero se cuenta el numero de autores en el libro
           sentenciaSQL =   "SELECT COUNT (CREDENCIAL_ID) " +
                            "FROM CREDENCIAL " +
                            "JOIN CLIENTE USING (CREDENCIAL_ID) " +
                            "WHERE CREDENCIAL_ID = ? ";

            ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
            ps.setInt(1, pais_id);
            rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set    
           
           if (rs.next()){
               count = rs.getInt(1);
            }
           
           libros = new Object[count][2];           
           sentenciaSQL  =  "SELECT CLIENTE_ID, NOMBRE ||''|| APELLIDO_PAT AS NOMBRE_CLIENTE " +
                            "FROM CLIENTE " +
                            "WHERE CREDENCIAL_ID = ? " +
                            "ORDER BY 2";   // Ordenar por la segunda columna     
           ps = conn.prepareStatement(sentenciaSQL);
           ps.setInt(1, pais_id);
           rs = ps.executeQuery();
           while (rs.next()){
               libros[i][0] = (rs.getInt(1));       // Id del libro
               libros[i][1] = (rs.getString(2));    // Titulos del libro
               i++;
           }           
           return libros;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetClientesByCredencial");
            return null;
        }
        finally{
           desconectar();           
        }
    }
    
    // Consultar paises con nombre parecido (id, pais)
    public Object[][] GetCredencialesByNombre(String fechaRenovacion){
    conectar();
    Object[][] credenciales;
    int i = 0;
    int count = 0;
    fechaRenovacion = "%" + fechaRenovacion + "%";
        try{
            // Cuenta todas las credenciales que tengan una fecha de renovación parecida
            sentenciaSQL =  "SELECT COUNT(CREDENCIAL_ID) " +
                            "FROM CREDENCIAL " +
                            "WHERE UPPER(FECHA_RENOVACION) LIKE UPPER(?)";

             ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
             ps.setString(1, fechaRenovacion);
             rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         

            if (rs.next()){
                count = rs.getInt(1);
            }

            // Misma consulta, pero ahora puede ser guardada en el arreglo
            credenciales = new Object[count][2];           
            sentenciaSQL =  "SELECT CREDENCIAL_ID, FECHA_RENOVACION " +
                            "FROM CREDENCIAL " +
                            "WHERE UPPER(FECHA_RENOVACION) LIKE UPPER(?) " +
                            "ORDER BY FECHA_RENOVACION";

            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, fechaRenovacion);
            rs = ps.executeQuery();

            while (rs.next()){
               credenciales[i][0] = (rs.getInt(1));
               credenciales[i][1] = (rs.getDate(2));  // Cambié getString a getDate para obtener directamente el objeto Date
               i++;
           }           
           return credenciales;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetCredencialesByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }


    // Eliminar un pais
    public int DeleteCredenciales(int pais_id){
        // Conecta a la base de datos
        conectar();
        try{            
            // Borrar el pais
            sentenciaSQL = "DELETE FROM CREDENCIAL " +
                            "WHERE CREDENCIAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, pais_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteCredenciales");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}

