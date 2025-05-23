package Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.Review;
import Config.ConnectionDB;

public class ReviewDAO {

    public boolean insertar(Review review) {
        String sql = "insert into tienda_deportiva.reviews (usuario_id, producto_id, calificacion, comentario) values (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getUsuarioId());
            stmt.setInt(2, review.getProductoId());
            stmt.setInt(3, review.getCalificacion());
            stmt.setString(4, review.getComentario());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar review: " + e.getMessage());
            return false;
        }
    }

    public Review obtenerPorId(int reviewId) {
        String sql = "select * from tienda_deportiva.reviews where review_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setUsuarioId(rs.getInt("usuario_id"));
                review.setProductoId(rs.getInt("producto_id"));
                review.setCalificacion(rs.getInt("calificacion"));
                review.setComentario(rs.getString("comentario"));
                return review;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener review: " + e.getMessage());
        }
        return null;
    }

    public List<Review> obtenerTodos() {
        List<Review> lista = new ArrayList<>();
        String sql = "select * from tienda_deportiva.reviews";
        try (Connection conn = ConnectionDB.getConn();
            Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setUsuarioId(rs.getInt("usuario_id"));
                review.setProductoId(rs.getInt("producto_id"));
                review.setCalificacion(rs.getInt("calificacion"));
                review.setComentario(rs.getString("comentario"));
                lista.add(review);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reviews: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Review review) {
        String sql = "update tienda_deportiva.reviews set usuario_id = ?, producto_id = ?, calificacion = ?, comentario = ? where review_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getUsuarioId());
            stmt.setInt(2, review.getProductoId());
            stmt.setInt(3, review.getCalificacion());
            stmt.setString(4, review.getComentario());
            stmt.setInt(5, review.getReviewId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar review: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int reviewId) {
        String sql = "delete from tienda_deportiva.reviews where review_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar review: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorUsuarioId(int usuarioId) {
        String sql = "DELETE FROM tienda_deportiva.reviews WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar reviews por usuario_id: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorProductoId(int productoId) {
        String sql = "DELETE FROM tienda_deportiva.reviews WHERE producto_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar reviews por producto_id: " + e.getMessage());
            return false;
        }
    }
    public List<Review> listarPorUsuarioId(int usuarioId) {
        List<Review> lista = new ArrayList<>();
        String sql = "SELECT * FROM tienda_deportiva.reviews WHERE usuario_id = ?";
        try (Connection conn = ConnectionDB.getConn();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setUsuarioId(rs.getInt("usuario_id"));
                    review.setProductoId(rs.getInt("producto_id"));
                    review.setCalificacion(rs.getInt("calificacion"));
                    review.setComentario(rs.getString("comentario"));
                    lista.add(review);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reviews por usuario_id: " + e.getMessage());
        }
        return lista;
    }
}