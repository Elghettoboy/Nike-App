package Service.Clientes;
import Models.Review;
import Repository.ReviewDAO;

import java.util.List;

public class ReviewClienteService {

    private ReviewDAO reviewDAO;

    public ReviewClienteService() {
        this.reviewDAO = new ReviewDAO();
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

}
