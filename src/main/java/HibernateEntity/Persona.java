package HibernateEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @Column(name = "id", nullable = false, precision = 10)
    private BigDecimal id;

    @Column(name = "nombre", length = 32)
    private String nombre;

    @Column(name = "apellido", length = 32)
    private String apellido;

    @Column(name = "salario", precision = 10)
    private BigDecimal salario;

    // Constructor sin parámetros (requerido por Hibernate)
    public Persona() {
        // Hibernate necesita este constructor sin parámetros para instanciar la clase
    }

    // Constructor parametrizado
    public Persona(BigDecimal id, String nombre, String apellido, BigDecimal salario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.salario = salario;
    }

    // Getters y setters
    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}
