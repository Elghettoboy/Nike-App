package Controller.Cliente;
import Models.Review;
import Service.Clientes.ReviewClienteService;
import java.util.List;

public class ReviewClienteController {
    private ReviewClienteService reviewClienteService;

    public ReviewClienteController() {
        this.reviewClienteService = new ReviewClienteService();
    }

    public boolean crearReview(Review review) {
        return reviewClienteService.crearReview(review);
    }

    public Review obtenerReviewPorId(int id) {
        return reviewClienteService.obtenerReviewPorId(id);
    }

    public List<Review> listarReviews() {
        return reviewClienteService.listarReviews();
    }

    public boolean actualizarReview(Review review) {
        return reviewClienteService.actualizarReview(review);
    }

    public boolean eliminarReview(int id) {
        return reviewClienteService.eliminarReview(id);
    }       

}
