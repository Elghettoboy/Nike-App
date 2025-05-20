package UI.MenusAdmin;

import java.util.Scanner;

public class MenuGestionUsuarios {
    private static final Scanner sc = new Scanner(System.in);

    public static void mostrarMenu() {
        while (true) {
            System.out.println("------ GESTIÓN DE USUARIOS ------");
            System.out.println("1. Ver todos los usuarios");
            System.out.println("2. Buscar usuario");
            System.out.println("3. Agregar usuario");
            System.out.println("4. Editar usuario");
            System.out.println("5. Eliminar usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    // llamar DAO para listar usuarios
                    break;
                case "2":
                    // buscar por ID o nombre
                    break;
                case "3":
                    // capturar datos y crear usuario
                    break;
                case "4":
                    // seleccionar y editar usuario
                    break;
                case "5":
                    // eliminar usuario por ID
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
            MenuPrincipalAdmin.pausa();
        }
    }
}
