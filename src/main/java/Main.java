import CRUD.CRUD;
import CRUD.Conn;
import CRUDHibernate.entity.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try (entityManagerFactory; entityManager) {
            transaction.begin();

            Persona persona = new Persona();

            persona.setNombre("Jose");
            persona.setApellido("Pepe");
            persona.setId(BigDecimal.valueOf(10));
            persona.setSalario(BigDecimal.valueOf(2000));

            entityManager.persist(persona);

            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    static void CRUDsinHibernate() throws SQLException {
        // Obtiene una conexión a la base de datos utilizando la clase Conn (Singleton)
        Connection conn = Conn.getInstanceOf().getConnection();
        // Crea una instancia de la clase CRUD, pasándole la conexión como parámetro
        CRUD crud = new CRUD(conn);

        // Scanner para capturar entradas del usuario desde la consola
        Scanner sc = new Scanner(System.in);

        String opcion; // Variable para almacenar la opción elegida por el usuario
        do {
            // Imprime las opciones disponibles
            System.out.println("Qué acción deseas realizar?\nOpciones posibles: (C)reate / (R)ead / (U)pdate / (D)elete / (exit)");
            opcion = sc.nextLine(); // Lee la entrada del usuario

            // Evalúa la opción seleccionada mediante un switch
            switch (opcion) {
                case "C": // Opción para crear la tabla
                    crud.crear(); // Llama al método crear de la clase CRUD
                    break;
                case "R": // Opción para leer los datos de la tabla
                    crud.consulta(); // Llama al método consulta de la clase CRUD
                    break;
                case "U": // Opción para actualizar un registro
                    System.out.println("Introduce el id de la persona que quieras modificar: ");
                    crud.modificar(sc.nextInt()); // Llama al método modificar con el ID ingresado
                    sc.nextLine(); // Limpia el buffer del Scanner tras leer un número
                    break;
                case "D": // Opción para eliminar un registro
                    System.out.println("Introduce el id de la persona que quieras eliminar de la tabla: ");
                    crud.eliminar(sc.nextInt()); // Llama al método eliminar con el ID ingresado
                    sc.nextLine(); // Limpia el buffer del Scanner tras leer un número
                    break;
                default: // Si no se selecciona una opción válida, no hace nada
                    break;
            }
        } while (!opcion.equals("exit")); // Repite el menú hasta que se escriba "exit"

        // Cierra la conexión a la base de datos al finalizar
        conn.close();
    }

}
