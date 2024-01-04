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
                Sucursales[i][8]=(rs.getString(9));
                Sucursales[i][9]=(rs.getString(10)); 
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
                            "EXTENSION = ?, " +
                            "PAGINA_WEB = ?, " +
                            "HORA_APERTURA= ?, " +
                            "HORA_CIERRE = ?, " +
                            "CORREO = ?, " +
                            "DIRECCION_ID = ?, " +
                            "WHERE SURCUSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, Suc[1].toString());     // nuevo nombre
            ps.setString(2, Suc[2].toString());     // nueva lada
            ps.setString(3, Suc[3].toString());     // nuevo telefono
            ps.setString(3, Suc[4].toString());     // nueva extension
            ps.setString(4, Suc[5].toString());     // Nueva  pagina web
            ps.setString(5, Suc[6].toString());     // Nueva apertura
            ps.setString(6, Suc[7].toString());     // Nuevo cierre
            ps.setString(7, Suc[8].toString());     // Nuevo correo
            ps.setString(8, Suc[9].toString());     // Nuevo direccion id
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

    // Consultar todas las sucusales 
    public Object [][] GetAllSucursalessByEstado(String estado_id){
        conectar();
        Object [][] suc;
        int i = 0;
        int count = 0;
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM SUCURSALES WHERE ESTADO_ID = ?";     // Numero de sucursales en ese estado
            ps = conn.prepareStatement(sentenciaSQL);       // Convierte el str a un sentencia utilizable en SQL        
            ps.setString(1, estado_id);          // Nuevo numero exterior
            rs = ps.executeQuery();                            // Resultado de la consulta

            // Numero de sucursales en ese estado (COUNT)
            if (rs.next()){
                count = rs.getInt(1);
            }

            // Arreglo de todas las sucursales (ID, nombre, lada , telefono , extencion, pagina web, hora apertura, hora cierre, correo, direccion id)
            suc = new Object[count][10];           
            sentenciaSQL  = "SELECT SUCURSAL_ID, NOMBRE, LADA, TELEFONO, EXTENCION, PAGINA WEB, HORA_APERTURA, HORA_CIERRE, CORREO, DIRECCION_ID " +
                            "FROM SUCURSAL WHERE ESTADO_ID = ? ORDER BY 2";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, estado_id);
            rs = ps.executeQuery();

            // Agregar a todas las sucursales al arreglo (migrar de rs a suc)
            while (rs.next()){
                suc[i][0] = (rs.getString(1));     // Id
                suc[i][1] = (rs.getString(2));     // nombre
                suc[i][2] = (rs.getString(3));     // lada
                suc[i][3] = (rs.getString(4));     // telefon
                suc[i][4] = (rs.getString(5));     // Extencion
                suc[i][5] = (rs.getString(6));     // pagina web
                suc[i][6] = (rs.getString(7));     // hora apertura
                suc[i][7] = (rs.getString(8));     // hora cierre
                suc[i][8] = (rs.getString(9));     // correo
                suc[i][9] = (rs.getString(10));     // direccion id
                i++;
            }           
            return suc;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetAllSucursalesByEstado");
            return null;
        }
        finally{
           desconectar();           
       }
    }  
     
    // Consultar una sucursale por id
    public Object[] GetSucursalById(String sucursal_id)
    {
        conectar();
        Object [] sucursal = new Object[10];
       
        try{                    
            sentenciaSQL  = "SELECT SUCURSAL_ID, NOMBRE, LADA, TELEFONO, EXTENCION, PAGINA WEB, HORA_APERTURA, HORA_CIERRE, CORREO, DIRECCION_ID  FROM SUCURSAL " + 
                            "WHERE SUCURSAL_ID = ?";           
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, sucursal_id);    // Reemplaza el parámetro index (simbolo ?) con el str x
            rs = ps.executeQuery();

            // Llena el arreglo con el resultado
            while (rs.next()){
                sucursal[0] = (rs.getString(1));
                sucursal[1] = (rs.getString(2));  
                sucursal[2] = (rs.getString(3));
                sucursal[3] = (rs.getString(4));  
                sucursal[4] = (rs.getString(5));
                sucursal[5] = (rs.getString(6));  
                sucursal[6] = (rs.getString(7));
                sucursal[7] = (rs.getString(8));
                sucursal[8] = (rs.getString(9));
                sucursal[9] = (rs.getString(10));
            }           
            return sucursal;
        }
         catch (SQLException ex){
             System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                     "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "GetSucursalById");
             return null;
        }
        finally{
           desconectar();           
        }

    }
    
    // Borrar una sucursal
    public int DeleteSucursal(String sucursal_id){
        conectar();  // Conecta a la base de datos
        try{
            // Cambiar a NULL la sucursal de cualquier empleado/cliente/direccion  con la sucursal a borrar
            sentenciaSQL =  "UPDATE CLIENTE" +
                            "SET SUCRUSAL_ID = NULL " +
                            "WHERE SUCURSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, sucursal_id);
            ps.executeUpdate();
            
             sentenciaSQL =  "UPDATE EMPLEADO" +
                            "SET SUCURSAL_ID = NULL " +
                            "WHERE SUCURSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, sucursal_id);
            ps.executeUpdate();
            
            sentenciaSQL =  "UPDATE DIRECCION" +
                            "SET SUCURSAL_ID = NULL " +
                            "WHERE SUCURSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, sucursal_id);
            ps.executeUpdate();                   
            
            // Borrar el idioma
            sentenciaSQL = "DELETE FROM SUCURSAL " +
                            "WHERE SUCURSAL_ID = ?";
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, sucursal_id);
            int res = ps.executeUpdate();
            if (res == 1){
                return 0;
            }
            
            return 1;
        }
        catch (SQLException ex){
            System.out.println("Error " +  ex.getSQLState() + "\n\n" + ex.getMessage() + 
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "DeleteSucursal");
            if (ex.getErrorCode() ==  2292)
                return 1;
            return 2;
        }
        finally{
           desconectar();
       }
    }

}
