/*
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla sucursal
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class SucursalDAO extends Conexion{

    public Object[][] getSucursal(){
       conectar();
       Object[][] Sucursales;
       int i = 0;
       int cant = 0;
       try{
           sentenciaSQL = "SELECT COUNT(*) FROM Sucursal";
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           
           if (rs.next()){
               cant = rs.getInt(1);
           }
           
           Sucursales = new Object [cant][10];
           
           sentenciaSQL  = "SELECT SUCURSAL_ID, NOMBRE, LADA, TELEFONO,EXTENCION,PAGINA_WEB,HORA_APERTURA,HORA_CIERRE, CORREO, DIRECCION_ID" +
                            "FROM SUCURSAL, DIRECCION, ESTADO " +
                            "WHERE SUCURSAL_DIRECCION_ID = DIECCION_ID " +
                            "AND SUCURSAL_ESTADO_ID = ESTADO_ID";           
           ps = conn.prepareStatement(sentenciaSQL);
           rs = ps.executeQuery();
           while (rs.next()){
               Sucursales[i][0]= (rs.getInt(1));
               Sucursales[i][1]=(rs.getString(2));
               if (rs.getString(3) == null)
                   Sucursales[i][2]=("");
               else
                   Sucursales[i][2]=(rs.getString(3));
               Sucursales[i][3]=(rs.getString(4));
               Sucursales[i][4]=(rs.getString(5));
               Sucursales[i][5]=(rs.getString(6));
               Sucursales[i][6]=(rs.getString(7));
               Sucursales[i][7]=(rs.getString(8)); 
               Sucursales[i][7]=(rs.getString(9));
               Sucursales[i][7]=(rs.getString(10)); 
               i++;
           }           
           return Sucursales;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getSucursales");
            return null;
        }
       finally{
           desconectar();           
       }
    }  
    
        // Modificar una sucursal
    public String UpdateSucursal(Object[] Suc)
    {       
        conectar();   // se conecta a la base de datos
        try{
           // actualiza los datos
           sentenciaSQL =   "UPDATE SUCURSAL SET " +
                            "NOMBRE = ?, " +
                            "LADA = ?, " +
                            "TELEFONO = ?, " +
                            "PAGINA_WEB = ?, " +
                            "HORA_APERTURA= ?, " +
                            "HORA_CIERRE = ?, " +
                            "CORREO = ?, " +
                            "DIRECCION_ID = ?, " +
                            "WHERE SUCUSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, Suc[1].toString());     // nombre
            ps.setString(2, Suc[2].toString());     // lada
            ps.setString(3, Suc[3].toString());     // telefono
            ps.setString(4, Suc[4].toString());     // Nueva  pagina web
            ps.setString(5, Suc[5].toString());     // Nuevo apertura
            ps.setString(6, Suc[6].toString());     // Nuevo cierre
            ps.setString(7, Suc[7].toString());     // Nuevo correo
            ps.setString(8, Suc[8].toString());     // Nuevo direccion id
            ps.setString(9, Suc[0].toString());     // Id de la sucursal a modificar
            ps.executeUpdate();
            return Suc[0].toString();               // Regresa el id de la sucursal
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "UpdateSucursal");
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

    
    /// falta modificar esta parte 
    
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
