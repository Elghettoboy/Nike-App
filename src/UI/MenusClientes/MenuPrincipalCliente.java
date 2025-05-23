package UI.MenusClientes;

import java.sql.SQLException;
import java.util.Scanner;

import Controller.Cliente.PedidosClienteController;
import Controller.Cliente.ProductosClienteController;
import Controller.Cliente.ReviewClienteController;
import Models.Usuarios;
import UI.MenuPerfil;

public class MenuPrincipalCliente {
    private static final Scanner sc = new Scanner(System.in);

    private static ReviewClienteController reviewController;
    private static ProductosClienteController productosController;
    private static PedidosClienteController pedidosController;

    static {
        try {
            reviewController = new ReviewClienteController();
            productosController = new ProductosClienteController();
            pedidosController = new PedidosClienteController();
        } catch (SQLException e) {
            System.err.println("Error crítico de SQL al inicializar controladores: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error crítico de Runtime al inicializar controladores: " + e.getMessage());
        }
    }

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

    public static void mostrarMenu(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PRINCIPAL ------");
            System.out.println("Bienvenido(a), " + usuario.getNombre());
            System.out.println();
            System.out.println("1. Ver Perfil");
            System.out.println("2. Ver Productos");
            System.out.println("3. Ver Carrito");
            System.out.println("4. Ver Pedidos");
            System.out.println("5. Ver Reseñas");
            System.out.println("6. Wishlist");
            System.out.println("7. Métodos de Pago");
            System.out.println("0. Cerrar Sesión");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        MenuPerfil.mostrarMenuPerfil(usuario);
                        if (usuario.getUsuarioId() == 0)
                            return;
                        pausa();
                        break;
                    case 2:
                        limpiarpantalla();
                        if (productosController != null) {
                            MenuProductosCliente.mostrarMenuProductosCliente(usuario, productosController);
                        } else {
                             System.out.println("Error: El controlador de productos no está inicializado.");
                        }
                        pausa();
                        break;
                    case 3:
                        limpiarpantalla();
                        MenuCarritosClientes.mostrarMenuCarritosClientes(usuario);
                        pausa();
                        break;
                   case 4:
                        limpiarpantalla();
                        if (pedidosController != null && productosController != null) {
                            MenuPedidosCliente.mostrarMenuPedidos(pedidosController, productosController, usuario);
                        } else {
                            System.out.println("Error: Los controladores de pedidos o productos no están inicializados.");
                        }
                        pausa();
                        break;
                    case 5:
                        limpiarpantalla();
                        if (reviewController != null && productosController != null) {
                            MenuReviewCliente.mostrarMenuReview(usuario, reviewController, productosController);
                        } else {
                            System.out.println("Error: Los controladores de review o productos no están disponibles/inicializados.");
                        }
                        pausa();
                        break;
                    case 6:
                        limpiarpantalla();
                        MenuWishlistCliente.mostrarMenuWishlist(usuario);
                        pausa();
                        break;
                    case 7:
                        limpiarpantalla();
                        MenuMetodoDePagoCliente.mostrarMenuMetodoDePago(usuario);
                        pausa();
                        break;
                    case 0:
                        limpiarpantalla();
                        System.out.println("Sesión cerrada. ¡Hasta luego, " + usuario.getNombre() + "!");
                        pausa();
                        return;
                    default:
                        System.out.println("Opción inválida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número válido.");
                pausa();
            } catch (Exception e) {
                System.err.println("Ocurrió un error general en el Menú Principal: " + e.getMessage());
                e.printStackTrace();
                pausa();
            }
        }
    }
}