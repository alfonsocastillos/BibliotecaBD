package dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Conexion {
    // Statement -> PreparedStatement -> CallableStatement
    public Connection conn = null;          // Sesion con la BD
    public ArrayList<Statement> statements; // Arreglo de instrucciones SQL
    public Statement s;                     // Una sola instruccion SQL
    public String sentenciaSQL;             // Instruccion SQL en forma string
    public ResultSet rs;                    // Tabla resultante de una consulta
    public PreparedStatement ps;            // Instruccion de SQL pre-compilada
    public CallableStatement cstmt;         // Llama un proceso almacenad   
    
    // Conecta a la base de datos del sistema y la crea
    public void conectar (){
        // Lee los controladores
        loadDriver();
        conn = null;
        // Lista de sentencias
        statements = new ArrayList();
        try{
            // Propiedades de la conexión
            Properties props = new Properties();
            props.put("user", ConfigDataBase.JDBC_USER);
            props.put("password", ConfigDataBase.JDBC_PSW);
            
            // Obtiene la conexión usando la URL de la BD y las propiedades para conectarse
            conn = DriverManager.getConnection(ConfigDataBase.JDBC_PROTOCOL, props);
            conn.setAutoCommit(true);   // Ejecuta las sentencias y guarda los resultados en la BD
            // Crea la sentencia cuyos resultados son iterables (SCROLL_SENSITIVE) y alterables (UPDATABLE)
            s = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statements.add(s);
        }
        catch (SQLException sqle){
            System.out.println(ConfigDataBase.DB_T_ERROR +  sqle.getSQLState() + 
                    ConfigDataBase.DB_NOCONECT);
            s = null;
        }
    }
    
    // Lee los controladores de Oracle
    private void loadDriver(){
        try {
            // Busca los drivers para Oracle y crea una instancia de su clase
            Class.forName(ConfigDataBase.JDBC_DRIVER_ORACLE).newInstance();
        } 
        catch (ClassNotFoundException cnfe) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_DRIVERNO_REC);
        } 
        catch (InstantiationException ie) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_NOINST_DRIVER);
        } 
        catch (IllegalAccessException iae) {
            System.out.println(ConfigDataBase.DB_T_ERROR + ConfigDataBase.DB_NOACCESS_DRIVER);
        }
    }
    
    // Desconecta la base de datos
    public void desconectar (){
        if (statements != null) 
            statements.clear();
        try{
            if (s != null)
                s.close();
            if (conn != null) 
                conn.close();
        }
        catch (SQLException sqle) {
            System.out.println(ConfigDataBase.DB_T_ERROR + sqle.getSQLState() +
                    ConfigDataBase.DB_CLOSECON_ERR + " " + sqle.getMessage());
        }
    }    
}