package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Productos;
import Config.ConnectionDB;
import Repository.DetallesPedidoDAO;
import Repository.ItemsCarritoDAO;
import Repository.ReviewDAO;
import Repository.WishlistItemsDAO;

public class ProductosDAO {

    private DetallesPedidoDAO detallesPedidoDAO;
    private ItemsCarritoDAO itemsCarritoDAO;
    private ReviewDAO reviewDAO;
    private WishlistItemsDAO wishlistItemsDAO;

    public ProductosDAO() throws SQLException {
        this.detallesPedidoDAO = new DetallesPedidoDAO();
        this.itemsCarritoDAO = new ItemsCarritoDAO();
        this.reviewDAO = new ReviewDAO();
        this.wishlistItemsDAO = new WishlistItemsDAO();
    }

    public boolean insertar(Productos producto) {
        String sql = "insert into tienda_deportiva.productos (nombre, precio, stock, descripcion) values (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getDescripcion());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public Productos obtenerPorId(int productoId, Connection conn) throws SQLException {
        String sql = "select * from tienda_deportiva.productos where producto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Productos producto = new Productos();
                    producto.setProductoId(rs.getInt("producto_id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getDouble("precio"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    return producto;
                }
            }
        }
        return null;
    }

    public Productos obtenerPorId(int productoId) {
        try (Connection conn = ConnectionDB.getConn()) {
            return obtenerPorId(productoId, conn);
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID (auto-conexión): " + e.getMessage());
            return null;
        }
    }

    public List<Productos> obtenerTodos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.productos ORDER BY producto_id";
        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Productos producto = new Productos();
                producto.setProductoId(rs.getInt("producto_id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setDescripcion(rs.getString("descripcion"));
                lista.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public List<Productos> obtenerProductosPaginados(int limite, int offset) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.productos ORDER BY producto_id LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limite);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Productos producto = new Productos();
                    producto.setProductoId(rs.getInt("producto_id"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecio(rs.getDouble("precio"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    lista.add(producto);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar productos paginados: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Productos producto, Connection conn) throws SQLException {
        String sql = "update tienda_deportiva.productos set nombre = ?, precio = ?, stock = ?, descripcion = ? where producto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getDescripcion());
            stmt.setInt(5, producto.getProductoId());
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean actualizar(Productos producto) {
        try (Connection conn = ConnectionDB.getConn()) {
            return actualizar(producto, conn);
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto (auto-conexión): " + e.getMessage());
            return false;
        }
    }

    public String obtenerNombreProductoPorId(int productoId) throws SQLException {
        String nombre = "Producto desconocido";
        String sql = "SELECT nombre FROM tienda_deportiva.productos WHERE producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombre = rs.getString("nombre");
                }
            }
        }
        return nombre;
    }

    public boolean eliminar(int productoId) {
        boolean detallesPedidoOk = detallesPedidoDAO.eliminarPorProductoId(productoId);
        if (!detallesPedidoOk) {
            System.out.println("Fallo al eliminar detalles_pedido para el producto_id: " + productoId + ". La eliminación del producto se detendrá.");
            return false;
        }

        boolean itemsCarritoOk = itemsCarritoDAO.eliminarPorProductoId(productoId);
        if (!itemsCarritoOk) {
            System.out.println("Fallo al eliminar items_carrito para el producto_id: " + productoId + ". La eliminación del producto se detendrá.");
            return false;
        }

        boolean reviewsOk = reviewDAO.eliminarPorProductoId(productoId);
        if (!reviewsOk) {
            System.out.println("Fallo al eliminar reviews para el producto_id: " + productoId + ". La eliminación del producto se detendrá.");
            return false;
        }

        boolean wishlistItemsOk = wishlistItemsDAO.eliminarPorProductoId(productoId);
        if (!wishlistItemsOk) {
            System.out.println("Fallo al eliminar wishlist_items para el producto_id: " + productoId + ". La eliminación del producto se detendrá.");
            return false;
        }

        String sql = "delete from tienda_deportiva.productos where producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Advertencia: No se encontró el producto con ID " + productoId + " para eliminar en la tabla productos, o ya fue eliminado.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto (tabla principal) con id " + productoId + ": " + e.getMessage());
            return false;
        }
    }
}