package Controller.Cliente;

import Models.Review;
import Repository.ProductosDAO;
import Service.Clientes.ReviewClienteService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewClienteController {
    private ReviewClienteService reviewClienteService;

    public ReviewClienteController() {
        try {
            this.reviewClienteService = new ReviewClienteService();
        } catch (RuntimeException e) { // Atrapa la RuntimeException del servicio si falla la inicialización del DAO
            System.err.println("Error al inicializar ReviewClienteService en ReviewClienteController: " + e.getMessage());
            throw e; // Relanza para que se sepa que el controlador no se pudo inicializar bien
        }
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

    private String obtenerNombreProductoPorId(int productoId) {
        try {
            ProductosDAO productoDAO = new ProductosDAO();
            return productoDAO.obtenerNombreProductoPorId(productoId);
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener nombre del producto ID " + productoId + ": " + e.getMessage());
            return "Producto desconocido";
        }
    }

    public List<Review> listarReviewsPorUsuario(int usuarioId) {
        if (this.reviewClienteService == null) {
            System.err.println("ReviewClienteService no está inicializado en ReviewClienteController.");
            return new ArrayList<>();
        }
        return reviewClienteService.listarReviewsPorUsuario(usuarioId);
    }
}