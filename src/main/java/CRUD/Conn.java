package CRUD;

import java.sql.*;
import java.util.Scanner;

/**
 * Clase CRUD con JDBC
 * */
public class Conn {
    // Variable para almacenar la conexión a la base de datos
    private Connection conn = null;

    // Instancia única de la clase Conn para el patrón Singleton
    private static Conn instance = null;

    /**
     * Método estático para obtener la instancia única de Conn (Singleton).
     * Si la instancia no existe, la crea llamando al constructor privado.
     *
     * @return La única instancia de Conn
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    public static Conn getInstanceOf() throws SQLException {
        if (instance == null) { // Verifica si la instancia aún no ha sido creada
            instance = new Conn(); // Crea la instancia
        }
        return instance; // Retorna la instancia única
    }

    /**
     * Constructor privado.
     * Solo puede ser llamado desde dentro de esta clase, garantizando el patrón Singleton.
     * Se encarga de inicializar la conexión a la base de datos.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    private Conn() throws SQLException {
        conectar(); // Llama al método para establecer la conexión
    }

    /**
     * Retorna la conexión a la base de datos.
     *
     * @return El objeto Connection
     */
    public Connection getConnection() {
        return conn; // Devuelve la conexión almacenada en la variable conn
    }

    /**
     * Método privado para establecer la conexión a la base de datos.
     * Configura el driver JDBC y establece la conexión usando la URL, usuario y contraseña.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    private void conectar() throws SQLException {
        String jdbc = "jdbc:mysql://localhost:3306/scheme"; // URL del servidor y esquema
        conn = DriverManager.getConnection(jdbc, "root", ""); // Establece la conexión con usuario y contraseña
    }
}
