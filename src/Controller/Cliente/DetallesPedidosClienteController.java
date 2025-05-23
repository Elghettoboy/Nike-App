package Controller.Cliente;

import Models.DetallesPedido;
import Models.Pedidos;
import Service.Clientes.DetallesPedidoClienteService;
import Repository.UsuariosDAO;
import Repository.ProductosDAO;

import java.sql.SQLException;
import java.util.List;

public class DetallesPedidosClienteController {
    private DetallesPedidoClienteService detallesPedidoService;
    private PedidosClienteController pedidosController;

    public DetallesPedidosClienteController() throws SQLException {
        this.detallesPedidoService = new DetallesPedidoClienteService();
    }

    public void setPedidosController(PedidosClienteController pedidosController) {
        this.pedidosController = pedidosController;
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

    public void mostrarPedidoConNombres(int pedidoId) throws SQLException {
        if (this.pedidosController == null) {
            System.out.println("Error: PedidosController no está configurado en DetallesPedidosClienteController.");
            return;
        }
        Pedidos pedido = pedidosController.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }

        List<DetallesPedido> detalles = pedidosController.obtenerDetallesPorPedidoId(pedidoId);

        UsuariosDAO clienteDAO = new UsuariosDAO();
        ProductosDAO productoDAO = new ProductosDAO();

        String nombreCliente = clienteDAO.obtenerNombreClientePorId(pedido.getUsuarioId());

        System.out.println("Pedido ID: " + pedido.getPedidoId() + " | Cliente: " + nombreCliente);
        System.out.println("Detalles del pedido:");

        for (DetallesPedido detalle : detalles) {
            String nombreProducto = productoDAO.obtenerNombreProductoPorId(detalle.getProductoId());
            System.out.println("Producto: " + nombreProducto +
                    " | Cantidad: " + detalle.getCantidad() +
                    " | Precio: $" + String.format("%.2f", detalle.getPrecio()));
        }
    }
}