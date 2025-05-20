package UI.MenusAdmin;

import java.util.Scanner;
import Models.Usuarios;

public class MenuPrincipalAdmin {
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

    public static void mostrarMenu(Usuarios admin) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PRINCIPAL ADMIN ------");
            System.out.println("Bienvenido(a), " + admin.getNombre());
            System.out.println();
            System.out.println("1. Gestionar Usuarios");
            System.out.println("2. Gestionar Productos");
            System.out.println("3. Gestionar Pedidos");
            System.out.println("4. Gestionar Envíos");
            System.out.println("5. Gestionar Reseñas");
            System.out.println("6. Ver Reportes");
            System.out.println("0. Cerrar Sesión");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        MenuGestionUsuarios.mostrarMenu();
                        pausa();
                        break;
                    case 2:
                        limpiarpantalla();
                        MenuGestionProductos.mostrarMenu();
                        pausa();
                        break;
                    case 3:
                        limpiarpantalla();
                        MenuGestionPedidos.mostrarMenu();
                        pausa();
                        break;
                    case 4:
                        limpiarpantalla();
                        MenuGestionEnvios.mostrarMenu();
                        pausa();
                        break;
                    case 5:
                        limpiarpantalla();
                        MenuGestionReview.mostrarMenu();
                        pausa();
                        break;
                    case 6:
                        limpiarpantalla();
                        MenuReportes.mostrarMenu();
                        pausa();
                        break;
                    case 0:
                        limpiarpantalla();
                        System.out.println("Sesión cerrada. ¡Hasta pronto, " + admin.getNombre() + "!");
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
