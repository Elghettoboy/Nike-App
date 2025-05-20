package Models;

public class DetallesPedido {

    private int detalleId;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private double precio;
    
    public int getDetalleId() {
        return detalleId;
    }
    public void setDetalleId(int detalleId) {
        this.detalleId = detalleId;
    }
    public int getPedidoId() {
        return pedidoId;
    }
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }
    public int getProductoId() {
        return productoId;
    }
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
