package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;

import Models.MetodoDePago;
import Models.Usuarios;
import Controller.Cliente.MetodosPagoClienteController;

public class MenuMetodoDePagoCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final MetodosPagoClienteController metodoController = new MetodosPagoClienteController();

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

    public static void mostrarMenuMetodoDePago(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ MÉTODOS DE PAGO ------");
            System.out.println("1. Ver todos los métodos de pago");
            System.out.println("2. Ver método de pago por ID");
            System.out.println("3. Agregar método de pago");
            System.out.println("4. Editar método de pago");
            System.out.println("5. Eliminar método de pago");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<MetodoDePago> metodos = metodoController.listarMetodos();
                        if (metodos.isEmpty()) {
                            System.out.println("No hay métodos de pago registrados.");
                        } else {
                            metodos.stream()
                                   .filter(m -> m.getUsuarioId() == usuario.getUsuarioId())
                                   .forEach(m -> {
                                       System.out.println("ID: " + m.getMetodoId());
                                       System.out.println("Tipo: " + m.getTipo());
                                       System.out.println("Activo: " + (m.isActivo() ? "Sí" : "No"));
                                       System.out.println("---------------------------");
                                   });
                        }
                        pausa();
                        break;

                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingrese el ID del método de pago: ");
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        MetodoDePago metodo = metodoController.obtenerMetodoPorId(idBuscar);
                        if (metodo != null && metodo.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.println("ID: " + metodo.getMetodoId());
                            System.out.println("Tipo: " + metodo.getTipo());
                            System.out.println("Activo: " + (metodo.isActivo() ? "Sí" : "No"));
                        } else {
                            System.out.println("Método de pago no encontrado o no pertenece al usuario.");
                        }
                        pausa();
                        break;

                    case 3:
                        limpiarpantalla();
                        MetodoDePago nuevoMetodo = new MetodoDePago();
                        nuevoMetodo.setUsuarioId(usuario.getUsuarioId());
                        System.out.print("Tipo de método (Tarjeta, Transferencia, etc.): ");
                        nuevoMetodo.setTipo(sc.nextLine());
                        nuevoMetodo.setActivo(true);

                        boolean agregado = metodoController.agregarMetodo(nuevoMetodo);
                        System.out.println(agregado ? "Método agregado correctamente." : "No se pudo agregar.");
                        pausa();
                        break;

                    case 4:
                        limpiarpantalla();
                        System.out.print("ID del método a editar: ");
                        int idEditar = Integer.parseInt(sc.nextLine());
                        MetodoDePago metodoEditar = metodoController.obtenerMetodoPorId(idEditar);
                        if (metodoEditar != null && metodoEditar.getUsuarioId() == usuario.getUsuarioId()) {
                            System.out.print("Nuevo tipo de método: ");
                            metodoEditar.setTipo(sc.nextLine());
                            System.out.print("¿Está activo? (true/false): ");
                            metodoEditar.setActivo(Boolean.parseBoolean(sc.nextLine()));
                            boolean actualizado = metodoController.actualizarMetodo(metodoEditar);
                            System.out.println(actualizado ? "Método actualizado correctamente." : "No se pudo actualizar.");
                        } else {
                            System.out.println("Método no encontrado o no pertenece al usuario.");
                        }
                        pausa();
                        break;

                    case 5:
                        limpiarpantalla();
                        System.out.print("ID del método a eliminar: ");
                        int idEliminar = Integer.parseInt(sc.nextLine());
                        MetodoDePago metodoEliminar = metodoController.obtenerMetodoPorId(idEliminar);
                        if (metodoEliminar != null && metodoEliminar.getUsuarioId() == usuario.getUsuarioId()) {
                            boolean eliminado = metodoController.eliminarMetodo(idEliminar);
                            System.out.println(eliminado ? "Método eliminado correctamente." : "No se pudo eliminar.");
                        } else {
                            System.out.println("Método no encontrado o no pertenece al usuario.");
                        }
                        pausa();
                        break;

                    case 0:
                        limpiarpantalla();
                        System.out.println("Regresando al menú principal...");
                        pausa();
                        return;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            }
        }
    }
}
