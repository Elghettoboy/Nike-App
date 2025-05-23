package UI.MenusClientes;

import Controller.Cliente.ProductosClienteController;
import Controller.Cliente.ReviewClienteController;
import Repository.ProductosDAO;
import Models.Productos;
import Models.Review;
import Models.Usuarios;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuReviewCliente {
    private static final Scanner sc = new Scanner(System.in);

    public static void limpiarpantalla() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }

    public static void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    // Helper method to get product name (remains unchanged)
    private static String obtenerNombreProductoPorId(int productoId) {
        try {
            // Consider passing ProductosClienteController if it has a method to get product name
            // to avoid direct DAO instantiation here if desired for consistency.
            // For now, keeping it as is since it's existing logic.
            ProductosDAO productoDAO = new ProductosDAO();
            return productoDAO.obtenerNombreProductoPorId(productoId);
        } catch (SQLException e) {
            System.err.println("Error al cargar nombre del producto ID " + productoId + ": " + e.getMessage());
            return "Producto ID " + productoId + " (Nombre no disponible)";
        }
    }

    private static void mostrarProductosParaSeleccionReview(List<Productos> productos) {
        System.out.println("--- Productos Disponibles (Página Actual) ---");
        if (productos.isEmpty()) {
            return;
        }
        for (Productos p : productos) {
            System.out.println(String.format("ID: %-5d | Nombre: %-30s",
                    p.getProductoId(),
                    p.getNombre()));
        }
        System.out.println("------------------------------------------------------");
    }

    private static void agregarReviewUI(Usuarios usuarioLogueado,
                                        ProductosClienteController productosController,
                                        ReviewClienteController reviewController) {
        limpiarpantalla();
        System.out.println("------ AGREGAR NUEVA REVIEW ------");

        Productos productoAResenar = null;
        int idProductoSeleccionado = -1;

        boolean productoSeleccionadoValido = false;
        while (!productoSeleccionadoValido) {
            System.out.println("\nSeleccione un producto de la lista para dejar su reseña:");
            int productosPorPagina = 10;
            int offsetActual = 0;
            boolean quiereVerMasPaginas = true;
            boolean algunProductoMostrado = false;

            while (quiereVerMasPaginas) {
                List<Productos> paginaDeProductos = productosController.listarProductosPaginados(productosPorPagina, offsetActual);

                if (paginaDeProductos.isEmpty()) {
                    if (offsetActual == 0) {
                        System.out.println("No hay productos disponibles en el catálogo para reseñar.");
                        pausa();
                        return;
                    } else {
                        System.out.println("--- No hay más productos para mostrar ---");
                    }
                    quiereVerMasPaginas = false;
                } else {
                    mostrarProductosParaSeleccionReview(paginaDeProductos);
                    algunProductoMostrado = true;
                    offsetActual += paginaDeProductos.size();

                    if (paginaDeProductos.size() == productosPorPagina) {
                        System.out.print("Ver más productos (S) / Ingresar ID del producto a reseñar (ID): ");
                        String respuesta = sc.nextLine().trim();
                        if (!respuesta.equalsIgnoreCase("S")) {
                            quiereVerMasPaginas = false;
                        }
                    } else {
                        System.out.println("--- Fin de la lista de productos ---");
                        quiereVerMasPaginas = false;
                    }
                }
            }

            if (!algunProductoMostrado) {
                System.out.println("No se mostraron productos para seleccionar.");
                pausa();
                return;
            }

            System.out.print("\nIngrese el ID del producto que desea reseñar (o 0 para cancelar): ");
            try {
                idProductoSeleccionado = Integer.parseInt(sc.nextLine());
                if (idProductoSeleccionado == 0) {
                    System.out.println("Agregar reseña cancelado.");
                    pausa();
                    return;
                }
                productoAResenar = productosController.obtenerProductoPorId(idProductoSeleccionado);
                if (productoAResenar != null) {
                    productoSeleccionadoValido = true;
                } else {
                    System.out.println("Producto con ID " + idProductoSeleccionado + " no encontrado. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Debe ser un número.");
            }
        }

        System.out.println("\nReseñando producto: " + productoAResenar.getNombre());
        int calificacion = 0;
        while (calificacion < 1 || calificacion > 5) {
            System.out.print("Calificación (1-5 estrellas): ");
            try {
                calificacion = Integer.parseInt(sc.nextLine());
                if (calificacion < 1 || calificacion > 5) {
                    System.out.println("Por favor, ingrese un número entre 1 y 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Calificación inválida. Ingrese un número.");
            }
        }

        System.out.print("Comentario (opcional, presiona ENTER para omitir): ");
        String comentario = sc.nextLine().trim();

        Review nuevoReview = new Review();
        nuevoReview.setUsuarioId(usuarioLogueado.getUsuarioId());
        nuevoReview.setProductoId(productoAResenar.getProductoId());
        nuevoReview.setCalificacion(calificacion);
        nuevoReview.setComentario(comentario.isEmpty() ? null : comentario);

        if (reviewController.crearReview(nuevoReview)) {
            System.out.println("¡Reseña agregada exitosamente para el producto '" + productoAResenar.getNombre() + "'!");
        } else {
            System.out.println("Error al agregar la reseña. Intente de nuevo.");
        }
        pausa();
    }

    /**
     * Lista todas las reviews del usuario de forma detallada.
     * @param usuario El usuario logueado.
     * @param reviewController El controlador de reviews.
     * @return true si se mostraron reviews, false si no hay reviews.
     */
    private static boolean listarMisReviewsDetalladamente(Usuarios usuario, ReviewClienteController reviewController) {
        List<Review> reviews = reviewController.listarReviewsPorUsuario(usuario.getUsuarioId());
        if (reviews == null || reviews.isEmpty()) {
            System.out.println("No has escrito ninguna reseña aún.");
            return false; // No reviews to show
        }

        System.out.println("------ MIS REVIEWS ------");
        for (Review r : reviews) {
            String nombreProducto = obtenerNombreProductoPorId(r.getProductoId());
            System.out.println("ID Review: " + r.getReviewId());
            System.out.println("Producto: " + nombreProducto + " (ID: " + r.getProductoId() + ")");
            System.out.println("Usuario ID: " + r.getUsuarioId() + " | Calificación: " + r.getCalificacion() + " estrellas");
            System.out.println("Comentario: " + (r.getComentario() == null || r.getComentario().isEmpty() ? "(Sin comentario)" : r.getComentario()) );
            System.out.println("----------------------------");
        }
        return true; // Reviews were shown
    }


    public static void mostrarMenuReview(Usuarios usuario,
                                         ReviewClienteController reviewController,
                                         ProductosClienteController productosController) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ REVIEW ------");
            System.out.println("1. Ver mis reviews");
            System.out.println("2. Ver review por ID (de mis reviews)");
            System.out.println("3. Agregar review a un producto");
            System.out.println("4. Editar mi review");
            System.out.println("5. Eliminar mi review");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        listarMisReviewsDetalladamente(usuario, reviewController);
                        pausa();
                        break;

                    case 2: // Ver review por ID
                        limpiarpantalla();
                        System.out.println("------ VER REVIEW POR ID (DE MIS REVIEWS) ------");
                        boolean tieneReviewsParaVer = listarMisReviewsDetalladamente(usuario, reviewController);

                        if (!tieneReviewsParaVer) {
                            pausa();
                            break;
                        }

                        System.out.print("\nDe la lista anterior, ingresa el ID del review que deseas ver: ");
                        int idBuscar;
                        try {
                            idBuscar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido.");
                            pausa();
                            break;
                        }
                        Review review = reviewController.obtenerReviewPorId(idBuscar);
                        if (review != null && review.getUsuarioId() == usuario.getUsuarioId()) {
                            String nombreProducto = obtenerNombreProductoPorId(review.getProductoId());
                            System.out.println("\n------ DETALLE DE MI REVIEW (ID: "+ review.getReviewId() + ") ------");
                            // System.out.println("ID Review: " + review.getReviewId()); // Already in header
                            System.out.println("Producto: " + nombreProducto + " (ID: " + review.getProductoId() + ")");
                            System.out.println("Calificación: " + review.getCalificacion() + " estrellas");
                            System.out.println("Comentario: " + (review.getComentario() == null || review.getComentario().isEmpty() ? "(Sin comentario)" : review.getComentario()));
                            System.out.println("------------------------------------");
                        } else if (review != null && review.getUsuarioId() != usuario.getUsuarioId()) {
                            System.out.println("Esta review no te pertenece o el ID es incorrecto.");
                        } else {
                            System.out.println("Review no encontrado con el ID " + idBuscar + ".");
                        }
                        pausa();
                        break;

                    case 3:
                        agregarReviewUI(usuario, productosController, reviewController);
                        break;

                    case 4: // Editar mi review
                        limpiarpantalla();
                        System.out.println("------ EDITAR MI REVIEW ------");
                        boolean tieneReviewsParaEditar = listarMisReviewsDetalladamente(usuario, reviewController);

                        if (!tieneReviewsParaEditar) {
                            pausa();
                            break;
                        }

                        System.out.print("\nDe la lista anterior, ingresa el ID del review que deseas editar: ");
                        int idEditar;
                         try {
                            idEditar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido.");
                            pausa();
                            break;
                        }
                        Review reviewEditar = reviewController.obtenerReviewPorId(idEditar);
                        if (reviewEditar != null && reviewEditar.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("\n--- Editando Review ID: " + reviewEditar.getReviewId() + " ---");
                            System.out.println("Producto: " + obtenerNombreProductoPorId(reviewEditar.getProductoId()));
                            System.out.println("Calificación actual: " + reviewEditar.getCalificacion() + " estrellas");
                            System.out.println("Comentario actual: " + (reviewEditar.getComentario() == null || reviewEditar.getComentario().isEmpty() ? "(Sin comentario)" : reviewEditar.getComentario()));
                            System.out.println("------------------------------------");

                            System.out.print("Nueva calificación (1-5 estrellas), o Enter para no cambiar: ");
                            String calificacionStr = sc.nextLine();
                            if (!calificacionStr.trim().isEmpty()) {
                                try {
                                    int nuevaCal = Integer.parseInt(calificacionStr);
                                    if (nuevaCal >= 1 && nuevaCal <= 5) {
                                        reviewEditar.setCalificacion(nuevaCal);
                                        System.out.println("Calificación actualizada a: " + nuevaCal + " estrellas.");
                                    } else {
                                        System.out.println("Calificación inválida ("+ nuevaCal +"), no se cambió. Debe ser entre 1 y 5.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Entrada inválida para calificación, no se cambió.");
                                }
                            }

                            System.out.print("Nuevo comentario (o Enter para no cambiar, '-' para borrar): ");
                            String comentarioStr = sc.nextLine(); // No trim yet to differentiate empty from space
                            if (comentarioStr.equals("-")) {
                                reviewEditar.setComentario(null);
                                System.out.println("Comentario eliminado.");
                            } else if (!comentarioStr.isEmpty()) { // If not "-", and not empty string from just pressing Enter
                                reviewEditar.setComentario(comentarioStr.trim());
                                System.out.println("Comentario actualizado.");
                            } else {
                                // If user just pressed Enter, comentarioStr is empty, do nothing to comment
                                System.out.println("Comentario no modificado.");
                            }


                            if (reviewController.actualizarReview(reviewEditar)) {
                                System.out.println("\nReview actualizado correctamente en la base de datos.");
                            } else {
                                System.out.println("\nError al actualizar el review en la base de datos.");
                            }
                        } else if (reviewEditar != null && reviewEditar.getUsuarioId() != usuario.getUsuarioId()) {
                            System.out.println("Esta review no te pertenece o el ID es incorrecto.");
                        } else {
                            System.out.println("Review no encontrado con el ID " + idEditar + ".");
                        }
                        pausa();
                        break;

                    case 5: // Eliminar mi review
                        limpiarpantalla();
                        System.out.println("------ ELIMINAR MI REVIEW ------");
                        boolean tieneReviewsParaEliminar = listarMisReviewsDetalladamente(usuario, reviewController);

                        if (!tieneReviewsParaEliminar) {
                            pausa();
                            break;
                        }

                        System.out.print("\nDe la lista anterior, ingresa el ID del review que deseas eliminar: ");
                        int idEliminar;
                        try {
                            idEliminar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID inválido.");
                            pausa();
                            break;
                        }
                        Review reviewEliminar = reviewController.obtenerReviewPorId(idEliminar);
                        if (reviewEliminar != null && reviewEliminar.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("\n--- Detalles del Review a Eliminar (ID: " + reviewEliminar.getReviewId() + ") ---");
                            System.out.println("Producto: " + obtenerNombreProductoPorId(reviewEliminar.getProductoId()));
                            System.out.println("Calificación: " + reviewEliminar.getCalificacion() + " estrellas");
                            System.out.println("Comentario: " + (reviewEliminar.getComentario() == null || reviewEliminar.getComentario().isEmpty() ? "(Sin comentario)" : reviewEliminar.getComentario()));
                            System.out.println("------------------------------------------");
                            System.out.print("¿Estás seguro que deseas eliminar esta review? (S/N): ");
                            if(sc.nextLine().trim().equalsIgnoreCase("S")){
                                if (reviewController.eliminarReview(idEliminar)) {
                                    System.out.println("Review eliminado correctamente.");
                                } else {
                                    System.out.println("Error al eliminar el review.");
                                }
                            } else {
                                System.out.println("Eliminación cancelada.");
                            }
                        } else if (reviewEliminar != null && reviewEliminar.getUsuarioId() != usuario.getUsuarioId()) {
                             System.out.println("Esta review no te pertenece o el ID es incorrecto.");
                        } else {
                            System.out.println("Review no encontrado con el ID " + idEliminar + ".");
                        }
                        pausa();
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado en Menú Review: " + e.getMessage());
                e.printStackTrace(); // Helpful for debugging
                pausa();
            }
        }
    }
}