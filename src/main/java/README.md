desenvolve unha aplicación mínima que insira, modifique e elimine datos dunha taboa .
o script de creacion da taboa para facer as probas pode ser este :

drop table if exists personas;
create table personas(
id numeric,
nombre varchar(32),
apellido varchar(32),
salario numeric,
primary key (id)
)
;
insert into personas values (1,'luis','perez',2000.4);
insert into personas values (2,'ana','suarez',2100.427);
insert into personas values (3,'pedro','aguiño',1500.6123254);

# Pasos realizados

1. Dependencias
2. Crear clase entidad (Mapping tabla personas a la clase Persona)
````java
import javax.persistence.*;

@Entity // Indica que esta clase es una entidad de JPA
@Table(name = "personas") // Asocia esta clase a la tabla "personas"
public class Persona {
    @Id // Define esta columna como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de IDs
    private Long id;

    @Column(name = "nombre", nullable = false, length = 32) // Mapea la columna "nombre"
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 32) // Mapea la columna "apellido"
    private String apellido;

    @Column(name = "salario") // Mapea la columna "salario"
    private Double salario;

    // Getters y Setters...
}
````
3. Poner la configuración de Hibernate en el persistance.xml
`````xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
                                 https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="myPersistenceUnit">
        <!-- Optional description -->
        <description>My Persistence Unit for JPA</description>

        <!-- JPA Provider (e.g., Hibernate) -->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!-- Data Source configuration -->
        <!-- Classes to be managed by JPA -->
        <class>CRUDHibernate.entity.Persona</class>

        <!-- Optional, exclude unlisted classes -->
        <exclude-unlisted-classes>false</exclude-unlisted-classes>

        <!-- Custom properties -->
        <properties>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="show_sql" value="true"/>
            <!-- Provide your database URL, username, and password -->
            <property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/scheme"/>
            <property name="hibernate.connection.username" value="root"/>
            <property name="hibernate.connection.password" value="admin"/>
        </properties>
    </persistence-unit>
</persistence>
`````
4. Crear clase para el manejo de la sesión de Hibernate (Utils)
````java
public class HibernateUtil {
    // Singleton para la fábrica de sesiones de Hibernate
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Método que construye la fábrica de sesiones utilizando la configuración de Hibernate
    private static SessionFactory buildSessionFactory() {
        try {
            // Lee el archivo hibernate.cfg.xml y construye la configuración
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Manejo de errores al inicializar Hibernate
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Método para obtener la fábrica de sesiones
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Método para cerrar la fábrica de sesiones al final del programa
    public static void shutdown() {
        getSessionFactory().close();
    }
}
````
5. Crear la clase CRUD con Hibernate
````java
public void crear(Persona persona) {
    Transaction transaction = null; // Se usa para manejar transacciones en Hibernate
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        // Inicia una transacción
        transaction = session.beginTransaction();
        // Guarda la entidad (INSERT en la base de datos)
        session.save(persona);
        // Confirma la transacción
        transaction.commit();
    } catch (Exception e) {
        // Si hay un error, revierte la transacción para evitar inconsistencias
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    }
}

public List<Persona> consultar() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        // Ejecuta una consulta HQL para obtener todas las entidades Persona
        return session.createQuery("from Persona", Persona.class).list();
    }
}

public void modificar(Persona persona) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        // Inicia una transacción
        transaction = session.beginTransaction();
        // Actualiza la entidad (UPDATE en la base de datos)
        session.update(persona);
        // Confirma la transacción
        transaction.commit();
    } catch (Exception e) {
        // Si hay un error, revierte la transacción
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    }
}

public void eliminar(Long id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        // Inicia una transacción
        transaction = session.beginTransaction();
        // Obtiene la entidad por su ID
        Persona persona = session.get(Persona.class, id);
        if (persona != null) {
            // Si la entidad existe, la elimina (DELETE en la base de datos)
            session.delete(persona);
        }
        // Confirma la transacción
        transaction.commit();
    } catch (Exception e) {
        // Si hay un error, revierte la transacción
        if (transaction != null) transaction.rollback();
        e.printStackTrace();
    }
}
````
6. Implementación del código
````java
public class Main {
    public static void main(String[] args) {
        PersonaDAO dao = new PersonaDAO();

        // Crear una nueva persona
        Persona p = new Persona();
        p.setNombre("Juan");
        p.setApellido("Pérez");
        p.setSalario(3000.0);
        dao.crear(p); // Guarda la persona en la base de datos

        // Consultar todas las personas
        List<Persona> personas = dao.consultar();
        personas.forEach(persona -> System.out.println(persona.getNombre()));

        // Modificar la primera persona encontrada
        if (!personas.isEmpty()) {
            Persona persona = personas.get(0); // Obtén la primera persona
            persona.setNombre("Carlos"); // Cambia su nombre
            dao.modificar(persona); // Actualiza la base de datos
        }

        // Eliminar la primera persona encontrada
        if (!personas.isEmpty()) {
            dao.eliminar(personas.get(0).getId()); // Elimina por ID
        }

        // Cerrar la sesión de Hibernate al terminar
        HibernateUtil.shutdown();
    }
}

````