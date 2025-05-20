package UI.MenusClientes;

import java.util.Scanner;


import Models.Usuarios;
import Models.Wishlist;
import Models.WishlistItems;
import Controller.Cliente.WishlistClienteController;
import Controller.Cliente.WishlistItemsClienteController;

public class MenuWishlistCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final WishlistClienteController wishlistController = new WishlistClienteController();
    private static final WishlistItemsClienteController wishlistItemsController = new WishlistItemsClienteController();

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

    public static Wishlist obtenerOcrearWishlistUsuario(int usuarioId) {
        // Buscar si ya existe una wishlist del usuario
        for (Wishlist w : wishlistController.listarWishlists()) {
            if (w.getUsuarioId() == usuarioId) {
                return w;
            }
        }
        // Si no existe, se crea una nueva
        Wishlist nueva = new Wishlist();
        nueva.setUsuarioId(usuarioId);
        boolean creada = wishlistController.crearWishlist(nueva);
        if (creada) {
            // Volver a obtenerla (ya que no sabemos el ID)
            for (Wishlist w : wishlistController.listarWishlists()) {
                if (w.getUsuarioId() == usuarioId) {
                    return w;
                }
            }
        }
        return null;
    }

    public static void mostrarMenuWishlist(Usuarios usuario) {
        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ WISHLIST ------");
            System.out.println("1. Ver todos los productos en la wishlist");
            System.out.println("2. Ver producto en la wishlist");
            System.out.println("3. Agregar producto a la wishlist");
            System.out.println("4. Eliminar producto de la wishlist");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        Wishlist wishlist = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                        boolean encontrado = false;
                        for (WishlistItems item : wishlistItemsController.listarItems()) {
                            if (item.getWishlistId() == wishlist.getWishlistId()) {
                                System.out.println("ID Item: " + item.getWishitemId());
                                System.out.println("Producto ID: " + item.getProductoId());
                                System.out.println("----------------------------");
                                encontrado = true;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("Tu wishlist está vacía.");
                        }
                        pausa();
                        break;

                    case 2:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del item en la wishlist: ");
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        WishlistItems itemBuscado = wishlistItemsController.obtenerItemPorId(idBuscar);
                        if (itemBuscado != null) {
                            Wishlist userWishlist = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                            if (itemBuscado.getWishlistId() == userWishlist.getWishlistId()) {
                                System.out.println("ID Item: " + itemBuscado.getWishitemId());
                                System.out.println("Producto ID: " + itemBuscado.getProductoId());
                            } else {
                                System.out.println("Este item no pertenece a tu wishlist.");
                            }
                        } else {
                            System.out.println("Item no encontrado.");
                        }
                        pausa();
                        break;

                    case 3:
                        limpiarpantalla();
                        Wishlist wishlistAgregar = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                        WishlistItems nuevoItem = new WishlistItems();
                        nuevoItem.setWishlistId(wishlistAgregar.getWishlistId());
                        System.out.print("Ingresa el ID del producto a agregar: ");
                        nuevoItem.setProductoId(Integer.parseInt(sc.nextLine()));

                        boolean agregado = wishlistItemsController.agregarItemAWishlist(nuevoItem);
                        System.out.println(agregado ? "Producto agregado a la wishlist." : "Error al agregar producto.");
                        pausa();
                        break;

                    case 4:
                        limpiarpantalla();
                        System.out.print("Ingresa el ID del item a eliminar: ");
                        int idEliminar = Integer.parseInt(sc.nextLine());
                        WishlistItems itemEliminar = wishlistItemsController.obtenerItemPorId(idEliminar);
                        if (itemEliminar != null) {
                            Wishlist userWishlist = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                            if (itemEliminar.getWishlistId() == userWishlist.getWishlistId()) {
                                boolean eliminado = wishlistItemsController.eliminarItem(idEliminar);
                                System.out.println(eliminado ? "Item eliminado de la wishlist." : "Error al eliminar item.");
                            } else {
                                System.out.println("Este item no pertenece a tu wishlist.");
                            }
                        } else {
                            System.out.println("Item no encontrado.");
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
