package Models;

public class Pedidos{

    private int pedidoId;
    private int usuarioId;
    private java.sql.Timestamp fechaPedido;
    private String estado;
    
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
    
    
    
}
