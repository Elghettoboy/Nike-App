package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Productos;

public class ProductosDAO extends BaseRepository {

    public boolean insertar(Productos producto) {
        String sql = "insert into tienda_deportiva.productos (nombre, precio, stock, descripcion) values (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public Productos obtenerPorId(int productoId) { 
        String sql = "select * from  tienda_deportiva.productos where producto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Productos producto = new Productos();
                producto.setProductoId(rs.getInt("producto_id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setDescripcion(rs.getString("descripcion"));
                return producto;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }

    public List<Productos> obtenerTodos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "select * from  tienda_deportiva.productos";

        try (Statement stmt = conn.createStatement();
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

    public boolean actualizar(Productos producto) {
        String sql = "update tienda_deportiva.productos set nombre = ?, precio = ?, stock = ?, descripcion = ? where producto_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getDescripcion());
            stmt.setInt(5, producto.getProductoId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int productoId) {
        String sql = "delete from tienda_deportiva.productos where producto_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }   

}
