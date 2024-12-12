package dataBase.dao;

import dataBase.CustomConnection;
import dataBase.ConfigDataBase;
import java.sql.*;

/**
 * Clase encargada de consultar, insertar, eliminar y modificar la tabla Estado.
 * @author alfonso
 */
public class EstadoDAO extends CustomConnection {
    
    /**
     * Crear un estado en un pais
     * @param estado datos del estado (Nombre, Id del pais).
     * @return int con el Id del estado creado.
     */
    public int saveEstado(Object[] estado) {
        connect();
        int estadoId = 0;
        try {
            String id = "";
            
            // Genera el nuevo id.
            sentenciaSQL = "SELECT PAIS_ID || LPAD(COALESCE(MAX(TO_NUMBER(SUBSTR(ESTADO_ID, 3, 2))), 0) + 1, 2, '0') FROM PAIS LEFT JOIN ESTADO USING (PAIS_ID) " +
                    "WHERE PAIS_ID = ? GROUP BY PAIS_ID";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado[1].toString());
            resultSet = preparedStatement.executeQuery();
            
            // Guarda el nuevo id.
            if(resultSet.next()) {
                id = resultSet.getString(1);
            }
            
            // Inserta mandando todos los datos, incluido en nuevo id.
            sentenciaSQL = "INSERT INTO ESTADO VALUES(?, ?, ?)";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            
            // Asigna los valores del arreglo.
            estadoId = Integer.valueOf(id);
            preparedStatement.setInt(1, estadoId);                  // Id (calculado).
            preparedStatement.setString(2, estado[0].toString());   // Estado (provisto).
            preparedStatement.setInt(3, (int) estado[1]);           // Id Pais (provisto).
            preparedStatement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "saveEstado");
            estadoId = 0;
        } finally {
           disconnect();
        }
        return estadoId;
    }
    
    /**
     * Modificar un estado
     * @param estado datos del estado (Id, nuevo nombre).
     * @return int con el Id del estado modificado.
     */
    public int updateEstado(Object[] estado) {
        connect();
        int estadoId;
        try {
           
            // Actualiza los datos
            sentenciaSQL = "UPDATE ESTADO SET ESTADO = ? WHERE ESTADO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado[1].toString());   // Nuevo nombre estado.
            preparedStatement.setInt(2, (int) estado[0]);           // Id del estado.
            preparedStatement.executeUpdate();
            estadoId = (int) estado[0];
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "updateEstado");
            estadoId = 0;
        } finally {
           disconnect();
        }
        return estadoId;
    }
    
    /**
     * Consultar todos los estados de un pais
     * @param paisId Id del pais a consultar.
     * @return Object[][] con los datos de los estados (Id, Estado).
     */
    public Object[][] getAllEstadosByPais(int paisId) {
       connect();
       Object[][] estados = null;
       try {
           
            // Numero de estados en el pais.
            sentenciaSQL =  "SELECT COUNT(*) FROM ESTADO WHERE PAIS_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, paisId);
            resultSet = preparedStatement.executeQuery();
           
            // Numero de idiomas (COUNT)
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
                       
            sentenciaSQL = "SELECT ESTADO_ID, ESTADO FROM ESTADO WHERE PAIS_ID = ? ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, paisId);
            resultSet = preparedStatement.executeQuery();
           
            // Agregar a todos los estados al arreglo.
            int i = 0;
            estados = new Object[count][2];
            while(resultSet.next()) {
                estados[i][0]=(resultSet.getString(1));
                estados[i][1]=(resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getAllEstadosByPais");
            estados = null;
        } finally {
           disconnect();
        }
        return estados;
    }  

    /**
     * Consulta el estado para un Id.
     * @param estadoId Id del estado a consultar.
     * @return Object[] con los datos del estado (Id, Estado, Id del pais).
     */
    public Object[] getEstadoById(int estadoId) {
        connect();
        Object[] estado = null;
        try {
            sentenciaSQL = "SELECT ESTADO_ID, ESTADO, PAIS_ID FROM ESTADO WHERE ESTADO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, estadoId);
            resultSet = preparedStatement.executeQuery();

            // Llena el arreglo estado con el resultado.
            estado = new Object[3];
            while(resultSet.next()) {
                estado[0] = resultSet.getInt(1);
                estado[1] = resultSet.getString(2);
                estado[2] = resultSet.getString(2);
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEstadoById");
            estado = null;
        } finally {
           disconnect();
        }
        return estado;
    }
           
    /**
     * Consultar estados con nombre parecido.
     * @param estado nombre del estado.
     * @return 
     */
    public Object[][] getEstadosByNombre(String estado) {
        connect();
        Object[][] estados = null;
        estado = "%" + estado + "%";
        try {
            
            // Cuenta todos los estados que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(ESTADO_ID) FROM ESTADO WHERE UPPER (ESTADO) LIKE UPPER(?) ";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado);
            resultSet = preparedStatement.executeQuery();
           
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            estados = new Object[count][2];
            sentenciaSQL =  "SELECT ESTADO_ID, ESTADO FROM ESTADO WHERE UPPER (ESTADO) LIKE UPPER(?) ";
            sentenciaSQL += "ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado);
            resultSet = preparedStatement.executeQuery();

            int i = 0;
            while(resultSet.next()) {
                estados[i][0] = (resultSet.getInt(1));
                estados[i][1] = (resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEstadosByNombre");
            estados = null;
        } finally {
           disconnect();
        }
        return estados;
    }
    
    /**
     * Consultar estados con nombre parecido limitando la busqueda al pais especificado.
     * @param paisId Id del pais por el cual filtrar.
     * @param estado nombre del estado.
     * @return 
     */
    public Object[][] getEstadosByNombre(int paisId, String estado) {
        connect();
        Object[][] estados = null;
        estado = "%" + estado + "%";
        try {
            
            // Cuenta todos los estados que tengan un nombre parecido.
            sentenciaSQL = "SELECT COUNT(ESTADO_ID) FROM ESTADO WHERE UPPER (ESTADO) LIKE UPPER(?) AND PAIS_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado);            
            preparedStatement.setInt(2, paisId);
            resultSet = preparedStatement.executeQuery();
           
            int count = 0;
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
           
            // Misma consulta, pero ahora puede ser guardada en el arreglo.
            estados = new Object[count][2];
            sentenciaSQL =   "SELECT ESTADO_ID, ESTADO FROM ESTADO WHERE UPPER (ESTADO) LIKE UPPER(?) AND PAIS_ID = ? ORDER BY 2";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setString(1, estado);
            preparedStatement.setInt(2, paisId);
            resultSet = preparedStatement.executeQuery();

            int i = 0;
            while(resultSet.next()) {
                estados[i][0] = (resultSet.getInt(1));
                estados[i][1] = (resultSet.getString(2));
                i++;
            }
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + ConfigDataBase.DB_ERR_QUERY + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + 
                    "\n\nUbicación: " + "getEstadosByNombre");
            estados = null;
        } finally {
           disconnect();
        }
        return estados;
    }

    /**
     * Eliminar un estado.
     * @param estadoId Id del estado a eliminar.
     * @return int representando el resultado de la transaccion.
     */
    public int deleteEstado(int estadoId) {
        connect();
        int state = 0;
        try {
            
            // Cambiar a NULL el estado de cualquier direccion con el estado a borrar.
            sentenciaSQL = "UPDATE DIRECCION SET ESTADO_ID = NULL WHERE ESTADO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, estadoId);
            preparedStatement.executeUpdate();
            
            // Borrar el estado.
            sentenciaSQL = "DELETE FROM ESTADO WHERE ESTADO_ID = ?";
            preparedStatement = connection.prepareStatement(sentenciaSQL);
            preparedStatement.setInt(1, estadoId);
            state = preparedStatement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ex.getSQLState() + "\n\n" + ex.getMessage() + "\n\n" + sentenciaSQL + "\n\nUbicación: " + "deleteEstado");
            if(ex.getErrorCode() == 2292) {
                state = 0;
            } else {
                state = 2;
            }
        } finally {
           disconnect();
        }
        return state;
    }
}
