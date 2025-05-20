package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Carrito;

public class CarritoDAO extends BaseRepository {

    public boolean insertar(Carrito carrito) {
        String sql = "insert into tienda_deportiva.carritos (usuario_id, fecha_creacion) values (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carrito.getUsuarioId());
            stmt.setTimestamp(2, carrito.getFechaCreacion());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar carrito: " + e.getMessage());
            return false;
        }
    }

    public Carrito obtenerPorId(int carritoId) {
        String sql = "select * from  tienda_deportiva.carritos where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Carrito carrito = new Carrito();
                carrito.setCarritoId(rs.getInt("carrito_id"));
                carrito.setUsuarioId(rs.getInt("usuario_id"));
                carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                return carrito;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener carrito: " + e.getMessage());
        }
        return null;
    }

    public List<Carrito> obtenerTodos() {
        List<Carrito> lista = new ArrayList<>();
        String sql = "select * from  tienda_deportiva.carritos";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Carrito carrito = new Carrito();
                carrito.setCarritoId(rs.getInt("carrito_id"));
                carrito.setUsuarioId(rs.getInt("usuario_id"));
                carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                lista.add(carrito);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar carritos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Carrito carrito) {
        String sql = "update tienda_deportiva.carritos set usuario_id = ?, fecha_creacion = ? where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carrito.getUsuarioId());
            stmt.setTimestamp(2, carrito.getFechaCreacion());
            stmt.setInt(3, carrito.getCarritoId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar carrito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int carritoId) {
        String sql = "delete from tienda_deportiva.carritos where carrito_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar carrito: " + e.getMessage());
            return false;
        }
    }
}
