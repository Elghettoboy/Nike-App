package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Usuarios;
import Config.ConnectionDB;

public class UsuariosDAO {

    public boolean insertar(Usuarios usuario) {
        String query = "insert into tienda_deportiva.usuarios (nombre, correo, contraseña, telefono) values (?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConn();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getTelefono());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuarios obtenerUsuarioPorCorreo(String correo) {
        String query = "select * from tienda_deportiva.usuarios where correo = ?";

        try (Connection conn = ConnectionDB.getConn();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setTelefono(rs.getString("telefono"));

                return usuario;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuario: " + e.getMessage());
        }
        return null;
    }

    public List<Usuarios> consultar() {
        List<Usuarios> lista = new ArrayList<>();
        String query = "select * from  tienda_deportiva.usuarios";

        try (Connection conn = ConnectionDB.getConn();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioId(rs.getInt("usuario_id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContraseña(rs.getString("contraseña"));
                usuario.setTelefono(rs.getString("telefono"));

                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Usuarios usuario) {
        String sql = "update tienda_deportiva.usuarios set nombre = ?, correo = ?, contraseña = ?, telefono = ? WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getContraseña());
            stmt.setString(4, usuario.getTelefono());
            stmt.setInt(5, usuario.getUsuarioId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int usuarioId) {
        String query = "delete from tienda_deportiva.usuarios where usuario_id = ?";

        try (Connection conn = ConnectionDB.getConn();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public String obtenerNombreClientePorId(int clienteId) {
        String sql = "SELECT nombre FROM tienda_deportiva.usuarios WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener nombre del cliente: " + e.getMessage());
        }
        return "Desconocido";
    }

    public Usuarios obtenerPorId(int usuarioId) {
        String sql = "SELECT * FROM tienda_deportiva.usuarios WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuarios usuario = new Usuarios();
                    usuario.setUsuarioId(rs.getInt("usuario_id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContraseña(rs.getString("contraseña")); // Considera no cargar la contraseña a menos que
                                                                       // sea necesario
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setDireccion(rs.getString("direccion")); // Asumiendo que la columna 'direccion' existe
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }
}
