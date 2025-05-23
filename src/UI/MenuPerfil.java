package UI;

import java.util.Scanner;
import Models.Usuarios;
import Controller.Cliente.UsuarioClienteController;


public class MenuPerfil {
private static final Scanner sc = new Scanner(System.in);
private static final UsuarioClienteController usuarioController = new UsuarioClienteController();

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

public static void mostrarMenuPerfil(Usuarios usuario) {
    while (true) {
        limpiarpantalla();
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Correo: " + usuario.getCorreo());
        System.out.println("Teléfono: " + usuario.getTelefono());
        
        System.out.println();
        System.out.println("------ MENÚ PERFIL ------");
        System.out.println("1. Actualizar Perfil");
        System.out.println("2. Eliminar Perfil");
        System.out.println("0. Salir");
        System.out.print("\nElige una opción: ");

        try {
            int opc = Integer.parseInt(sc.nextLine());

            switch (opc) {
                case 1:
                    ActualizarPerfil(usuario);
                    break;
                case 2:
                    EliminarPerfil(usuario);
                    return;
                case 0:
                    limpiarpantalla();
                    return;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
                    pausa();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un número válido.");
            pausa();
        }
    }
}

public static void ActualizarPerfil(Usuarios usuario) {
    while (true) {
        limpiarpantalla();
        System.out.println("------ ACTUALIZAR PERFIL ------");
        System.out.println("1. Cambiar nombre");
        System.out.println("2. Cambiar correo");
        System.out.println("3. Cambiar contraseña");
        System.out.println("4. Cambiar teléfono");
        System.out.println("0. Salir");
        System.out.print("\nElige una opción: ");

        try {
            int opc = Integer.parseInt(sc.nextLine());

            switch (opc) {
                case 1:
                    System.out.print("Nuevo nombre: ");
                    usuario.setNombre(sc.nextLine());
                    if (usuarioController.actualizar(usuario)) {
                        System.out.println("Nombre actualizado correctamente.");
                    } else {
                        System.out.println("Error al actualizar nombre.");
                    }
                    break;
                case 2:
                    System.out.print("Nuevo correo: ");
                    usuario.setCorreo(sc.nextLine());
                    if (usuarioController.actualizar(usuario)) {
                        System.out.println("Correo actualizado correctamente.");
                    } else {
                        System.out.println("Error al actualizar correo.");
                    }
                    break;
                case 3:
                    System.out.print("Nueva contraseña: ");
                    usuario.setContraseña(sc.nextLine());
                    if (usuarioController.actualizar(usuario)) {
                        System.out.println("Contraseña actualizada correctamente.");
                    } else {
                        System.out.println("Error al actualizar contraseña.");
                    }
                    break;
                case 4:
                    System.out.print("Nuevo teléfono: ");
                    usuario.setTelefono(sc.nextLine());
                    if (usuarioController.actualizar(usuario)) {
                        System.out.println("Teléfono actualizado correctamente.");
                    } else {
                        System.out.println("Error al actualizar teléfono.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }
            pausa();
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresa un número válido.");
            pausa();
        }
    }
}

public static void EliminarPerfil(Usuarios usuario) {
    limpiarpantalla();
    System.out.println("------ ELIMINAR PERFIL ------");
    System.out.print("¿Estás seguro que deseas eliminar tu perfil? (S/N): ");
    String respuesta = sc.nextLine().trim().toUpperCase();

    if (respuesta.equals("S")) {
        if (usuarioController.eliminar(usuario.getUsuarioId())) {
            System.out.println("Perfil eliminado exitosamente.");
            pausa();
            MenuLogin.mostrarMenuLogin();
        } else {
            System.out.println("Error al eliminar el perfil.");
            pausa();
        }
    } else {
        System.out.println("Eliminación cancelada.");
        pausa();
    }
}

}