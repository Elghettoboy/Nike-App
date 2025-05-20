package UI.MenusClientes;

import java.util.Scanner;
import Models.Usuarios;
import UI.MenuPerfil;

public class MenuPrincipalCliente {
    private static final Scanner sc = new Scanner(System.in);

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
            System.out.println("5. Ver Envíos");
            System.out.println("6. Ver Reseñas");
            System.out.println("7. Wishlist");
            System.out.println("8. Métodos de Pago");
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
                        MenuProductosCliente.mostrarMenuProductosCliente(usuario);
                        pausa();
                        break;
                    case 3:
                        limpiarpantalla();
                        MenuCarritosClientes.mostrarMenuCarritosClientes(usuario);
                        pausa();
                        break;
                    case 4:
                        limpiarpantalla();
                        MenuPedidosCliente.mostrarMenuPedidos(usuario);
                        pausa();
                        break;
                    case 5:
                        limpiarpantalla();
                        MenuEnviosCliente.mostrarMenuEnvios(usuario);
                        pausa();
                        break;
                    case 6:
                        limpiarpantalla();
                        MenuReviewCliente.mostrarMenuReview(usuario);
                        pausa();
                        break;
                    case 7:
                        limpiarpantalla();
                        MenuWishlistCliente.mostrarMenuWishlist(usuario);
                        pausa();
                        break;
                    case 8:
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
            }
        }
    }
}
