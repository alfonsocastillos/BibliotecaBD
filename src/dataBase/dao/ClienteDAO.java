/*
   Operaciones a la base de datos en la tabla cliente
*/
package dataBase.dao;

import dataBase.Conexion;
import dataBase.ConfigDataBase;
import java.sql.*;

public class ClienteDAO extends Conexion {
     
    public Object [][] getCustomers(){
        // se conecta a la base de datos
        conectar();
        // crea un arreglo de beans para guardar los datos
        Object[][] customers; 
        // Contador
        int i = 0;
        int cant = 0;
        
        try{
            sentenciaSQL = "SELECT COUNT(*) FROM CLIENTE";
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();

            if (rs.next()){
                cant = rs.getInt(1);
            }

            customers = new Object[cant][4];
            sentenciaSQL  = "SELECT cliente_id, nombre, apellido_pat, apellido_mat FROM CLIENTE";           
            ps = conn.prepareStatement(sentenciaSQL);
            rs = ps.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos
            // al arreglo de beans
            while (rs.next()){
                customers[i][0]=(rs.getString(1));
                customers[i][1]=(rs.getString(2));  
                customers[i][2]=(rs.getString(3)); 
                customers[i][3]=(rs.getString(4));
                i++;
            }           
            return customers;
        }
        catch (SQLException ex){
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + 
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getCustomers");
            return null;
        }
        finally{
            desconectar();           
        }
    }
    
    // Método para generar un nuevo ID
    public String generateId(String tableName, String idColumnName) {
        try {
            conectar();
            String id = "";
            String query = "SELECT LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || " +
                           "LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0') || " +
                           "LPAD(NVL(MAX(TO_NUMBER(SUBSTR(" + idColumnName + ", 5, 3))) + 1, 1), 3, '0') " +
                           "FROM " + tableName +
                           " WHERE SUBSTR(" + idColumnName + ", 1, 4) = " +
                           "LPAD(SUBSTR(EXTRACT(YEAR FROM SYSDATE), 3, 2), 2, '0') || " +
                           "LPAD(EXTRACT(MONTH FROM SYSDATE), 2, '0')";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getString(1);
            }

            return id;
        } catch (SQLException ex) {
            // Manejar la excepción según tus necesidades
            System.out.println("Error en generateId: " + ex.getMessage());
            return null;
        } finally {
            desconectar();
        }
    }
    
    // Método para guardar un usuario
    public String saveUsuario(Object[] usuario) {
        conectar();
        try {
            String id = generateId("CLIENTE", "CLIENTE_ID"); // Generar nuevo ID
            String sentenciaSQL = "INSERT INTO CLIENTE VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sentenciaSQL);
            
            ps.setString(1, id);                  // Id (calculado)
            ps.setString(2, usuario[0].toString());// Nombre
            ps.setString(3, usuario[1].toString());// Apellido Paterno
            ps.setString(4, usuario[2].toString());// Apellido Materno
            ps.setString(5, usuario[3].toString());// Correo
            ps.setString(6, usuario[4].toString());// Id de dirección (provisto)
            ps.setString(7, usuario[5].toString());// Id de escolaridad (provisto)
            ps.setString(8, usuario[6].toString());// Id de credencial (provisto)
            
            ps.executeUpdate();
            return id;
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getSQLState() + "\n\n" + ex.getMessage() +
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveUsuario");
            return null;
        } finally {
            desconectar();
        }
    }

    // Método para actualizar un usuario
    public String updateUsuario(Object[] usuario) {
        conectar();
        try {
            String sentenciaSQL = "UPDATE CLIENTE SET " +
                    "NOMBRE = ?, " +
                    "APELLIDO_PAT = ?, " +
                    "APELLIDO_MAT = ?, " +
                    "CORREO = ?, " +
                    "DIRECCION_ID = ?, " +
                    "ESCOLARIDAD_ID = ?, " +
                    "CREDENCIAL_ID = ? " +
                    "WHERE CLIENTE_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sentenciaSQL);

            ps.setString(1, usuario[0].toString());  // Nuevo nombre
            ps.setString(2, usuario[1].toString());  // Nuevo apellido paterno
            ps.setString(3, usuario[2].toString());  // Nuevo apellido materno
            ps.setString(4, usuario[3].toString());  // Nuevo correo
            ps.setString(5, usuario[4].toString());  // Nuevo id de dirección
            ps.setString(6, usuario[5].toString());  // Nuevo id de escolaridad
            ps.setString(7, usuario[6].toString());  // Nuevo id de credencial
            ps.setString(8, usuario[7].toString());  // Id del usuario a modificar

            ps.executeUpdate();
            return usuario[7].toString();  // Regresa el id del usuario
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getSQLState() + "\n\n" + ex.getMessage() +
                    "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateUsuario");
            return null;
        } finally {
            desconectar();
        }
    }

    // Método para consultar todas las direcciones de un usuario por estado
    public Object[][] getAllDireccionesByUsuario(String usuario_id) {
        conectar();
        Object[][] dirs;
        int i = 0;
        int count = 0;
        try {
            String sentenciaSQL = "SELECT COUNT(*) FROM CLIENTE WHERE CLIENTE_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, usuario_id);
            ResultSet rs = ps.executeQuery();

            // Verificar si el usuario existe
            if (rs.next()) {
                count = 1;  // El usuario existe, por lo que solo habrá una dirección
            }

            dirs = new Object[count][8];
            sentenciaSQL = "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, DIRECCION_ID, ESCOLARIDAD_ID " +
               "FROM CLIENTE WHERE CLIENTE_ID = ?";
            
            ps = conn.prepareStatement(sentenciaSQL);
            ps.setString(1, usuario_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                dirs[i][0] = rs.getString(1);  // Id del usuario
                dirs[i][1] = rs.getString(2);  // Nombre
                dirs[i][2] = rs.getString(3);  // Apellido Paterno
                dirs[i][3] = rs.getString(4);  // Apellido Materno
                dirs[i][4] = rs.getString(5);  // Correo
                dirs[i][5] = rs.getString(6);  // Id de dirección
                dirs[i][6] = rs.getString(7);  // Id de escolaridad
                dirs[i][7] = rs.getString(8);  // Id de credencial
                i++;
            }
            return dirs;
        } catch (SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY +
                    "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getAllDireccionesByUsuario");
            return null;
        } finally {
            desconectar();
        }
    }
}

                     