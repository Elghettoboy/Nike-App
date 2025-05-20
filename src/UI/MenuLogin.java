package UI;

import Models.Usuarios;
import Controller.Cliente.UsuarioClienteController;
import UI.MenusAdmin.MenuPrincipalAdmin;
import UI.MenusClientes.MenuPrincipalCliente;

import java.util.Scanner;

public class MenuLogin {

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

    public static void mostrarMenuLogin() {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ LOGIN ------");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        iniciarSesion();
                        break;
                    case 2:
                        registrarse();
                        break;
                    case 0:
                        limpiarpantalla();
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                        pausa();
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número válido.");
                pausa();
            }
        }
    }

    private static void iniciarSesion() {
        limpiarpantalla();
        System.out.println("------ INICIAR SESIÓN ------");
        System.out.print("Correo: ");
        String correo = sc.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = sc.nextLine();

        Usuarios usuario = usuarioController.obtenerUsuario(correo);

        if (usuario != null && usuarioController.login(correo, contraseña)) {
            limpiarpantalla();
            System.out.println("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
            pausa();
            mostrarMenuPorRol(usuario);
        } else {
            System.out.println("Correo o contraseña incorrectos.");
            pausa();
        }
    }

    private static void registrarse() {
        limpiarpantalla();
        System.out.println("------ REGISTRARSE ------");

        Usuarios nuevo = new Usuarios();

        System.out.print("Nombre: ");
        nuevo.setNombre(sc.nextLine());

        System.out.print("Correo: ");
        nuevo.setCorreo(sc.nextLine());

        System.out.print("Contraseña: ");
        nuevo.setContraseña(sc.nextLine());

        System.out.print("Teléfono: ");
        nuevo.setTelefono(sc.nextLine());

        System.out.print("Rol (admin / cliente): ");
        nuevo.setRol(sc.nextLine().toLowerCase());

        if (usuarioController.registrar(nuevo)) {
            limpiarpantalla();
            System.out.println("Usuario registrado exitosamente.");
        } else {
            System.out.println("Error al registrar usuario.");
        }
        pausa();
    }

    private static void mostrarMenuPorRol(Usuarios usuario) {
        if (usuario.getRol().equalsIgnoreCase("admin")) {
            MenuPrincipalAdmin.mostrarMenu(usuario);
        } else {
            MenuPrincipalCliente.mostrarMenu(usuario);
        }
    }
}
