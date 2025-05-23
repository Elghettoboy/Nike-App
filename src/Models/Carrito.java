package Models;

public class Carrito {

    private int carritoId;
    private int usuarioId;
    private java.sql.Timestamp fechaCreacion;

    public int getCarritoId() {
        return carritoId;
    }

    private String nombreUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setCarritoId(int carritoId) {
        this.carritoId = carritoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public java.sql.Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(java.sql.Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
public String toString() {
    return "Carrito ID: " + carritoId +
           ", Usuario ID: " + usuarioId +
           ", Nombre de usuario: " + (nombreUsuario != null ? nombreUsuario : "N/A") +
           ", Fecha de creación: " + (fechaCreacion != null ? fechaCreacion.toString() : "N/A");
}

}
