package UI;

import Models.Usuarios;
import Controller.Cliente.UsuarioClienteController;
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
        sc.nextLine(); // Espera a que el usuario presione Enter
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
        int intentos = 0;
        boolean sesionIniciada = false;

        while (intentos < 3 && !sesionIniciada) {
            limpiarpantalla();
            System.out.println("------ INICIAR SESIÓN ------");
            if (intentos > 0) {
                System.out.println("Intento " + (intentos + 1) + " de 3.");
            }
            System.out.print("Correo: ");
            String correo = sc.nextLine();
            System.out.print("Contraseña: ");
            String contraseña = sc.nextLine();

            Usuarios usuario = usuarioController.obtenerUsuario(correo);

            if (usuario != null && usuarioController.login(correo, contraseña)) {
                limpiarpantalla();
                System.out.println("Inicio de sesión exitoso. ¡Bienvenido, " + usuario.getNombre() + "!");
                sesionIniciada = true; // Marcar como sesión iniciada
                pausa();
                MenuPrincipalCliente.mostrarMenu(usuario);
            } else {
                intentos++;
                System.out.println("Correo o contraseña incorrectos.");
                if (intentos < 3) {
                    System.out.println("Te quedan " + (3 - intentos) + " intentos.");
                }
                pausa();
            }
        }

        if (!sesionIniciada && intentos == 3) {
            limpiarpantalla();
            System.out.println("Has excedido el número de intentos permitidos.");
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
        nuevo.setContraseña(sc.nextLine()); // Asegúrate de que el método se llame setContraseña

        System.out.print("Teléfono: ");
        nuevo.setTelefono(sc.nextLine());

        if (usuarioController.registrar(nuevo)) {
            limpiarpantalla();
            System.out.println("Usuario registrado exitosamente.");
        } else {
            System.out.println("Error al registrar usuario.");
        }
        pausa();
    }
}