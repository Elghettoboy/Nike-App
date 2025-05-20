package UI.MenusClientes;

import java.util.Scanner;
import java.util.List;

import Models.Productos;
import Models.Usuarios;
import Controller.Cliente.ProductosClienteController;

public class MenuProductosCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final ProductosClienteController productosController = new ProductosClienteController();

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

    public static void mostrarMenuProductosCliente(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PRODUCTOS ------");
            System.out.println("1. Ver todos los productos");
            System.out.println("2. Ver producto por ID");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        List<Productos> productos = productosController.listarProductos();
                        if (productos.isEmpty()) {
                            System.out.println("No hay productos disponibles.");
                        } else {
                            for (Productos p : productos) {
                                System.out.println("ID: " + p.getProductoId());
                                System.out.println("Nombre: " + p.getNombre());
                                System.out.println("Descripción: " + p.getDescripcion());
                                System.out.println("Precio: $" + p.getPrecio());
                                System.out.println("Stock: " + p.getStock());
                                System.out.println("-----------------------------");
                            }
                        }
                        pausa();
                        break;
                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del producto: ");
                        int id = Integer.parseInt(sc.nextLine());
                        Productos producto = productosController.obtenerProductoPorId(id);
                        if (producto != null) {
                            System.out.println("ID: " + producto.getProductoId());
                            System.out.println("Nombre: " + producto.getNombre());
                            System.out.println("Descripción: " + producto.getDescripcion());
                            System.out.println("Precio: $" + producto.getPrecio());
                            System.out.println("Stock: " + producto.getStock());
                        } else {
                            System.out.println("Producto no encontrado.");
                        }
                        pausa();
                        break;
                    case 0:
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
