package UI.MenusAdmin;

import java.util.Scanner;


public class MenuReportes {
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

    public static void mostrarMenu() {
        while (true) {
            limpiarpantalla();
            System.out.println("------ REPORTES Y ESTADÍSTICAS ------");
            System.out.println("1. Reporte de ventas");
            System.out.println("2. Pedidos por estado");
            System.out.println("3. Usuarios registrados");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                case "2":
                case "3":
                    System.out.println("Funcionalidad no implementada aún.");
                    pausa();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida.");
                    pausa();
                    break;
            }
        }
    }
}
