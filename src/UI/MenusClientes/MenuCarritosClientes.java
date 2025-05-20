package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;

import Models.Carrito;
import Models.ItemsCarrito;
import Models.Usuarios;
import Controller.Cliente.CarritoClienteController;
import Controller.Cliente.ItemsCarritoClienteController;

public class MenuCarritosClientes {
    private static final Scanner sc = new Scanner(System.in);
    private static final CarritoClienteController carritoController = new CarritoClienteController();
    private static final ItemsCarritoClienteController itemsCarritoController = new ItemsCarritoClienteController();

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

    public static void mostrarMenuCarritosClientes(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ CARRITO ------");
            System.out.println("1. Ver todos los carritos");
            System.out.println("2. Ver mi carrito y sus productos");
            System.out.println("3. Agregar producto al carrito");
            System.out.println("4. Eliminar carrito");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");
            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<Carrito> carritos = carritoController.obtenerTodosLosCarritos();
                        if (carritos.isEmpty()) {
                            System.out.println("No hay carritos registrados.");
                        } else {
                            carritos.forEach(System.out::println);
                        }
                        pausa();
                        break;

                    case 2:
                        limpiarpantalla();
                        Carrito carrito = carritoController.obtenerCarritoPorUsuarioId(usuario.getUsuarioId());
                        if (carrito == null) {
                            System.out.println("No tienes un carrito asignado.");
                        } else {
                            System.out.println("Carrito ID: " + carrito.getCarritoId());
                            List<ItemsCarrito> items = itemsCarritoController.listarItemsCarrito();
                            items.stream()
                                 .filter(item -> item.getCarritoId() == carrito.getCarritoId())
                                 .forEach(System.out::println);
                        }
                        pausa();
                        break;

                    case 3:
                        limpiarpantalla();
                        Carrito carritoActual = carritoController.obtenerCarritoPorUsuarioId(usuario.getUsuarioId());
                        if (carritoActual == null) {
                            carritoActual = new Carrito();
                            carritoActual.setUsuarioId(usuario.getUsuarioId());
                            carritoController.crearCarrito(carritoActual);
                            carritoActual = carritoController.obtenerCarritoPorUsuarioId(usuario.getUsuarioId());
                        }

                        System.out.print("ID del producto: ");
                        int productoId = Integer.parseInt(sc.nextLine());
                        System.out.print("Cantidad: ");
                        int cantidad = Integer.parseInt(sc.nextLine());

                        ItemsCarrito nuevoItem = new ItemsCarrito();
                        nuevoItem.setCarritoId(carritoActual.getCarritoId());
                        nuevoItem.setProductoId(productoId);
                        nuevoItem.setCantidad(cantidad);

                        boolean agregado = itemsCarritoController.agregarItem(nuevoItem);
                        if (agregado) {
                            System.out.println("Producto agregado al carrito.");
                        } else {
                            System.out.println("Error al agregar producto al carrito.");
                        }
                        pausa();
                        break;

                    case 4:
                        limpiarpantalla();
                        Carrito carritoAEliminar = carritoController.obtenerCarritoPorUsuarioId(usuario.getUsuarioId());
                        if (carritoAEliminar != null) {
                            boolean eliminado = carritoController.eliminarCarrito(carritoAEliminar.getCarritoId());
                            if (eliminado) {
                                System.out.println("Carrito eliminado correctamente.");
                            } else {
                                System.out.println("No se pudo eliminar el carrito.");
                            }
                        } else {
                            System.out.println("No tienes un carrito para eliminar.");
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
