package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;

import Models.Review;
import Models.Usuarios;
import Controller.Cliente.ReviewClienteController;

public class MenuReviewCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final ReviewClienteController reviewController = new ReviewClienteController();

    public static void limpiarpantalla() {
        try {
            new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    public static void mostrarMenuReview(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ REVIEW ------");
            System.out.println("1. Ver todos los reviews");
            System.out.println("2. Ver review por ID");
            System.out.println("3. Agregar review");
            System.out.println("4. Editar review");
            System.out.println("5. Eliminar review");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<Review> reviews = reviewController.listarReviews();
                        boolean hayReviews = false;
                        for (Review r : reviews) {
                            if (r.getUsuarioId() == usuario.getUsuarioId()) {
                                System.out.println("ID: " + r.getReviewId());
                                System.out.println("Producto ID: " + r.getProductoId());
                                System.out.println("Calificación: " + r.getCalificacion());
                                System.out.println("Comentario: " + r.getComentario());
                                System.out.println("----------------------------");
                                hayReviews = true;
                            }
                        }
                        if (!hayReviews) {
                            System.out.println("No tienes reviews registrados.");
                        }
                        pausa();
                        break;

                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del review: ");
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        Review review = reviewController.obtenerReviewPorId(idBuscar);
                        if (review != null && review.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("ID: " + review.getReviewId());
                            System.out.println("Producto ID: " + review.getProductoId());
                            System.out.println("Calificación: " + review.getCalificacion());
                            System.out.println("Comentario: " + review.getComentario());
                        } else {
                            System.out.println("Review no encontrado o no te pertenece.");
                        }
                        pausa();
                        break;

                    case 3:
                        limpiarpantalla();
                        Review nuevoReview = new Review();
                        nuevoReview.setUsuarioId(usuario.getUsuarioId());
                        System.out.print("ID del producto: ");
                        nuevoReview.setProductoId(Integer.parseInt(sc.nextLine()));
                        System.out.print("Calificación (1-5): ");
                        nuevoReview.setCalificacion(Integer.parseInt(sc.nextLine()));
                        System.out.print("Comentario: ");
                        nuevoReview.setComentario(sc.nextLine());

                        boolean creado = reviewController.crearReview(nuevoReview);
                        System.out.println(creado ? "Review creado exitosamente." : "Error al crear review.");
                        pausa();
                        break;

                    case 4:
                        limpiarpantalla();
                        System.out.print("ID del review a editar: ");
                        int idEditar = Integer.parseInt(sc.nextLine());
                        Review reviewEditar = reviewController.obtenerReviewPorId(idEditar);
                        if (reviewEditar != null && reviewEditar.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.print("Nueva calificación (1-5): ");
                            reviewEditar.setCalificacion(Integer.parseInt(sc.nextLine()));
                            System.out.print("Nuevo comentario: ");
                            reviewEditar.setComentario(sc.nextLine());

                            boolean actualizado = reviewController.actualizarReview(reviewEditar);
                            System.out.println(actualizado ? "Review actualizado correctamente." : "Error al actualizar.");
                        } else {
                            System.out.println("Review no encontrado o no te pertenece.");
                        }
                        pausa();
                        break;

                    case 5:
                        limpiarpantalla();
                        System.out.print("ID del review a eliminar: ");
                        int idEliminar = Integer.parseInt(sc.nextLine());
                        Review reviewEliminar = reviewController.obtenerReviewPorId(idEliminar);
                        if (reviewEliminar != null && reviewEliminar.getUsuarioId() == usuario.getUsuarioId()) {
                            boolean eliminado = reviewController.eliminarReview(idEliminar);
                            System.out.println(eliminado ? "Review eliminado correctamente." : "Error al eliminar.");
                        } else {
                            System.out.println("Review no encontrado o no te pertenece.");
                        }
                        pausa();
                        break;

                    case 0:
                        limpiarpantalla();
                        System.out.println("Regresando al menú principal...");
                        pausa();
                        return;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            }
        }
    }
}
