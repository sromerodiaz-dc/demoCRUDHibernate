import CRUD.CRUD;
import CRUD.Conn;

import Hibernate.HibernateCRUD;
import HibernateEntity.Persona;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("CRUD con Hibernate => 1\nCRUD sin Hibernate => 2");

        if (sc.nextLine().equals("1")) CRUDconHibernate();
        else if (sc.nextLine().equals("2")) CRUDsinHibernate();
        else System.out.println("Opcion incorrectar");


        /*
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
        */
    }

    static void CRUDconHibernate() {
        HibernateCRUD hibernateCRUD = new HibernateCRUD();

        // Crear una nueva Persona
        Persona nuevaPersona = new Persona(BigDecimal.valueOf(1), "Juan", "Pérez", BigDecimal.valueOf(3000));

        // Metodo Hibernate para crear Persona
        hibernateCRUD.crear(nuevaPersona);
        System.out.println("Persona creada: " + nuevaPersona.getNombre());

        // Consultar todas las personas
        hibernateCRUD.consulta();

        // Modificar una persona
        hibernateCRUD.modificar(BigDecimal.valueOf(3000));
        System.out.println("Persona con ID 3000 modificada.");

        // Consultar todas las personas
        hibernateCRUD.consulta();

        // Consulta por ID
        hibernateCRUD.consultaId(BigDecimal.valueOf(3000));

        // Eliminar una persona
        hibernateCRUD.eliminar(3000);
        System.out.println("Persona con ID 3000 eliminada.");

        // Consultar todas las personas
        hibernateCRUD.consulta();

        // Consultar todas las personas
        hibernateCRUD.consulta();
    }

    static void CRUDsinHibernate() throws SQLException {
        // Obtiene una conexión a la base de datos utilizando la clase Conn (Singleton)
        Connection conn = Conn.getInstanceOf().getConnection();
        // Crea una instancia de la clase CRUD, pasándole la conexión como parámetro
        CRUD crud = new CRUD(conn);

        Scanner sc = new Scanner(System.in);

        String opcion;
        do
        {
            // Imprime las opciones disponibles
            System.out.println("Qué acción deseas realizar?\nOpciones posibles: (C)reate / (R)ead / (U)pdate / (D)elete / (exit)");
            opcion = sc.nextLine();

            switch (opcion)
            {
                case "C":
                    crud.crear();
                    break;
                case "R":
                    crud.consulta();
                    break;
                case "U":
                    System.out.println("Introduce el id de la persona que quieras modificar: ");
                    crud.modificar(sc.nextInt());
                    sc.nextLine();
                    break;
                case "D":
                    System.out.println("Introduce el id de la persona que quieras eliminar de la tabla: ");
                    crud.eliminar(sc.nextInt());
                    sc.nextLine();
                    break;
                default:
                    break;
            }
        } while (!opcion.equals("exit")); // Repite el menú hasta que se escriba "exit"

        // Cierra la conexión a la base de datos al finalizar
        conn.close();
    }
}
