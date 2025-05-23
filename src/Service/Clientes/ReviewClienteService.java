package Service.Clientes;

import Models.Review;
import Repository.ReviewDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewClienteService {

    private ReviewDAO reviewDAO;

    public ReviewClienteService() {
        try {
            this.reviewDAO = new ReviewDAO();
        } catch (Exception e) {
            
            System.err.println("Error al inicializar ReviewDAO en ReviewClienteService: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de reviews.", e);
        }
    }

    public boolean crearReview(Review review) {
        return reviewDAO.insertar(review);
    }

    public Review obtenerReviewPorId(int id) {
        return reviewDAO.obtenerPorId(id);
    }

    public List<Review> listarReviews() {
        return reviewDAO.obtenerTodos();
    }

    public boolean actualizarReview(Review review) {
        return reviewDAO.actualizar(review);
    }

    public boolean eliminarReview(int id) {
        return reviewDAO.eliminar(id);
    }

    public List<Review> listarReviewsPorUsuario(int usuarioId) {
        if (this.reviewDAO == null) {
            System.err.println("ReviewDAO no está inicializado en ReviewClienteService.");
            return new ArrayList<>();
        }
        return reviewDAO.listarPorUsuarioId(usuarioId);
    }
}