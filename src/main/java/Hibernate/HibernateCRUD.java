package Hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import HibernateEntity.Persona;
import org.hibernate.query.Query;

public class HibernateCRUD {

    // Método para crear la tabla "personas" y cargar datos iniciales.
    public void crear(Persona persona) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Inicia una transacción
            Transaction transaction = session.beginTransaction();

            // Guardamos las personas en la base de datos
            session.persist(persona);

            // Commit de la transacción
            transaction.commit();

            System.out.println("Personas insertadas correctamente.");
        }
        catch (Exception e) {
            System.out.println("Error al insertar personas: " + e.getMessage());
        }
    }

    // Método para consultar todas las personas en la tabla "personas"
    public void consulta() {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            String hql = "FROM Persona"; // HQL consulta todas las entidades Persona
            Query<Persona> query = session.createQuery(hql, Persona.class);
            List<Persona> personas = query.getResultList();

            // Mostrar resultados
            for (Persona persona : personas) {
                System.out.println("[" + persona.getId() + "] => " + persona.getNombre() + " " + persona.getApellido() + " - " + persona.getSalario() + "€");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // Método para consultar personas filtrando por ID
    public void consultaId(BigDecimal id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            // Consulta HQL para buscar una persona por su ID
            String hql = "FROM Persona WHERE id = :id"; // Usamos un parámetro nombrado ":id"

            // Creamos la consulta
            Query<Persona> query = session.createQuery(hql, Persona.class);

            // Asignamos el valor del parámetro
            query.setParameter("id", id);

            // Ejecutamos la consulta
            Persona persona = query.uniqueResult(); // Obtiene un único resultado (persona)

            // Si encontramos la persona, la mostramos
            if (persona != null)
            {
                System.out.println("[" + persona.getId() + "] => " + persona.getNombre() + " " + persona.getApellido() + " - " + persona.getSalario() + "€");
            }
            else
            {
                System.out.println("Persona con ID " + id + " no encontrada.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


    // Método para modificar una persona
    public void modificar(BigDecimal id) {
        Scanner sc = new Scanner(System.in);

        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Persona persona = session.get(Persona.class, id); // Recupera la persona por ID

            if (persona != null)
            {
                System.out.println("Qué valor quieres modificar de " + persona.getNombre() + "?\nOpciones posibles: (1) nombre / (2) apellido / (3) salario");
                int opcion = sc.nextInt();
                sc.nextLine(); // Limpiar el buffer

                Transaction transaction = session.beginTransaction(); // Inicia transacción

                switch (opcion)
                {
                    case 1:
                        System.out.println("Inserte el nuevo nombre: ");
                        persona.setNombre(sc.nextLine());
                        break;
                    case 2:
                        System.out.println("Inserte el nuevo apellido: ");
                        persona.setApellido(sc.nextLine());
                        break;
                    case 3:
                        System.out.println("Inserte el nuevo salario: ");
                        persona.setSalario(BigDecimal.valueOf(Integer.parseInt(sc.nextLine())));
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        return;
                }

                session.merge(persona); // Actualiza la persona
                transaction.commit(); // Commit de la transacción
            }
            else
            {
                System.out.println("Persona con ID " + id + " no encontrada.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // Método para eliminar una persona
    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Persona persona = session.get(Persona.class, id); // Recupera la persona por ID

            if (persona != null)
            {
                Transaction transaction = session.beginTransaction(); // Inicia transacción
                session.remove(persona); // Elimina la persona
                transaction.commit(); // Commit de la transacción
            }
            else
            {
                System.out.println("Persona con ID " + id + " no encontrada.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
