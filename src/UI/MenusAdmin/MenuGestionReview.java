package UI.MenusAdmin;

import java.util.Scanner;

public class MenuGestionReview {
    
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
            System.out.println("------ GESTIÓN DE RESEÑAS ------");
            System.out.println("1. Ver todas las reseñas");
            System.out.println("2. Eliminar reseña");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                case "2":
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
