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

public class PaisDAO extends Conexion {    
    // Crear un pais
    public int SavePais(String pais) 
    {
        // Se conecta a la base de datos
        conectar();
        try{
            int id = 0;
            // genera el nuevo id            
            sentenciaSQL =  "SELECT MAX(PAIS_ID) + 1 FROM PAIS";
            ps = conn.prepareStatement(sentenciaSQL);            
            rs = ps.executeQuery();
            
            // guarda el nuevo id
            if (rs.next()){
                id = rs.getInt(1);
            }
            
            // inserta mandando todos los datos, incluido en nuevo id
            sentenciaSQL = "INSERT INTO PAIS VALUES(?, ?, SYSDATE)";
            ps = conn.prepareStatement(sentenciaSQL);
            // Asigna los valores del arreglo            
            ps.setInt(1, id);       // Id (calculado)
            ps.setString(2, pais);  // Pais (provisto)            
            ps.executeUpdate();
            return id;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "SavePais");
            return 0;
        }
        finally{
           desconectar();
       }

    }
    
    // Modificar un pais (id, pais)
    public int UpdatePais(Object[] pais)
    {       
        // se conecta a la base de datos
       conectar();
       try{
           // actualiza los datos
           sentenciaSQL = "UPDATE PAIS SET " +
                          "PAIS = ?, " +
                          "LAST_UPDATE = SYSDATE " +
                          "WHERE PAIS_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, pais[1].toString());    // Nuevo nombre pais
            ps.setInt(2, (Integer) pais[0]);        // Id del pais
            ps.executeUpdate();
            return (Integer) pais[0];               // Regresa el id del pais
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdatePais");
            return 0;
        }
        finally{
           desconectar();
       }
    }
    
    // Consultar todos los paises (id, pais)
    public Object [][] GetAllPaises(){
        conectar();
        Object [][] paises;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM PAIS";      // Numero de paises           
            ps = conn.prepareStatement(sentenciaSQL);        // Convierte el str a un sentencia utilizable en SQL           
            rs = ps.executeQuery();                          // Resultado de la consulta

            // Numero de idiomas (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todos los paises (ID, pais)
            paises = new Object[count][2];           
            sentenciaSQL  = "SELECT PAIS_ID, PAIS FROM PAIS ORDER BY 2";           
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
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllPaises");
            return null;
        }
        finally{
           desconectar();           
       }
    }  

    // Consulta el pais para un id (id, pais)
    public Object[] GetPaisById(int pais_id)
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
        
    // Consultar paises con nombre parecido (id, pais)
    public Object[][] GetPaisesByNombre(String pais){
       conectar();
       Object[][] paises;
       int i = 0;
       int count = 0;
       pais = "%" + pais + "%";
       try{
            // Cuenta todos los actores que tengan un nombre parecido
            sentenciaSQL =  "SELECT COUNT(PAIS_ID) " +
                            "FROM PAIS " +
                            "WHERE UPPER (PAIS) LIKE UPPER(?)";   // Todos los paises que tengan un nombre parecido


             ps = conn.prepareStatement(sentenciaSQL);   // prepara la sentencia 
             ps.setString(1, pais);
             rs = ps.executeQuery();                     // Ejecuta la sentencia y la asigna al result set         

            if (rs.next()){
                count = rs.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo
            paises = new Object[count][2];           
            sentenciaSQL =  "SELECT PAIS_ID, PAIS " +
                            "FROM PAIS " +
                            "WHERE UPPER (PAIS) LIKE UPPER(?) " +
                            "ORDER BY PAIS";

            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, pais);
            rs = ps.executeQuery();
           
            while (rs.next()){
               paises[i][0] = (rs.getInt(1));
               paises[i][1] = (rs.getString(2));
               i++;
           }           
           return paises;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetPaisesByNombre");
            return null;
        }
        finally{
           desconectar();           
       }
    }

    // Eliminar un pais
    public int DeletePais(int pais_id){
        // Conecta a la base de datos
        conectar();
        try{            
            // Borrar el pais
            sentenciaSQL = "DELETE FROM PAIS " +
                            "WHERE PAIS_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setInt(1, pais_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeletePais");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }
}
