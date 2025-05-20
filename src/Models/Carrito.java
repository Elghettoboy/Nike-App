package Models;

public class Carrito {
    
    private int carritoId;
    private int usuarioId;
    private java.sql.Timestamp fechaCreacion;
    
    public int getCarritoId() {
        return carritoId;
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

    
    
}

    

