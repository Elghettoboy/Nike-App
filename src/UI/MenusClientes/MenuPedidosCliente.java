package UI.MenusClientes;

import Controller.Cliente.PedidosClienteController;
import Models.Pedidos;
import Models.DetallesPedido;
import Models.Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPedidosCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final PedidosClienteController pedidosController = new PedidosClienteController();

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

    public static void mostrarMenuPedidos(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PEDIDOS ------");
            System.out.println("1. Ver todos los pedidos");
            System.out.println("2. Ver pedido por ID");
            System.out.println("3. Realizar pedido");
            System.out.println("4. Cancelar pedido");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");
            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<Pedidos> pedidos = pedidosController.listarPedidos();
                        if (pedidos.isEmpty()) {
                            System.out.println("No hay pedidos registrados.");
                        } else {
                            for (Pedidos p : pedidos) {
                                System.out.println("Pedido ID: " + p.getPedidoId() + " | Cliente ID: " + p.getUsuarioId());
                            }
                        }
                        pausa();
                        break;
                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del pedido: ");
                        int idPedido = Integer.parseInt(sc.nextLine());
                        Pedidos pedido = pedidosController.obtenerPedidoPorId(idPedido);
                        if (pedido != null) {
                            System.out.println("Pedido ID: " + pedido.getPedidoId() + " | Cliente ID: " + pedido.getUsuarioId());
                            List<DetallesPedido> detalles = pedidosController.obtenerDetallesPorPedidoId(idPedido);
                            if (detalles.isEmpty()) {
                                System.out.println("No hay detalles para este pedido.");
                            } else {
                                for (DetallesPedido d : detalles) {
                                    System.out.println("Detalle ID: " + d.getDetalleId() +
                                        " | Producto ID: " + d.getProductoId() +
                                        " | Cantidad: " + d.getCantidad() +
                                        " | Precio: $" + d.getPrecio());
                                }
                            }
                        } else {
                            System.out.println("Pedido no encontrado.");
                        }
                        pausa();
                        break;
                    case 3:
                        limpiarpantalla();
                        Pedidos nuevo = new Pedidos();
                        nuevo.setUsuarioId(usuario.getUsuarioId()); // Asigna ID del usuario actual
                        // podrías pedir más datos aquí si tu modelo lo necesita
                        List<DetallesPedido> listaDetalles = new ArrayList<>();

                        System.out.print("¿Cuántos productos deseas agregar? ");
                        int cantidadDetalles = Integer.parseInt(sc.nextLine());
                        for (int i = 0; i < cantidadDetalles; i++) {
                            DetallesPedido detalle = new DetallesPedido();
                            System.out.print("Producto ID: ");
                            detalle.setProductoId(Integer.parseInt(sc.nextLine()));
                            System.out.print("Cantidad: ");
                            detalle.setCantidad(Integer.parseInt(sc.nextLine()));
                            System.out.print("Precio: ");
                            detalle.setPrecio(Double.parseDouble(sc.nextLine()));
                            listaDetalles.add(detalle);
                        }

                        if (pedidosController.agregarPedido(nuevo, listaDetalles)) {
                            System.out.println("Pedido creado exitosamente.");
                        } else {
                            System.out.println("Error al crear el pedido.");
                        }
                        pausa();
                        break;
                    case 4:
                        limpiarpantalla();
                        System.out.print("ID del pedido a eliminar: ");
                        int eliminarId = Integer.parseInt(sc.nextLine());
                        if (pedidosController.eliminarPedido(eliminarId)) {
                            System.out.println("Pedido eliminado correctamente.");
                        } else {
                            System.out.println("No se pudo eliminar el pedido.");
                        }
                        pausa();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opción no válida, intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida, por favor ingresa un número.");
                pausa();
            }
        }
    }
}
