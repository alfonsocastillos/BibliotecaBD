package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Operaciones a la base de datos en la tabla cliente.
 * @author alfonso
 */
public class ClienteDAO extends CustomConnection {

    /**
     * Consulta la informacion para un cliente (Id, Nombre, Apellido Paterno, Apellido Materno, Correo, Pais, Estado, Alcaldia, CP, Calle, Num Exterior, Num Interior, Escolaridad).
     * @param clienteId Id del cliente a consultar.
     * @return 
     */
    public Object[] getClienteById(int clienteId) {
        connect();
        Object[] cliente = null;
        try {
            sentenciaSQL = "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, PAIS, ESTADO, ALCALDIA, CP, CALLE, EXTERIOR, INTERIOR, NIVEL FROM CLIENTE " +
                    "JOIN DIRECCION USING (DIRECCION_ID) JOIN ESTADO USING (ESTADO_ID) JOIN PAIS USING (PAIS_ID) JOIN ESCOLARIDAD USING(ESCOLARIDAD_ID) WHERE CLIENTE_ID = ? ";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, clienteId);
            resultSet = preparedStatement.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos al arreglo.
            cliente = new Object[13];
            while(resultSet.next()) {
                cliente[0] = resultSet.getInt(1);       // Id.
                cliente[1] = resultSet.getString(2);    // Nombre.
                cliente[2] = resultSet.getString(3);    // Apellido Paterno.
                cliente[3] = resultSet.getString(4);    // Apellido Materno.
                cliente[4] = resultSet.getString(5);    // Correo.
                cliente[5] = resultSet.getString(6);    // Pais.
                cliente[6] = resultSet.getString(7);    // Estado.
                cliente[7] = resultSet.getString(8);    // Alcaldia.
                cliente[8] = resultSet.getString(9);    // CP.
                cliente[9] = resultSet.getString(10);   // Calle.
                cliente[10] = resultSet.getString(11);  // Exterior.
                cliente[11] = resultSet.getString(12);  // Interior.
                cliente[12] = resultSet.getString(13);  // Escolaridad.
            }      
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getClienteById");
            cliente = null;
        } finally {
            disconnect();
        }
        return cliente;
    }
                
    /**
     * Guarda un usuario con los datos.
     * @param cliente lista con los datos del cliente (Nombre, Apellido Paterno, Apellido Materno, Correo, Alcaldia, CP, Calle, Num Exterior, Num Interior, Id del Estado, 
     * Id de Escolaridad).
     * @return String con el Id del cliente creado.
     */
    public int saveCliente(Object[] cliente) {
        connect();
        int clienteId;
        try {
            sentenciaSQL = "SELECT MAX(CLIENTE_ID) FROM CLIENTE";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            resultSet = preparedStatement.executeQuery();
            
            String newId = "";
            if(resultSet.next()) {
               newId = Integer.toString(resultSet.getInt(1) + 1);
            } 
            
            // Crear registro direccion para el cliente.
            DireccionDAO dirDAO = new DireccionDAO();
            Object[] dir = {
                cliente[4], // Alcaldia.
                cliente[5], // CP.
                cliente[6], // Calle.
                cliente[7], // Exterior.
                cliente[8], // Interior.
                cliente[9]  // Estado ID.
            };
            String dirId = dirDAO.saveDireccion(dir);
            
            LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String next_year = dateFormat.format(localDateTime);

            CredencialDAO credDAO = new CredencialDAO();
            int credId = credDAO.saveCredencial(next_year);
            
            // Resto del código para insertar el usuario en CLIENTE
            sentenciaSQL = "INSERT INTO CLIENTE VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);

            preparedStatement.setBigDecimal(1, new BigDecimal(newId));                      // Id (calculado).
            preparedStatement.setString(2, cliente[0].toString());                          // Nombre.
            preparedStatement.setString(3, cliente[1].toString());                          // Apellido Paterno.
            preparedStatement.setString(4, cliente[2].toString());                          // Apellido Materno.
            preparedStatement.setString(5, cliente[3].toString());                          // Correo.
            preparedStatement.setString(6, dirId);                                          // Id de dirección (calculado).
            preparedStatement.setBigDecimal(7, new BigDecimal(cliente[10].toString()));     // Id de escolaridad.
            preparedStatement.setBigDecimal(8, new BigDecimal(credId));                     // Id de credencial (calculado).
            preparedStatement.executeUpdate();
            clienteId = Integer.valueOf(newId);
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "saveCliente");
            clienteId = 0;
        } finally {
            disconnect();
        }
        return clienteId;
    }

    /**
     * Actualiza la informacion de un cliente.
     * @param clienteId Id del Cliente siendo editado.
     * @param cliente Detalles del cliente a editar (Nombre, Apellido Paterno, Apellido Materno, Correo, Alcaldia, CP, Calle, Num Exterior, Num Interior, Id del Estado, 
     * Id de Escolaridad).
     * @return String con el Id del Cliente editado.
     */
    public int updateCliente(int clienteId, Object[] cliente) {
        connect();
        int clienteEditId;
        try {             
            
            // Actualiza la direccion del cliente.
            DireccionDAO dirDAO = new DireccionDAO();
            Object[] dir = {
                cliente[4], // Alcaldia.
                cliente[5], // CP.
                cliente[6], // Calle.
                cliente[7], // Exterior.
                cliente[8], // Interior.
                cliente[9]  // Estado ID.
            };
            boolean dirActualizada = dirDAO.updateDireccion(clienteId, dir);
            if(!dirActualizada) {
                throw new Exception();
            }
                       
            // Resto del código para actualizar el usuario en Cliente
            sentenciaSQL = "UPDATE CLIENTE SET NOMBRE = ?, APELLIDO_PAT = ?, APELLIDO_MAT = ?, CORREO = ?, ESCOLARIDAD_ID = ? WHERE CLIENTE_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);            
            preparedStatement.setString(1, cliente[0].toString());                         // Nombre.
            preparedStatement.setString(2, cliente[1].toString());                         // Apellido Paterno.
            preparedStatement.setString(3, cliente[2].toString());                         // Apellido Materno.
            preparedStatement.setString(4, cliente[3].toString());                         // Correo.
            preparedStatement.setBigDecimal(5, new BigDecimal(cliente[10].toString()));    // Id de escolaridad.
            preparedStatement.setBigDecimal(6, new BigDecimal(clienteId));                 // Id (calculado).
            preparedStatement.executeUpdate();
            clienteEditId = clienteId;
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "updateCliente");
            clienteEditId = 0;
        } catch(Exception ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_ERR_QUERY + "\n\n" + "Error al actualizar la direccion " + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "updateCliente");
            clienteEditId = 0;
        } finally {
            disconnect();
        }
        return clienteEditId;
    }    
    
    /**
     * Regresa todos los clientes que coincidan con el filtro
     * @param filtro 
     * @return 
     */
    public Object[][] getClientesByFilter(String filtro) {
        connect();
        Object[][] clientes = null;
        int i = 0;
        int count = 0;
        filtro = "%" + filtro + "%";
        try {
            
            // Cuenta los registros en la base de datos.
            sentenciaSQL = "SELECT COUNT (CLIENTE_ID) FROM (" +
                    "SELECT CLIENTE_ID FROM CLIENTE WHERE UPPER(NOMBRE) LIKE UPPER(?) OR UPPER(APELLIDO_PAT) LIKE UPPER(?) OR UPPER(APELLIDO_MAT) LIKE UPPER(?) OR " +
                    "UPPER(CORREO) LIKE UPPER(?) OR CREDENCIAL_ID LIKE ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, filtro);
            preparedStatement.setString(2, filtro);
            preparedStatement.setString(3, filtro);
            preparedStatement.setString(4, filtro);
            preparedStatement.setString(5, filtro);
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
            
            // Consulta los registros .
            clientes = new Object[count][6];
            
            // Cuenta los registros en la base de datos.
            sentenciaSQL = "SELECT CLIENTE_ID, NOMBRE, APELLIDO_PAT, APELLIDO_MAT, CORREO, CREDENCIAL_ID FROM CLIENTE WHERE UPPER(NOMBRE) LIKE UPPER(?) OR " +
                    "UPPER(APELLIDO_PAT) LIKE UPPER(?) OR UPPER(APELLIDO_MAT) LIKE UPPER(?) OR UPPER(CORREO) LIKE UPPER(?) OR CREDENCIAL_ID LIKE ? ORDER BY NOMBRE";                        
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, filtro);
            preparedStatement.setString(2, filtro);
            preparedStatement.setString(3, filtro);
            preparedStatement.setString(4, filtro);
            preparedStatement.setString(5, filtro);
            resultSet = preparedStatement.executeQuery();
            
            // Recorre el result set para obtener los datos y asignarlos al arreglo.
            while(resultSet.next()) {
                clientes[i][0] = resultSet.getInt(1);                    // Id del cliente.
                clientes[i][1] = resultSet.getString(2);                 // Nombre del cliente.
                clientes[i][2] = resultSet.getString(3);                 // Apellido Paterno del cliente.
                clientes[i][3] = resultSet.getString(4);                 // Apellido Materno del cliente.
                clientes[i][4] = resultSet.getString(5);                 // Correo del cliente.
                clientes[i][5] = resultSet.getInt(6);                    // Credencial del cliente.
                i++;
            }           
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "getClientesByFilter");
            clientes = null;
        } finally {
           disconnect();
        }
        return clientes;
    }
    
    /**
     * Eliminar un estado
     * @param clienteId Id del cliente a elimianar.
     * @return int representando el resultado de la transaccion.
     */
    public int deleteCliente(int clienteId) {
        connect();
        int state = 0;
        try {
            
            // Consultar los registros asociados a este cliente.
            sentenciaSQL = "SELECT DIRECCION_ID, CREDENCIAL_ID FROM CLIENTE WHERE CLIENTE_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, clienteId);
            resultSet = preparedStatement.executeQuery();
            
            String dirId = "";
            int credId = 0;
            if(resultSet.next()) {
                dirId = resultSet.getString(1);
                credId = resultSet.getInt(2);
            }
            
            // Borrar la credencial asociada.
            sentenciaSQL = "DELETE FROM CREDENCIAL WHERE CREDENCIAL_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, credId);
            
            // Borrar el cliente.
            sentenciaSQL = "DELETE FROM CLIENTE WHERE CLIENTE_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, clienteId);
            state = preparedStatement.executeUpdate();
            
            // Borrar la direccion asociada con el cliente.
            sentenciaSQL = "DELETE FROM DIRECCION WHERE DIRECCION_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, dirId);
            preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteCliente");
            if(ex.getErrorCode() == 2292) {
                state = 0;
            } else {
                return 2;
            }
        } finally {
           disconnect();
        }
        return state;
    }
}