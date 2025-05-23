package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.ItemsCarrito;
import Config.ConnectionDB;

public class ItemsCarritoDAO {

    public boolean insertar(ItemsCarrito item) {
        String sql = "INSERT INTO tienda_deportiva.items_carrito (carrito_id, producto_id, cantidad) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getCarritoId());
            stmt.setInt(2, item.getProductoId());
            stmt.setInt(3, item.getCantidad());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar item carrito: " + e.getMessage());
            return false;
        }
    }

    public ItemsCarrito obtenerPorId(int carritoId, int productoId) {
        String sql = "SELECT * FROM tienda_deportiva.items_carrito WHERE carrito_id = ? AND producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            stmt.setInt(2, productoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ItemsCarrito item = new ItemsCarrito();
                    item.setCarritoId(rs.getInt("carrito_id"));
                    item.setProductoId(rs.getInt("producto_id"));
                    item.setCantidad(rs.getInt("cantidad"));
                    return item;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener item del carrito: " + e.getMessage());
        }
        return null;
    }

    public List<ItemsCarrito> obtenerPorCarritoId(int carritoId) {
        List<ItemsCarrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.items_carrito WHERE carrito_id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemsCarrito item = new ItemsCarrito();
                    item.setCarritoId(rs.getInt("carrito_id"));
                    item.setProductoId(rs.getInt("producto_id"));
                    item.setCantidad(rs.getInt("cantidad"));
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar items carrito: " + e.getMessage());
        }
        return lista;
    }

    public List<ItemsCarrito> obtenerTodos() {
        List<ItemsCarrito> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.items_carrito";

        try (Connection conn = ConnectionDB.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ItemsCarrito item = new ItemsCarrito();
                item.setCarritoId(rs.getInt("carrito_id"));
                item.setProductoId(rs.getInt("producto_id"));
                item.setCantidad(rs.getInt("cantidad"));
                lista.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar items carrito: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(ItemsCarrito item) {
        String sql = "UPDATE tienda_deportiva.items_carrito SET cantidad = ? WHERE carrito_id = ? AND producto_id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getCantidad());
            stmt.setInt(2, item.getCarritoId());
            stmt.setInt(3, item.getProductoId());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar item carrito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int carritoId, int productoId) {
        String sql = "DELETE FROM tienda_deportiva.items_carrito WHERE carrito_id = ? AND producto_id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            stmt.setInt(2, productoId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar item carrito: " + e.getMessage());
            return false;
        }
    }

    public List<ItemsCarrito> listarItems() {
        String sql = "SELECT ic.*, p.nombre AS nombre_producto " +
                     "FROM tienda_deportiva.items_carrito ic " +
                     "JOIN tienda_deportiva.productos p ON ic.producto_id = p.producto_id";
        List<ItemsCarrito> lista = new ArrayList<>();
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ItemsCarrito item = new ItemsCarrito();
                item.setCarritoId(rs.getInt("carrito_id"));
                item.setProductoId(rs.getInt("producto_id"));
                item.setCantidad(rs.getInt("cantidad"));
                item.setNombreProducto(rs.getString("nombre_producto"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar items del carrito: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPorCarritoId(int carritoId) {
        String sql = "DELETE FROM tienda_deportiva.items_carrito WHERE carrito_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar items_carrito por carrito_id: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorProductoId(int productoId) {
        String sql = "DELETE FROM tienda_deportiva.items_carrito WHERE producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar items_carrito por producto_id: " + e.getMessage());
            return false;
        }
    }
}
