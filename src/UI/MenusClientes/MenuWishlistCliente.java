package UI.MenusClientes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Models.Usuarios;
import Models.Wishlist;
import Models.WishlistItems;
import Models.Productos;
import Controller.Cliente.WishlistClienteController;
import Controller.Cliente.WishlistItemsClienteController;
import Controller.Cliente.ProductosClienteController;

public class MenuWishlistCliente {
    private static final Scanner sc = new Scanner(System.in);

    private static WishlistClienteController wishlistController;
    private static WishlistItemsClienteController wishlistItemsController;
    private static ProductosClienteController productosClienteController;

    static {
        try {
            wishlistController = new WishlistClienteController();
            wishlistItemsController = new WishlistItemsClienteController();
            productosClienteController = new ProductosClienteController();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar controladores para Wishlist: " + e.getMessage());
            wishlistController = null;
            wishlistItemsController = null;
            productosClienteController = null;
        }
    }

    public static void limpiarpantalla() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }

    public static void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }

    public static Wishlist obtenerOcrearWishlistUsuario(int usuarioId) {
        if (wishlistController == null) {
            System.err.println("WishlistController no inicializado.");
            return null;
        }
        Wishlist wishlist = wishlistController.obtenerWishlistPorUsuarioId(usuarioId);
        if (wishlist == null) {
            System.out.println("No tienes una wishlist activa, creando una nueva...");
            Wishlist nueva = new Wishlist();
            nueva.setUsuarioId(usuarioId);
            // Assuming crearWishlist sets the ID on the 'nueva' object upon success
            if (wishlistController.crearWishlist(nueva) && nueva.getWishlistId() > 0) {
                System.out.println("Nueva wishlist creada con ID: " + nueva.getWishlistId());
                return nueva;
            } else {
                // If creation failed or ID wasn't set, try fetching again in case of race condition or delayed propagation
                wishlist = wishlistController.obtenerWishlistPorUsuarioId(usuarioId);
                if (wishlist == null) {
                     System.err.println("Error: No se pudo crear una nueva wishlist o no se obtuvo su ID.");
                }
            }
        }
        if (wishlist == null) { // Double check after potential recreation attempt
            System.err.println("No se pudo obtener o crear la wishlist para el usuario " + usuarioId);
        }
        return wishlist;
    }

    private static void mostrarProductosParaSeleccionWishlist(List<Productos> productos) {
        System.out.println("--- Productos Disponibles (Página Actual) ---");
        if (productos.isEmpty()) {
            return;
        }
        for (Productos p : productos) {
            System.out.println(String.format("ID: %-5d | Nombre: %-30s | Precio: $%-8.2f",
                    p.getProductoId(),
                    p.getNombre(),
                    p.getPrecio()));
        }
        System.out.println("------------------------------------------------------");
    }

    private static void agregarProductoAWishlistUI(Usuarios usuario) {
        limpiarpantalla();
        System.out.println("------ AGREGAR PRODUCTO A LA WISHLIST ------");

        if (productosClienteController == null || wishlistController == null || wishlistItemsController == null) {
            System.out.println("Error: Uno o más controladores no están inicializados.");
            pausa();
            return;
        }

        Wishlist wishlistUsuario = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
        if (wishlistUsuario == null || wishlistUsuario.getWishlistId() <= 0) {
            System.out.println("No se pudo acceder o crear tu wishlist. Inténtalo de nuevo.");
            pausa();
            return;
        }

        int idProductoSeleccionado = -1;
        Productos productoSeleccionado = null;
        boolean seleccionValida = false;

        while(!seleccionValida) {
            System.out.println("\nSelecciona un producto de la lista para agregar:");
            int productosPorPagina = 10;
            int offsetActual = 0;
            boolean quiereVerMasPaginas = true;
            boolean algunProductoMostrado = false;

            while (quiereVerMasPaginas) {
                List<Productos> paginaDeProductos = productosClienteController.listarProductosPaginados(productosPorPagina, offsetActual);

                if (paginaDeProductos.isEmpty()) {
                    if (offsetActual == 0) {
                        System.out.println("No hay productos disponibles en el catálogo.");
                        pausa();
                        return;
                    } else {
                        System.out.println("--- No hay más productos para mostrar ---");
                    }
                    quiereVerMasPaginas = false;
                } else {
                    mostrarProductosParaSeleccionWishlist(paginaDeProductos);
                    algunProductoMostrado = true;
                    offsetActual += paginaDeProductos.size();

                    if (paginaDeProductos.size() == productosPorPagina) {
                        System.out.print("Ver más productos (S) / Ingresar ID del producto a agregar (ID): ");
                        String respuesta = sc.nextLine().trim();
                        if (!respuesta.equalsIgnoreCase("S")) {
                            quiereVerMasPaginas = false;
                        }
                    } else {
                        System.out.println("--- Fin de la lista de productos ---");
                        quiereVerMasPaginas = false;
                    }
                }
            }

            if (!algunProductoMostrado) {
                System.out.println("No se mostraron productos.");
                pausa();
                return;
            }

            System.out.print("\nIngrese el ID del producto que desea agregar a la wishlist (o 0 para cancelar): ");
            try {
                idProductoSeleccionado = Integer.parseInt(sc.nextLine());
                if (idProductoSeleccionado == 0) {
                    System.out.println("Agregar a wishlist cancelado.");
                    pausa();
                    return;
                }
                productoSeleccionado = productosClienteController.obtenerProductoPorId(idProductoSeleccionado);
                if (productoSeleccionado != null) {
                    seleccionValida = true;
                } else {
                    System.out.println("Producto con ID " + idProductoSeleccionado + " no encontrado. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Debe ser un número.");
            }
        }

        WishlistItems nuevoItem = new WishlistItems();
        nuevoItem.setWishlistId(wishlistUsuario.getWishlistId());
        nuevoItem.setProductoId(productoSeleccionado.getProductoId());

        if (wishlistItemsController.agregarItemAWishlist(nuevoItem)) {
            System.out.println("Producto '" + productoSeleccionado.getNombre() + "' agregado a tu wishlist exitosamente.");
        } else {
            System.out.println("Error al agregar el producto a la wishlist. Es posible que ya exista.");
        }
        pausa();
    }

    /**
     * Muestra los items de la wishlist del usuario de forma detallada.
     * @param usuario El usuario actual.
     * @return true si se mostraron items, false si la wishlist está vacía o no se pudo acceder.
     */
    private static boolean mostrarWishlistDetallada(Usuarios usuario) {
        Wishlist wishlistUsuario = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
        if (wishlistUsuario == null || wishlistUsuario.getWishlistId() <= 0) {
            // obtenerOcrearWishlistUsuario ya imprime mensajes de error si falla gravemente.
            // Si solo no existe y no se pudo crear, o si no tiene ID:
            System.out.println("No se pudo acceder o inicializar tu wishlist en este momento.");
            return false;
        }

        List<WishlistItems> itemsEnWishlist = wishlistItemsController.listarItemsPorWishlistId(wishlistUsuario.getWishlistId());

        if (itemsEnWishlist == null || itemsEnWishlist.isEmpty()) {
            System.out.println("Tu wishlist (ID: " + wishlistUsuario.getWishlistId() + ") está vacía.");
            return false;
        }

        System.out.println("------ MI WISHLIST (ID: " + wishlistUsuario.getWishlistId() + ") ------");
        for (WishlistItems item : itemsEnWishlist) {
            Productos producto = productosClienteController.obtenerProductoPorId(item.getProductoId());
            System.out.println("ID Item Wishlist: " + item.getWishitemId()); // Este es el ID que el usuario ingresará
            if (producto != null) {
                System.out.println("  Producto: " + producto.getNombre() + " (ID Prod: " + producto.getProductoId() + ")");
                System.out.println("  Precio: $" + String.format("%.2f", producto.getPrecio()));
            } else {
                System.out.println("  Producto (ID: " + item.getProductoId() + ") ya no está disponible o no fue encontrado.");
            }
            System.out.println("----------------------------");
        }
        return true;
    }

    public static void mostrarMenuWishlist(Usuarios usuario) {
         if (wishlistController == null || wishlistItemsController == null || productosClienteController == null) {
            System.out.println("Error: Los controladores necesarios para el menú Wishlist no están inicializados.");
            System.out.println("Por favor, revisa la consola de error al inicio de la aplicación.");
            pausa();
            return;
        }

        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ WISHLIST ------");
            System.out.println("1. Ver mi wishlist");
            System.out.println("2. Ver item específico en mi wishlist por ID de item");
            System.out.println("3. Agregar producto a la wishlist");
            System.out.println("4. Eliminar producto de la wishlist (por ID de item)");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        mostrarWishlistDetallada(usuario); // Llama al helper
                        pausa();
                        break;

                    case 2: // Ver item específico
                        limpiarpantalla();
                        System.out.println("------ VER ITEM ESPECÍFICO EN WISHLIST ------");
                        boolean tieneItemsParaVer = mostrarWishlistDetallada(usuario);

                        if (!tieneItemsParaVer) {
                            // Mensaje ya fue mostrado por mostrarWishlistDetallada
                            pausa();
                            break;
                        }

                        System.out.print("\nDe la lista anterior, ingresa el ID del item en la wishlist que deseas ver: ");
                        int idBuscar;
                        try {
                            idBuscar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID de item inválido. Debe ser un número.");
                            pausa();
                            break;
                        }

                        WishlistItems itemBuscado = wishlistItemsController.obtenerItemPorId(idBuscar);
                        if (itemBuscado != null) {
                            // Validar que el item pertenece a la wishlist del usuario actual
                            Wishlist userWishlistValidacion = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                            if (userWishlistValidacion != null && itemBuscado.getWishlistId() == userWishlistValidacion.getWishlistId()) {
                                System.out.println("\n------ DETALLE DEL ITEM (ID Item: " + itemBuscado.getWishitemId() + ") ------");
                                Productos producto = productosClienteController.obtenerProductoPorId(itemBuscado.getProductoId());
                                if (producto != null) {
                                    System.out.println("Producto: " + producto.getNombre() + " (ID Prod: " + producto.getProductoId() + ")");
                                    System.out.println("Precio: $" + String.format("%.2f", producto.getPrecio()));
                                    System.out.println("Descripción: " + (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty() ? "(Sin descripción)" : producto.getDescripcion()));
                                } else {
                                    System.out.println("Producto asociado (ID Prod: " + itemBuscado.getProductoId() + ") no encontrado.");
                                }
                                System.out.println("------------------------------------------");
                            } else {
                                System.out.println("Este item no pertenece a tu wishlist o tu wishlist no pudo ser accedida.");
                            }
                        } else {
                            System.out.println("Item no encontrado en la wishlist con ID: " + idBuscar);
                        }
                        pausa();
                        break;

                    case 3:
                        agregarProductoAWishlistUI(usuario);
                        break;

                    case 4: // Eliminar producto de la wishlist
                        limpiarpantalla();
                        System.out.println("------ ELIMINAR PRODUCTO DE LA WISHLIST ------");
                        boolean tieneItemsParaEliminar = mostrarWishlistDetallada(usuario);

                        if (!tieneItemsParaEliminar) {
                            // Mensaje ya fue mostrado por mostrarWishlistDetallada
                            pausa();
                            break;
                        }
                        System.out.print("\nDe la lista anterior, ingresa el ID del item de la wishlist que deseas eliminar: ");
                        int idEliminar;
                        try {
                            idEliminar = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID de item inválido. Debe ser un número.");
                            pausa();
                            break;
                        }

                        WishlistItems itemEliminar = wishlistItemsController.obtenerItemPorId(idEliminar);
                        if (itemEliminar != null) {
                            // Validar que el item pertenece a la wishlist del usuario actual
                            Wishlist userWishlistVal = obtenerOcrearWishlistUsuario(usuario.getUsuarioId());
                            if (userWishlistVal != null && itemEliminar.getWishlistId() == userWishlistVal.getWishlistId()) {
                                System.out.println("\n--- Confirmar eliminación del item ID: " + itemEliminar.getWishitemId() + " ---");
                                Productos p = productosClienteController.obtenerProductoPorId(itemEliminar.getProductoId());
                                if (p != null) System.out.println("Producto: " + p.getNombre());
                                else System.out.println("Producto ID: " + itemEliminar.getProductoId() + " (detalle no disponible)");
                                System.out.println("------------------------------------");

                                System.out.print("¿Estás seguro que deseas eliminar este item de tu wishlist? (S/N): ");
                                if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                                    if (wishlistItemsController.eliminarItem(idEliminar)) { // eliminarItem espera el ID del WishlistItem
                                        System.out.println("Item eliminado de la wishlist exitosamente.");
                                    } else {
                                        System.out.println("Error al eliminar el item de la wishlist.");
                                    }
                                } else {
                                     System.out.println("Eliminación cancelada.");
                                }
                            } else {
                                System.out.println("Este item no pertenece a tu wishlist o tu wishlist no pudo ser accedida.");
                            }
                        } else {
                            System.out.println("Item no encontrado con ID: " + idEliminar + " en la wishlist.");
                        }
                        pausa();
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado en el Menú Wishlist: " + e.getMessage());
                e.printStackTrace();
                pausa();
            }
        }
    }
}