package CRUD;

import java.sql.*;
import java.util.Scanner;

/**
 * Metodos basicos SQL (Create, Read, Update, Delete)
 * */
public class CRUD {
    // Variable de instancia para almacenar la conexión a la base de datos
    private final Connection conn;

    // Constructor que recibe la conexión y la asigna al atributo conn
    public CRUD(Connection conn) {
        this.conn = conn;
    }

    /**
     * CREATE
     * Método para crear (o recrear) la tabla "personas".
     * Se elimina la tabla existente (si existe) y se crea de nuevo con una estructura definida.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    public void crear() throws SQLException {
        String query =
                "drop table if exists personas;\n" + // Elimina la tabla si ya existe
                        "create table personas(\n" +   // Define una nueva tabla llamada "personas"
                        "id numeric,\n" +             // Columna "id" de tipo numérico
                        "nombre varchar(32),\n" +     // Columna "nombre" de tipo cadena (máx 32 caracteres)
                        "apellido varchar(32),\n" +   // Columna "apellido" de tipo cadena (máx 32 caracteres)
                        "salario numeric,\n" +        // Columna "salario" de tipo numérico
                        "primary key (id)\n" +        // Define "id" como clave primaria
                        ")";

        // Crea un objeto Statement para ejecutar la consulta
        Statement stmt = conn.createStatement();
        // Ejecuta la consulta. Aquí debería ser executeUpdate (ya que no se espera un ResultSet)
        stmt.execute(query);
        stmt.close(); // Cierra el Statement para liberar recursos
    }

    /**
     * READ
     * Método para leer todas las filas de la tabla "personas".
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    public void consulta() throws SQLException {
        String query = "select * from personas"; // Consulta para seleccionar todos los registros

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query); // Ejecuta la consulta y obtiene un ResultSet

        // Itera sobre el ResultSet para procesar cada fila
        while (rs.next()) {
            int id = rs.getInt("id"); // Obtiene el valor de la columna "id"
            String nombre = rs.getString("nombre"); // Obtiene el valor de la columna "nombre"
            String apellido = rs.getString("apellido"); // Obtiene el valor de la columna "apellido"
            int salario = rs.getInt("salario"); // Obtiene el valor de la columna "salario"

            System.out.println("[" + id + "] => " + nombre + " " + apellido + " - " + salario + "€");
        }

        stmt.close(); // Cierra el Statement para liberar recursos
    }

    /**
     * UPDATE
     * Permite modificar valores de una fila específica de la tabla "personas".
     * Usa PreparedStatement para evitar inyecciones SQL y asegurar la ejecución segura.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    public void modificar(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        String query = "SELECT * FROM personas WHERE id = ?"; // Consulta para obtener la fila a modificar

        // Usa PreparedStatement para evitar SQL Injection
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id); // Asigna el valor del ID de manera segura

            try (ResultSet rs = pstmt.executeQuery()) { // Ejecuta la consulta y obtiene el ResultSet
                if (rs.next()) { // Verifica si el ID existe
                    System.out.println("Qué valor quieres modificar de " + rs.getString("nombre") + "?" +
                            "\nOpciones posibles: (1) nombre / (2) apellido / (3) salario");

                    // Lee la opción seleccionada por el usuario
                    int opcion = sc.nextInt();
                    sc.nextLine(); // Limpia el buffer de entrada
                    switch (opcion) {
                        case 1:
                            System.out.println("Inserte el nuevo nombre: ");
                            modificarAux("nombre", sc.nextLine(), id); // Llama al método auxiliar para actualizar el nombre
                            break;
                        case 2:
                            System.out.println("Inserte el nuevo apellido: ");
                            modificarAux("apellido", sc.nextLine(), id); // Llama al método auxiliar para actualizar el apellido
                            break;
                        case 3:
                            System.out.println("Inserte el nuevo salario: ");
                            modificarAux("salario", sc.nextLine(), id); // Llama al método auxiliar para actualizar el salario
                            break;
                    }
                }
            }
        }
    }

    /**
     * Método auxiliar para UPDATE.
     * Actualiza una columna específica de la tabla "personas" con un nuevo valor.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    private void modificarAux(String col, String valor, int id) throws SQLException {
        // Construye la consulta para actualizar dinámicamente la columna especificada
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE personas SET " + col + " = ? WHERE id = ?")) {
            if (col.equals("salario")) {
                // Si la columna es "salario", convierte el valor a entero
                pstmt.setInt(1, Integer.parseInt(valor));
            } else {
                // Para otras columnas, se usa como cadena
                pstmt.setString(1, valor);
            }
            pstmt.setInt(2, id); // Asigna el ID de la fila a modificar

            pstmt.executeUpdate(); // Ejecuta la consulta para modificar los datos
        }
    }

    /**
     * DELETE
     * Elimina una fila de la tabla "personas" basándose en su ID.
     *
     * @throws SQLException Si ocurre un error al conectarse a la base de datos
     */
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM personas WHERE id = ?"; // Consulta para eliminar una fila específica

        // Usa PreparedStatement para evitar SQL Injection
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id); // Asigna el ID de la fila a eliminar

            pstmt.executeUpdate(); // Ejecuta la consulta para eliminar los datos
        }
    }
}

