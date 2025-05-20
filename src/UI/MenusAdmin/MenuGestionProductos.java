package UI.MenusAdmin;

import java.util.Scanner;

public class MenuGestionProductos {
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
            System.out.println("------ GESTIÓN DE PRODUCTOS ------");
            System.out.println("1. Ver todos los productos");
            System.out.println("2. Buscar producto por ID");
            System.out.println("3. Agregar nuevo producto");
            System.out.println("4. Editar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    limpiarpantalla();
                    System.out.println("Lista de productos:");
                    // Lógica para mostrar productos
                    pausa();
                    break;
                case "2":
                    limpiarpantalla();
                    System.out.println("Buscar producto por ID:");
                    // Lógica para buscar
                    pausa();
                    break;
                case "3":
                    limpiarpantalla();
                    System.out.println("Agregar nuevo producto:");
                    // Lógica para agregar
                    pausa();
                    break;
                case "4":
                    limpiarpantalla();
                    System.out.println("Editar producto:");
                    // Lógica para editar
                    pausa();
                    break;
                case "5":
                    limpiarpantalla();
                    System.out.println("Eliminar producto:");
                    // Lógica para eliminar
                    pausa();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida.");
                    pausa();
            }
        }
    }
}
