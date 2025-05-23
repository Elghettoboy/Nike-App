package Models;

import java.util.List;

public class Pedidos {

    private int pedidoId;
    private int usuarioId;
    private java.sql.Timestamp fechaPedido;
    private String estado;
    private List<DetallesPedido> detalles; // Nuevo atributo

    public int getPedidoId() {
        return pedidoId;
    }
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public java.sql.Timestamp getFechaPedido() {
        return fechaPedido;
    }
    public void setFechaPedido(java.sql.Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos para detalles
    public List<DetallesPedido> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetallesPedido> detalles) {
        this.detalles = detalles;
    }
    
}