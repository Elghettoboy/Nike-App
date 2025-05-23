package Controller.Cliente;

import Models.Pedidos;
import Models.DetallesPedido;
import Service.Clientes.PedidosClienteService;
import Repository.UsuariosDAO;
import Repository.ProductosDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidosClienteController {
    private PedidosClienteService pedidosService;
    private DetallesPedidosClienteController detallesController;

    public PedidosClienteController() throws SQLException {
        this.pedidosService = new PedidosClienteService();
        this.detallesController = new DetallesPedidosClienteController();
        this.detallesController.setPedidosController(this);
    }

    public boolean agregarPedido(Pedidos pedido, List<DetallesPedido> detalles) {
        boolean creado = pedidosService.crearPedido(pedido); 
        if (creado && pedido.getPedidoId() > 0) {
            for (DetallesPedido detalle : detalles) {
                detalle.setPedidoId(pedido.getPedidoId());
                if (!detallesController.agregarDetallePedido(detalle)) {
                    System.err.println("Error al agregar un detalle del pedido ID: " + detalle.getProductoId() + ". El pedido podría estar incompleto.");
                }
            }
        } else {
            if (!creado) System.err.println("El pedido principal no pudo ser creado.");
            if (pedido.getPedidoId() <= 0) System.err.println("El pedido principal se guardó pero no se obtuvo un ID válido.");
            return false;
        }
        return creado;
    }

    public Pedidos obtenerPedidoPorId(int pedidoId) {
        return pedidosService.obtenerPedidoPorId(pedidoId);
    }

    public List<Pedidos> listarPedidos() {
        return pedidosService.listarPedidos();
    }

    public List<Pedidos> listarPedidosPorUsuario(int usuarioId) {
        if (pedidosService == null) {
             System.err.println("PedidosClienteService no está inicializado.");
             return new ArrayList<>();
        }
        return pedidosService.listarPedidosPorUsuario(usuarioId);
    }

    public boolean actualizarPedido(Pedidos pedido) {
        return pedidosService.actualizarPedido(pedido);
    }

    public boolean eliminarPedido(int pedidoId) {
        List<DetallesPedido> detallesAEliminar = this.obtenerDetallesPorPedidoId(pedidoId);
        boolean todosLosDetallesEliminados = true;

        if (detallesAEliminar != null) {
            for (DetallesPedido detalle : detallesAEliminar) {
                if (!detallesController.eliminarDetallePedido(detalle.getDetalleId())) {
                    todosLosDetallesEliminados = false;
                    System.err.println("Error al eliminar el detalle con ID: " + detalle.getDetalleId() + " para el pedido ID: " + pedidoId);
                }
            }
        }

        if (!todosLosDetallesEliminados && detallesAEliminar != null && !detallesAEliminar.isEmpty()) {
            System.err.println("No todos los detalles del pedido " + pedidoId + " fueron eliminados. No se eliminará el pedido principal.");
            return false;
        }
        
        return pedidosService.eliminarPedido(pedidoId);
    }

    public List<DetallesPedido> obtenerDetallesPorPedidoId(int pedidoId) {
        List<DetallesPedido> todosLosDetallesDeTodosLosPedidos = detallesController.listarDetallesPedidos();
        if (todosLosDetallesDeTodosLosPedidos == null) {
            return new ArrayList<>();
        }
        return todosLosDetallesDeTodosLosPedidos.stream()
                .filter(dp -> dp.getPedidoId() == pedidoId)
                .collect(Collectors.toList());
    }

    public void mostrarPedidoConDetalles(int pedidoId) {
        Pedidos pedido = obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            System.out.println("Pedido no encontrado con ID: " + pedidoId);
            return;
        }

        List<DetallesPedido> detalles = obtenerDetallesPorPedidoId(pedidoId);
        String nombreCliente = obtenerNombreClientePorId(pedido.getUsuarioId());

        System.out.println("\n------ DETALLE DEL PEDIDO ------");
        System.out.println("Pedido ID: " + pedido.getPedidoId());
        System.out.println("Cliente: " + nombreCliente);
        System.out.println("Fecha: " + pedido.getFechaPedido());
        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("\nArtículos:");
        if (detalles == null || detalles.isEmpty()) {
            System.out.println(" (Este pedido no tiene artículos detallados o no se pudieron cargar)");
        } else {
            double totalPedidoCalculado = 0;
            for (DetallesPedido detalle : detalles) {
                String nombreProducto = obtenerNombreProductoPorId(detalle.getProductoId());
                double subtotal = detalle.getCantidad() * detalle.getPrecio();
                totalPedidoCalculado += subtotal;
                System.out.println(String.format("  - %s (ID Prod: %d) | Cant: %d | P.Unit: $%.2f | Subt: $%.2f",
                        nombreProducto, detalle.getProductoId(), detalle.getCantidad(), detalle.getPrecio(), subtotal));
            }
            System.out.println("---------------------------------");
            System.out.println(String.format("TOTAL DEL PEDIDO (calculado): $%.2f", totalPedidoCalculado));
        }
        System.out.println("-------------------------------");
    }

    public String obtenerNombreClientePorId(int usuarioId) {
        try {
            UsuariosDAO usuariosDAO = new UsuariosDAO();
            return usuariosDAO.obtenerNombreClientePorId(usuarioId);
        } catch (Exception e) {
            System.err.println("Error SQL al obtener nombre del cliente ID " + usuarioId + ": " + e.getMessage());
            return "Cliente Desconocido";
        }
    }

    private String obtenerNombreProductoPorId(int productoId) {
        try {
            ProductosDAO productoDAO = new ProductosDAO();
            return productoDAO.obtenerNombreProductoPorId(productoId);
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener nombre del producto ID " + productoId + ": " + e.getMessage());
            return "Producto Desconocido";
        }
    }
}