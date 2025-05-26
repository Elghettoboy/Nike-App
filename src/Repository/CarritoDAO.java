package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Carrito;
import Config.ConnectionDB;

public class CarritoDAO {

    private ItemsCarritoDAO itemsCarritoDAO;

    public CarritoDAO() throws SQLException {
        this.itemsCarritoDAO = new ItemsCarritoDAO();
    }

    public boolean insertar(Carrito carrito) {
        String sql = "INSERT INTO tienda_deportiva.carritos (usuario_id, fecha_creacion) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, carrito.getUsuarioId());
            if (carrito.getFechaCreacion() == null) {
                carrito.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            }
            stmt.setTimestamp(2, carrito.getFechaCreacion());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        carrito.setCarritoId(generatedKeys.getInt(1));
                    } else {
                        return false; 
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar carrito: " + e.getMessage());
            return false;
        }
    }

    public Carrito obtenerPorId(int carritoId) {
        String sql = "SELECT * FROM tienda_deportiva.carritos WHERE carrito_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrito carrito = new Carrito();
                    carrito.setCarritoId(rs.getInt("carrito_id"));
                    carrito.setUsuarioId(rs.getInt("usuario_id"));
                    carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    return carrito;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener carrito: " + e.getMessage());
        }
        return null;
    }
    
    public Carrito obtenerActivoPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM tienda_deportiva.carritos WHERE usuario_id = ? ORDER BY fecha_creacion DESC, carrito_id DESC LIMIT 1";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrito carrito = new Carrito();
                    carrito.setCarritoId(rs.getInt("carrito_id"));
                    carrito.setUsuarioId(rs.getInt("usuario_id"));
                    carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    return carrito;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener carrito activo por usuario_id: " + e.getMessage());
        }
        return null;
    }

    public List<Carrito> obtenerTodosLosCarritos() {
        String sql = "SELECT c.*, u.nombre AS nombre_usuario " +
                     "FROM tienda_deportiva.carritos c " +
                     "JOIN tienda_deportiva.usuarios u ON c.usuario_id = u.usuario_id ORDER BY c.carrito_id";
        List<Carrito> carritos = new ArrayList<>();
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Carrito carrito = new Carrito();
                carrito.setCarritoId(rs.getInt("carrito_id"));
                carrito.setUsuarioId(rs.getInt("usuario_id"));
                carrito.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                carrito.setNombreUsuario(rs.getString("nombre_usuario"));
                carritos.add(carrito);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar carritos: " + e.getMessage());
        }
        return carritos;
    }

    public boolean actualizar(Carrito carrito) {
        String sql = "UPDATE tienda_deportiva.carritos SET usuario_id = ?, fecha_creacion = ? WHERE carrito_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        if (itemsCarritoDAO == null) {
             System.err.println("ItemsCarritoDAO no inicializado en CarritoDAO.");
             return false;
        }
        boolean itemsEliminados = itemsCarritoDAO.eliminarPorCarritoId(carritoId);
        if (!itemsEliminados) {
            System.out.println("Advertencia: No se eliminaron todos los ítems (o no había) para el carrito_id: " + carritoId);
        }

        String sql = "DELETE FROM tienda_deportiva.carritos WHERE carrito_id = ?";
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carritoId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar carrito con id " + carritoId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorUsuarioId(int usuarioId) {
        // Paso 1: Obtener todos los IDs de los carritos asociados con el usuarioId.
        String sqlSelectCarritos = "SELECT carrito_id FROM tienda_deportiva.carritos WHERE usuario_id = ?";
        List<Integer> idsDeCarritosAEliminar = new ArrayList<>();
        
        try (Connection conn = ConnectionDB.getConn();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelectCarritos)) {
            
            stmtSelect.setInt(1, usuarioId);
            try (ResultSet rs = stmtSelect.executeQuery()) {
                while (rs.next()) {
                    idsDeCarritosAEliminar.add(rs.getInt("carrito_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener carritos para el usuario_id " + usuarioId + " durante la eliminación: " + e.getMessage());
            return false; // Fallo crítico al obtener la lista de carritos, no se puede proceder.
        }

        if (idsDeCarritosAEliminar.isEmpty()) {
            // No hay carritos para este usuario, se considera una "eliminación" exitosa.
            System.out.println("No se encontraron carritos para el usuario_id: " + usuarioId + ". No hay nada que eliminar.");
            return true;
        }

        boolean todosEliminadosCorrectamente = true;
        // Paso 2: Iterar sobre cada carritoId y llamar al método eliminar(carritoId) existente.
        for (int carritoId : idsDeCarritosAEliminar) {
            if (!this.eliminar(carritoId)) {
                // El método `eliminar(carritoId)` ya imprime su propio mensaje de error.
                System.err.println("Fallo al procesar la eliminación completa del carrito con id " + carritoId + " para el usuario " + usuarioId + ".");
                todosEliminadosCorrectamente = false;
                // Se podría decidir detener todo el proceso aquí si una eliminación falla:
                // return false; 
                // Por ahora, se intentará eliminar los demás carritos.
            }
        }

        if (todosEliminadosCorrectamente) {
            System.out.println("Todos los carritos (" + idsDeCarritosAEliminar.size() + ") del usuario_id: " + usuarioId + " han sido procesados para eliminación.");
        } else {
            System.err.println("No todos los carritos del usuario_id: " + usuarioId + " pudieron ser eliminados completamente.");
        }
        
        return todosEliminadosCorrectamente;
    }
}
