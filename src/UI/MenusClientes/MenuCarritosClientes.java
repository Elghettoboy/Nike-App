package UI.MenusClientes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Timestamp; // Para fecha creación carrito si es necesario

import Models.Carrito;
import Models.ItemsCarrito;
import Models.Productos;
import Models.Usuarios;
import Controller.Cliente.CarritoClienteController;
import Controller.Cliente.ItemsCarritoClienteController;
import Controller.Cliente.ProductosClienteController;

public class MenuCarritosClientes {
    private static final Scanner sc = new Scanner(System.in);

    private static CarritoClienteController carritoController;
    private static ItemsCarritoClienteController itemsCarritoController;
    private static ProductosClienteController productosController;

    static {
        try {
            carritoController = new CarritoClienteController();
            itemsCarritoController = new ItemsCarritoClienteController();
            productosController = new ProductosClienteController();
        } catch (Exception e) {
            System.err.println("Error crítico al inicializar controladores en MenuCarritosClientes: " + e.getMessage());
            carritoController = null;
            itemsCarritoController = null;
            productosController = null;
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

    private static Carrito obtenerOcrearCarritoActivoUsuario(Usuarios usuario, CarritoClienteController carritoController2) {
        if (carritoController == null) {
            System.err.println("CarritoController no está inicializado y es necesario.");
            return null;
        }
        Carrito carrito = carritoController.obtenerCarritoActivoPorUsuarioId(usuario.getUsuarioId());
        if (carrito == null) {
            System.out.println("No tienes un carrito activo, creando uno nuevo...");
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuarioId(usuario.getUsuarioId());
            nuevoCarrito.setFechaCreacion(new Timestamp(System.currentTimeMillis()));

            boolean creacionExitosa = carritoController.crearCarrito(nuevoCarrito);
            if (creacionExitosa && nuevoCarrito.getCarritoId() > 0) {
                System.out.println("Nuevo carrito creado con ID: " + nuevoCarrito.getCarritoId());
                return nuevoCarrito;
            } else {
                System.err.println("Error: No se pudo crear un nuevo carrito o no se obtuvo su ID.");
                return null;
            }
        }
        return carrito;
    }

    private static void mostrarProductosParaSeleccion(List<Productos> productos) {
        System.out.println("--- Productos Disponibles (Página Actual) ---");
        if (productos == null || productos.isEmpty()) {
            return;
        }
        for (Productos p : productos) {
            System.out.println(String.format("ID: %-5d | Nombre: %-30s | Precio: $%-8.2f | Stock: %d",
                    p.getProductoId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getStock()));
        }
        System.out.println("------------------------------------------------------");
    }

    private static void agregarProductoAlCarritoUI(Usuarios usuario, Carrito carritoActivo) {
        limpiarpantalla();
        System.out.println("------ AGREGAR PRODUCTO AL CARRITO (ID: "+ carritoActivo.getCarritoId() +") ------");

        if (productosController == null || itemsCarritoController == null) {
            System.out.println("Error: Controladores de productos o items de carrito no están inicializados.");
            pausa();
            return;
        }

        Productos productoSeleccionado = null;
        boolean seleccionValida = false;

        while(!seleccionValida){
            System.out.println("\nSelecciona un producto de la lista para agregar:");
            int productosPorPagina = 10;
            int offsetActual = 0;
            boolean quiereVerMasPaginas = true;
            boolean algunProductoMostrado = false;

            while (quiereVerMasPaginas) {
                List<Productos> paginaDeProductos = productosController.listarProductosPaginados(productosPorPagina, offsetActual);
                if (paginaDeProductos == null || paginaDeProductos.isEmpty()) {
                    if (offsetActual == 0) {
                        System.out.println("No hay productos disponibles en el catálogo.");
                        pausa();
                        return;
                    } else {
                        System.out.println("--- No hay más productos para mostrar ---");
                    }
                    quiereVerMasPaginas = false;
                } else {
                    mostrarProductosParaSeleccion(paginaDeProductos);
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

            System.out.print("\nIngrese el ID del producto que desea agregar al carrito (o 0 para cancelar): ");
            int idProductoSeleccionado;
            try {
                idProductoSeleccionado = Integer.parseInt(sc.nextLine());
                if (idProductoSeleccionado == 0) {
                    System.out.println("Operación cancelada.");
                    pausa();
                    return;
                }
                productoSeleccionado = productosController.obtenerProductoPorId(idProductoSeleccionado);
                if (productoSeleccionado != null) {
                    seleccionValida = true;
                } else {
                    System.out.println("Producto con ID " + idProductoSeleccionado + " no encontrado. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Debe ser un número.");
            }
        }

        System.out.print("Cantidad para '" + productoSeleccionado.getNombre() + "' (Stock: " + productoSeleccionado.getStock() + "): ");
        int cantidad;
        try {
            cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser un número positivo.");
                pausa();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida.");
            pausa();
            return;
        }

        ItemsCarrito itemParaProcesar = new ItemsCarrito();
        itemParaProcesar.setCarritoId(carritoActivo.getCarritoId());
        itemParaProcesar.setProductoId(productoSeleccionado.getProductoId());
        itemParaProcesar.setCantidad(cantidad);

        if (itemsCarritoController.agregarOActualizarItemAlCarrito(itemParaProcesar)) {
            System.out.println("¡Producto '" + productoSeleccionado.getNombre() + "' procesado en el carrito exitosamente!");
        } else {
            System.out.println("Error al procesar el producto en el carrito.");
        }
        pausa();
    }

    private static void modificarCantidadProductoEnCarritoUI(Usuarios usuario, Carrito carritoActivo) {
        limpiarpantalla();
        System.out.println("------ MODIFICAR CANTIDAD DE PRODUCTO (ID Carrito: " + carritoActivo.getCarritoId() + ") ------");

        if (itemsCarritoController == null || productosController == null) {
            System.out.println("Error: Controladores no inicializados.");
            pausa();
            return;
        }

        List<ItemsCarrito> items = itemsCarritoController.obtenerItemsPorCarritoId(carritoActivo.getCarritoId());
        if (items == null || items.isEmpty()) {
            System.out.println("Tu carrito está vacío. Nada que modificar.");
            pausa();
            return;
        }

        System.out.println("Productos en tu carrito:");
        for (ItemsCarrito item : items) {
            Productos p = productosController.obtenerProductoPorId(item.getProductoId());
            if (p != null) {
                System.out.println(String.format("ID Prod: %-5d | Nombre: %-30s | Cantidad Actual: %d",
                        item.getProductoId(), p.getNombre(), item.getCantidad()));
            } else {
                System.out.println(String.format("ID Prod: %-5d | Nombre: (No encontrado) | Cantidad Actual: %d",
                        item.getProductoId(), item.getCantidad()));
            }
        }
        System.out.println("--------------------------------------------------------------------");

        int idProductoModificar;
        Productos productoAModificar = null;
        boolean productoEnCarrito = false;

        while (true) {
            System.out.print("Ingresa el ID del producto del carrito que deseas modificar (o 0 para cancelar): ");
            try {
                idProductoModificar = Integer.parseInt(sc.nextLine());
                if (idProductoModificar == 0) {
                    System.out.println("Modificación cancelada.");
                    pausa();
                    return;
                }

                for (ItemsCarrito item : items) {
                    if (item.getProductoId() == idProductoModificar) {
                        productoEnCarrito = true;
                        productoAModificar = productosController.obtenerProductoPorId(idProductoModificar);
                        break;
                    }
                }

                if (productoEnCarrito && productoAModificar != null) {
                    break;
                } else if (!productoEnCarrito) {
                    System.out.println("El producto con ID " + idProductoModificar + " no se encuentra en tu carrito. Intenta de nuevo.");
                } else {
                    System.out.println("Error al obtener detalles del producto con ID " + idProductoModificar + ". Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de producto inválido. Debe ser un número.");
            }
        }

        int nuevaCantidad;
        while (true) {
            System.out.print("Ingresa la nueva cantidad para '" + productoAModificar.getNombre() + "' (Stock: " + productoAModificar.getStock() + "): ");
            try {
                nuevaCantidad = Integer.parseInt(sc.nextLine());
                if (nuevaCantidad <= 0) {
                    System.out.println("La cantidad debe ser un número positivo.");
                } else {
                    // Optional: Add a preliminary stock check here if desired,
                    // though the controller should be the ultimate authority.
                    // if (nuevaCantidad > productoAModificar.getStock()) {
                    // System.out.println("La cantidad solicitada (" + nuevaCantidad + ") excede el stock disponible (" + productoAModificar.getStock() + ").");
                    // } else {
                    break; // Valid quantity (positive)
                    // }
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida. Debe ser un número.");
            }
        }

        ItemsCarrito itemModificado = new ItemsCarrito();
        itemModificado.setCarritoId(carritoActivo.getCarritoId());
        itemModificado.setProductoId(productoAModificar.getProductoId());
        itemModificado.setCantidad(nuevaCantidad);

        if (itemsCarritoController.agregarOActualizarItemAlCarrito(itemModificado)) {
            System.out.println("Cantidad del producto '" + productoAModificar.getNombre() + "' actualizada a " + nuevaCantidad + " exitosamente.");
        } else {
            System.out.println("Error al actualizar la cantidad del producto en el carrito.");
            System.out.println("Es posible que la cantidad solicitada exceda el stock disponible o haya ocurrido un error interno.");
        }
        pausa();
    }

    private static void eliminarProductoDelCarritoUI(Usuarios usuario, Carrito carritoActivo) {
        limpiarpantalla();
        System.out.println("------ ELIMINAR PRODUCTO DEL CARRITO (ID: " + carritoActivo.getCarritoId() + ") ------");
        if (itemsCarritoController == null || productosController == null) {
             System.out.println("Error: Controladores no inicializados.");
             pausa();
             return;
        }

        List<ItemsCarrito> items = itemsCarritoController.obtenerItemsPorCarritoId(carritoActivo.getCarritoId());
        if (items == null || items.isEmpty()) {
            System.out.println("Tu carrito está vacío. Nada que eliminar.");
            pausa();
            return;
        }

        System.out.println("Productos en tu carrito:");
        for (ItemsCarrito item : items) {
            Productos p = productosController.obtenerProductoPorId(item.getProductoId());
            if (p != null) {
                System.out.println(String.format("Producto ID: %-5d | Nombre: %-30s | Cantidad: %d",
                    item.getProductoId(), p.getNombre(), item.getCantidad()));
            } else {
                System.out.println(String.format("Producto ID: %-5d | Nombre: (No encontrado) | Cantidad: %d",
                    item.getProductoId(), item.getCantidad()));
            }
        }
        System.out.println("-------------------------------------------");
        System.out.print("Ingresa el ID del producto a eliminar del carrito (o 0 para cancelar): ");
        int productoIdAEliminar;
        try {
            productoIdAEliminar = Integer.parseInt(sc.nextLine());
            if (productoIdAEliminar == 0) {
                System.out.println("Eliminación cancelada.");
                pausa();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID de producto inválido.");
            pausa();
            return;
        }

        if (itemsCarritoController.eliminarItem(carritoActivo.getCarritoId(), productoIdAEliminar)) {
            System.out.println("Producto eliminado del carrito exitosamente.");
        } else {
            System.out.println("Error al eliminar el producto del carrito o el producto no estaba en el carrito.");
        }
        pausa();
    }

    public static void mostrarMenuCarritosClientes(Usuarios usuario) {
        if (carritoController == null || itemsCarritoController == null || productosController == null) {
            System.out.println("Error crítico: Uno o más controladores no están inicializados.");
            System.out.println("Por favor, revisa la consola de error al inicio de la aplicación.");
            pausa();
            return;
        }

        Carrito carritoActivo = obtenerOcrearCarritoActivoUsuario(usuario, carritoController);
        if (carritoActivo == null) {
            System.out.println("No se pudo acceder o crear un carrito. El menú de carritos no puede continuar.");
            pausa();
            return;
        }

        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ CARRITO (ID: " + carritoActivo.getCarritoId()+ ", Usuario: " + usuario.getNombre() + ") ------");
            System.out.println("1. Ver contenido del carrito");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Modificar cantidad de producto en carrito");
            System.out.println("4. Eliminar producto del carrito");
            System.out.println("5. Vaciar carrito completo");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        limpiarpantalla();
                        System.out.println("------ CONTENIDO DEL CARRITO (ID: " + carritoActivo.getCarritoId() + ") ------");
                        List<ItemsCarrito> items = itemsCarritoController.obtenerItemsPorCarritoId(carritoActivo.getCarritoId());
                        if (items == null || items.isEmpty()) {
                            System.out.println("Tu carrito está vacío.");
                        } else {
                            double totalCarrito = 0;
                            for (ItemsCarrito item : items) {
                                Productos p = productosController.obtenerProductoPorId(item.getProductoId());
                                if (p != null) {
                                    System.out.println(String.format("Producto: %s (ID: %d) | Cantidad: %d | P.Unit: $%.2f | Subtotal: $%.2f",
                                        p.getNombre(), item.getProductoId(), item.getCantidad(), p.getPrecio(), (item.getCantidad() * p.getPrecio()) ));
                                    totalCarrito += item.getCantidad() * p.getPrecio();
                                } else {
                                     System.out.println(String.format("Producto ID: %d (Nombre no encontrado) | Cantidad: %d", item.getProductoId(), item.getCantidad()));
                                }
                            }
                            System.out.println("-----------------------------------");
                            System.out.println(String.format("TOTAL DEL CARRITO: $%.2f", totalCarrito));
                        }
                        pausa();
                        break;
                    case 2:
                        agregarProductoAlCarritoUI(usuario, carritoActivo);
                        break;
                    case 3:
                        modificarCantidadProductoEnCarritoUI(usuario, carritoActivo); // MODIFIED LINE
                        break;
                    case 4:
                        eliminarProductoDelCarritoUI(usuario, carritoActivo);
                        break;
                    case 5:
                         limpiarpantalla();
                         System.out.println("------ VACIAR CARRITO ------");
                         System.out.print("¿Estás seguro que deseas eliminar TODOS los items del carrito ID " + carritoActivo.getCarritoId() + "? (S/N): ");
                         if (sc.nextLine().trim().equalsIgnoreCase("S")) {
                            if(itemsCarritoController.eliminarTodosLosItemsDeUnCarrito(carritoActivo.getCarritoId())){
                                System.out.println("Todos los items han sido eliminados del carrito.");
                            } else {
                                System.out.println("Error al intentar vaciar el carrito.");
                            }
                         } else {
                            System.out.println("Operación cancelada.");
                         }
                        pausa();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opción no válida.");
                        pausa();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                pausa();
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado en Menú Carrito: " + e.getMessage());
                e.printStackTrace();
                pausa();
            }
        }
    }
}