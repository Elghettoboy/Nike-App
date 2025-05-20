package Service.Clientes;

import java.util.List;
import Models.DetallesPedido;
import Repository.DetallesPedidoDAO;

public class DetallesPedidoClienteService {
    private DetallesPedidoDAO detallesPedidoDAO;

    public DetallesPedidoClienteService() {
        this.detallesPedidoDAO = new DetallesPedidoDAO();
    }

    public boolean agregarDetallePedido(DetallesPedido detalle) {
        return detallesPedidoDAO.insertar(detalle);
    }

    public DetallesPedido obtenerDetallePorId(int detalleId) {
        return detallesPedidoDAO.obtenerPorId(detalleId);
    }

    public List<DetallesPedido> listarDetallesPedidos() {
        return detallesPedidoDAO.obtenerTodos();
    }

    public boolean actualizarDetallePedido(DetallesPedido detalle) {
        return detallesPedidoDAO.actualizar(detalle);
    }

    public boolean eliminarDetallePedido(int detalleId) {
        return detallesPedidoDAO.eliminar(detalleId);
    }
}
