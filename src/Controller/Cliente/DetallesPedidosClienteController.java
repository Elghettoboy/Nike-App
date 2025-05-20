package Controller.Cliente;
import Models.DetallesPedido;
import Service.Clientes.DetallesPedidoClienteService;
import java.util.List;

public class DetallesPedidosClienteController {
    private DetallesPedidoClienteService detallesPedidoService;
    
    public DetallesPedidosClienteController() {
        this.detallesPedidoService = new DetallesPedidoClienteService();
    }

    public boolean agregarDetallePedido(DetallesPedido detalle) {
        return detallesPedidoService.agregarDetallePedido(detalle);
    }

    public DetallesPedido obtenerDetallePorId(int detalleId) {
        return detallesPedidoService.obtenerDetallePorId(detalleId);
    }

    public List<DetallesPedido> listarDetallesPedidos() {
        return detallesPedidoService.listarDetallesPedidos();
    }

    public boolean actualizarDetallePedido(DetallesPedido detalle) {
        return detallesPedidoService.actualizarDetallePedido(detalle);
    }

    public boolean eliminarDetallePedido(int detalleId) {
        return detallesPedidoService.eliminarDetallePedido(detalleId);
    }   

}
