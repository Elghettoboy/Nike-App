package Service.Clientes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import Config.ConnectionDB;
import Models.DetallesPedido;
// import Models.Envios; // Eliminado
import Models.Pedidos;
import Models.Productos;
import Models.Usuarios;
import Repository.DetallesPedidoDAO;
import Repository.PedidosDAO;
import Repository.ProductosDAO;
import Repository.UsuariosDAO;
// import Service.Clientes.EnviosClienteService; // Eliminado

public class PedidosClienteService {

    private PedidosDAO pedidosDAO;
    private DetallesPedidoDAO detallesPedidoDAO;
    private ProductosDAO productosDAO;
    // private EnviosClienteService enviosService; // Eliminado
    private UsuariosDAO usuariosDAO;

    public PedidosClienteService() {
        try {
            this.pedidosDAO = new PedidosDAO();
            this.detallesPedidoDAO = new DetallesPedidoDAO();
            this.productosDAO = new ProductosDAO();
            // this.enviosService = new EnviosClienteService(); // Eliminado
            this.usuariosDAO = new UsuariosDAO();
        } catch (SQLException e) {
            System.err.println("Error al inicializar DAOs en PedidosClienteService: " + e.getMessage());
            throw new RuntimeException("Error de inicialización del servicio de pedidos.", e);
        }
    }

    public boolean crearPedido(Pedidos pedido) {
        return pedidosDAO.insertar(pedido);
    }

    public boolean crearPedidoCompleto(Pedidos pedido, List<DetallesPedido> detalles, Usuarios usuario) {
        Connection conn = null;
        boolean exito = false;
        try {
            conn = ConnectionDB.getConn();
            if (conn == null) {
                System.err.println("Fallo al obtener conexión de ConnectionDB.");
                return false;
            }
            conn.setAutoCommit(false);

            if (!pedidosDAO.insertar(pedido, conn) || pedido.getPedidoId() <= 0) {
                throw new SQLException("No se pudo guardar el encabezado del pedido o no se obtuvo ID válido del pedido.");
            }

            for (DetallesPedido detalle : detalles) {
                detalle.setPedidoId(pedido.getPedidoId());
                if (!detallesPedidoDAO.insertar(detalle, conn) || detalle.getDetalleId() <= 0) {
                    throw new SQLException("Error al guardar un detalle del pedido (ID Producto: " + detalle.getProductoId() + ") o no se obtuvo ID de detalle válido.");
                }

                Productos producto = productosDAO.obtenerPorId(detalle.getProductoId(), conn);
                if (producto == null) {
                    throw new SQLException("Producto ID " + detalle.getProductoId() + " no encontrado para actualizar stock.");
                }
                int nuevoStock = producto.getStock() - detalle.getCantidad();
                if (nuevoStock < 0) {
                    throw new SQLException("Stock insuficiente para el producto ID " + producto.getProductoId() + ".");
                }
                producto.setStock(nuevoStock);
                if (!productosDAO.actualizar(producto, conn)) {
                    throw new SQLException("Error al actualizar stock para el producto ID " + producto.getProductoId() + ".");
                }

                // Sección de creación de Envío ELIMINADA
            }

            conn.commit();
            exito = true;
            System.out.println("Pedido completo creado y stock actualizado exitosamente.");

        } catch (SQLException e) {
            System.err.println("Error de SQL durante la creación completa del pedido: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transacción del pedido revertida.");
                } catch (SQLException ex) {
                    System.err.println("Error crítico al intentar hacer rollback en la creación del pedido: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al restaurar auto-commit o cerrar conexión del pedido: " + e.getMessage());
                }
            }
        }
        return exito;
    }

    public Pedidos obtenerPedidoPorId(int id) {
        return pedidosDAO.obtenerPorId(id);
    }

    public List<Pedidos> listarPedidos() {
        return pedidosDAO.obtenerTodos();
    }

    public List<Pedidos> listarPedidosPorUsuario(int usuarioId) {
        if (pedidosDAO == null) {
            System.err.println("PedidosDAO no está inicializado en PedidosClienteService.");
            return new ArrayList<>();
        }
        return pedidosDAO.obtenerTodosPorUsuarioId(usuarioId);
    }

    public boolean actualizarPedido(Pedidos pedido) {
        return pedidosDAO.actualizar(pedido);
    }

    public boolean eliminarPedido(int id) {
        return pedidosDAO.eliminar(id);
    }

    public List<Pedidos> listarPedidosConDetalles() {
        return pedidosDAO.obtenerPedidosConDetalles();
    }
}