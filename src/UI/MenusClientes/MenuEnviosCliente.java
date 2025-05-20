package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;

import Models.Envios;
import Models.Usuarios;
import Controller.Cliente.EnviosClienteController;

public class MenuEnviosCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final EnviosClienteController enviosController = new EnviosClienteController();

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

    public static void mostrarMenuEnvios(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ ENVÍOS ------");
            System.out.println("1. Ver todos los envíos");
            System.out.println("2. Ver mi envío");
            System.out.println("3. Cancelar mi envío");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<Envios> envios = enviosController.listarEnvios();
                        if (envios.isEmpty()) {
                            System.out.println("No hay envíos registrados.");
                        } else {
                            envios.forEach(e -> {
                                System.out.println("ID: " + e.getEnvioId() +
                                                   " | Detalle ID: " + e.getDetalleId() +
                                                   " | Estado: " + e.getEstadoEnvio() +
                                                   " | Código: " + e.getCodigoSeguimiento());
                            });
                        }
                        pausa();
                        break;

                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID de tu envío: ");
                        int envioId = Integer.parseInt(sc.nextLine());
                        Envios envio = enviosController.obtenerEnvioPorId(envioId);
                        if (envio != null) {
                            System.out.println("Envío ID: " + envio.getEnvioId());
                            System.out.println("Detalle ID: " + envio.getDetalleId());
                            System.out.println(" Estado: " + envio.getEstadoEnvio());
                            System.out.println("Código de seguimiento: " + envio.getCodigoSeguimiento());
                        } else {
                            System.out.println("No se encontró el envío.");
                        }
                        pausa();
                        break;

                    case 3:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del envío que deseas cancelar: ");
                        int idCancelar = Integer.parseInt(sc.nextLine());
                        boolean eliminado = enviosController.eliminarEnvio(idCancelar);
                        if (eliminado) {
                            System.out.println("Envío cancelado exitosamente.");
                        } else {
                            System.out.println("No se pudo cancelar el envío.");
                        }
                        pausa();
                        break;

                    case 0:
                        limpiarpantalla();
                        System.out.println("Regresando al menú principal...");
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
