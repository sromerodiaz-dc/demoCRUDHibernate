package Hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Cargar configuración y construir SessionFactory
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Manejar errores de inicialización
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Método para obtener la SessionFactory Singleton.
     *
     * @return La instancia única de SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Método para cerrar la SessionFactory al final de la aplicación.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
