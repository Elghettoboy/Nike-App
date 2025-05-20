package Service.Clientes;

import Models.Pedidos;
import Repository.PedidosDAO;

import java.util.List;

public class PedidosClienteService {

    private PedidosDAO pedidosDAO;

    public PedidosClienteService() {
        this.pedidosDAO = new PedidosDAO();
    }

    public boolean crearPedido(Pedidos pedido) {
        return pedidosDAO.insertar(pedido);
    }

    public Pedidos obtenerPedidoPorId(int id) {
        return pedidosDAO.obtenerPorId(id);
    }

    public List<Pedidos> listarPedidos() {
        return pedidosDAO.obtenerTodos();
    }

    public boolean actualizarPedido(Pedidos pedido) {
        return pedidosDAO.actualizar(pedido);
    }

    public boolean eliminarPedido(int id) {
        return pedidosDAO.eliminar(id);
    }
}
