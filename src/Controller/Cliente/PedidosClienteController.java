package Controller.Cliente;

import Models.Pedidos;
import Models.DetallesPedido;
import Service.Clientes.PedidosClienteService;

import java.util.List;
import java.util.stream.Collectors;

public class PedidosClienteController {
    private PedidosClienteService pedidosService;
    private DetallesPedidosClienteController detallesController;

    public PedidosClienteController() {
        this.pedidosService = new PedidosClienteService();
        this.detallesController = new DetallesPedidosClienteController(); // conexión entre controllers
    }

    public boolean agregarPedido(Pedidos pedido, List<DetallesPedido> detalles) {
        boolean creado = pedidosService.crearPedido(pedido);
        if (creado) {
            for (DetallesPedido detalle : detalles) {
                detalle.setPedidoId(pedido.getPedidoId()); // Asigna el ID del pedido
                detallesController.agregarDetallePedido(detalle);
            }
        }
        return creado;
    }

    public Pedidos obtenerPedidoPorId(int pedidoId) {
        return pedidosService.obtenerPedidoPorId(pedidoId);
    }

    public List<Pedidos> listarPedidos() {
        return pedidosService.listarPedidos();
    }

    public boolean actualizarPedido(Pedidos pedido) {
        return pedidosService.actualizarPedido(pedido);
    }

    public boolean eliminarPedido(int pedidoId) {
        // Elimina los detalles primero
        List<DetallesPedido> detalles = detallesController.listarDetallesPedidos();
        for (DetallesPedido detalle : detalles) {
            if (detalle.getPedidoId() == pedidoId) {
                detallesController.eliminarDetallePedido(detalle.getDetalleId());
            }
        }
        // Luego el pedido
        return pedidosService.eliminarPedido(pedidoId);
    }

    public List<DetallesPedido> obtenerDetallesPorPedidoId(int pedidoId) {
        List<DetallesPedido> todos = detallesController.listarDetallesPedidos();
        return todos.stream()
                    .filter(dp -> dp.getPedidoId() == pedidoId)
                    .collect(Collectors.toList());
    }
}
