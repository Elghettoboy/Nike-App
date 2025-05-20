package Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;  
import Models.Review;

public class ReviewDAO extends BaseRepository {

    public boolean insertar(Review review) {
        String sql = "insert into tienda_deportiva.reviews (usuario_id, producto_id, calificacion, comentario) values (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "select * from  tienda_deportiva.reviews where review_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "select * from  tienda_deportiva.reviews";

        try (Statement stmt = conn.createStatement();
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

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar review: " + e.getMessage());
            return false;
        }
    }   

}
