package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Models.ItemsCarrito;
import Models.ItemsCarritoConProducto;
import Config.ConnectionDB;

public class ItemsConCarritoDAO {

    public List<ItemsCarritoConProducto> obtenerItemsConProducto(int carritoId) {
        List<ItemsCarritoConProducto> lista = new ArrayList<>();
        String sql = "SELECT ic.carrito_id, ic.producto_id, ic.cantidad, p.nombre, p.precio " +
                     "FROM tienda_deportiva.items_carrito ic " +
                     "JOIN tienda_deportiva.productos p ON ic.producto_id = p.producto_id " +
                     "WHERE ic.carrito_id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemsCarritoConProducto item = new ItemsCarritoConProducto();
                    item.setCarritoId(rs.getInt("carrito_id"));
                    item.setProductoId(rs.getInt("producto_id"));
                    item.setCantidad(rs.getInt("cantidad"));
                    item.setNombreProducto(rs.getString("nombre"));
                    item.setPrecioProducto(rs.getDouble("precio"));
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener items con detalles: " + e.getMessage());
        }
        return lista;
    }

    public double obtenerTotalCarrito(int carritoId) {
        String sql = "SELECT SUM(ic.cantidad * p.precio) AS total " +
                     "FROM tienda_deportiva.items_carrito ic " +
                     "JOIN tienda_deportiva.productos p ON ic.producto_id = p.producto_id " +
                     "WHERE ic.carrito_id = ?";

        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener total carrito: " + e.getMessage());
        }
        return 0.0;
    }

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
}
