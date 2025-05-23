package UI.MenusClientes;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import Models.Productos;
import Models.Usuarios;
import Controller.Cliente.ProductosClienteController;

public class MenuProductosCliente {
    private static final Scanner sc = new Scanner(System.in);
    private static final ProductosClienteController productosController;

    static {
        ProductosClienteController tempController = null;
        try {
            tempController = new ProductosClienteController();
        } catch (Exception e) {
            System.err.println("Error al inicializar ProductosClienteController: " + e.getMessage());
        }
        productosController = tempController;
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

    private static void mostrarProductosPaginadosInteractivo() {
        if (productosController == null) {
            System.out.println("Error: El controlador de productos no está disponible.");
            pausa();
            return;
        }
        System.out.println("------ CATÁLOGO DE PRODUCTOS ------");
        int productosPorPagina = 10;
        int offsetActual = 0;
        boolean continuarMostrando = true;
        boolean hayProductosMostradosAlgunaVez = false;

        while (continuarMostrando) {
            List<Productos> productosPagina = productosController.listarProductosPaginados(productosPorPagina, offsetActual);

            if (productosPagina == null) {
                System.out.println("Error al cargar productos.");
                productosPagina = new ArrayList<>();
            }

            if (productosPagina.isEmpty()) {
                if (!hayProductosMostradosAlgunaVez && offsetActual == 0) {
                    System.out.println("No hay productos disponibles en este momento.");
                } else {
                    System.out.println("--- No hay más productos para mostrar ---");
                }
                continuarMostrando = false;
            } else {
                for (Productos p : productosPagina) {
                    System.out.printf("ID: %-3d | Nombre: %-25s | Precio: $%-8.2f | Stock: %d%n",
                            p.getProductoId(),
                            p.getNombre().length() > 25 ? p.getNombre().substring(0, 22) + "..." : p.getNombre(),
                            p.getPrecio(),
                            p.getStock());
                }
                hayProductosMostradosAlgunaVez = true;
                offsetActual += productosPagina.size();

                if (productosPagina.size() == productosPorPagina) {
                    System.out.print("¿Mostrar más productos? (S/N): ");
                    String respuesta = sc.nextLine().trim();
                    if (!respuesta.equalsIgnoreCase("S")) {
                        continuarMostrando = false;
                    }
                } else {
                    System.out.println("--- Fin de la lista de productos ---");
                    continuarMostrando = false;
                }
            }
            if (continuarMostrando && !productosPagina.isEmpty()) {
                System.out.println("------------------------------------------");
            }
        }
        if (hayProductosMostradosAlgunaVez) {
            System.out.println("------------------------------------------");
        }
    }

    public static void mostrarMenuProductosCliente(Usuarios usuario, ProductosClienteController extProductosController) {
        if (MenuProductosCliente.productosController == null && extProductosController == null) {
            System.out.println("Error crítico: El controlador de productos no pudo ser inicializado ni proporcionado.");
            pausa();
            return;
        }
        
        final ProductosClienteController currentProductosController = (extProductosController != null) ? extProductosController : MenuProductosCliente.productosController;


        while (true) {
            limpiarpantalla();
            System.out.println("------ MENÚ PRODUCTOS ------");
            System.out.println("1. Ver todos los productos (con detalle completo)");
            System.out.println("2. Ver un producto por ID (con detalle completo)");
            System.out.println("3. Agregar nuevo producto al catálogo");
            System.out.println("4. Editar producto existente del catálogo");
            System.out.println("5. Eliminar producto del catálogo");
            System.out.println("0. Regresar al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                int opc = Integer.parseInt(sc.nextLine());
                switch (opc) {
                    case 1:
                        verTodosLosProductosUI(currentProductosController);
                        break;
                    case 2:
                        verProductoPorIdUI(currentProductosController);
                        break;
                    case 3:
                        agregarProductoUI(currentProductosController);
                        break;
                    case 4:
                        editarProductoUI(currentProductosController);
                        break;
                    case 5:
                        eliminarProductoUI(currentProductosController);
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
            } catch (Exception e) {
                System.err.println("Ocurrió un error inesperado en el Menú Productos: " + e.getMessage());
                e.printStackTrace();
                pausa();
            }
        }
    }

    private static void verTodosLosProductosUI(ProductosClienteController productosCtrl) {
        limpiarpantalla();
        List<Productos> productos = productosCtrl.listarProductos();
        if (productos == null || productos.isEmpty()) {
            System.out.println("No hay productos disponibles.");
        } else {
            System.out.println("------ TODOS LOS PRODUCTOS (DETALLE COMPLETO) ------");
            for (Productos p : productos) {
                System.out.println("ID: " + p.getProductoId());
                System.out.println("Nombre: " + p.getNombre());
                System.out.println("Descripción: " + p.getDescripcion());
                System.out.println("Precio: $" + String.format("%.2f", p.getPrecio()));
                System.out.println("Stock: " + p.getStock());
                System.out.println("---------------------------------------");
            }
        }
        pausa();
    }

    private static void verProductoPorIdUI(ProductosClienteController productosCtrl) {
        limpiarpantalla();
        mostrarProductosPaginadosInteractivo();
        System.out.println("\n------ VER DETALLE DE PRODUCTO POR ID ------");
        System.out.print("Ingresa el ID del producto que deseas ver en detalle (o 0 para cancelar): ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            if (id == 0) {
                System.out.println("Operación cancelada.");
                pausa();
                return;
            }
            Productos producto = productosCtrl.obtenerProductoPorId(id);
            if (producto != null) {
                System.out.println("\n------ DETALLE DEL PRODUCTO ------");
                System.out.println("ID: " + producto.getProductoId());
                System.out.println("Nombre: " + producto.getNombre());
                System.out.println("Descripción: " + producto.getDescripcion());
                System.out.println("Precio: $" + String.format("%.2f", producto.getPrecio()));
                System.out.println("Stock: " + producto.getStock());
                System.out.println("--------------------------------");
            } else {
                System.out.println("Producto con ID '" + id + "' no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número.");
        }
        pausa();
    }

    private static void agregarProductoUI(ProductosClienteController productosCtrl) {
        limpiarpantalla();
        mostrarProductosPaginadosInteractivo();
        System.out.println("\n------ AGREGAR NUEVO PRODUCTO ------");
        try {
            System.out.print("Nombre del producto: ");
            String nombre = sc.nextLine();

            System.out.print("Descripción del producto: ");
            String descripcion = sc.nextLine();

            double precio = -1;
            while (precio < 0) {
                System.out.print("Precio del producto: ");
                try {
                    precio = Double.parseDouble(sc.nextLine());
                    if (precio < 0) System.out.println("El precio no puede ser negativo.");
                } catch (NumberFormatException e) {
                    System.out.println("Precio inválido. Ingresa un número.");
                    precio = -1;
                }
            }

            int stock = -1;
            while (stock < 0) {
                System.out.print("Stock inicial del producto: ");
                try {
                    stock = Integer.parseInt(sc.nextLine());
                    if (stock < 0) System.out.println("El stock no puede ser negativo.");
                } catch (NumberFormatException e) {
                    System.out.println("Stock inválido. Ingresa un número entero.");
                    stock = -1;
                }
            }

            if (nombre.trim().isEmpty() || descripcion.trim().isEmpty()) {
                 System.out.println("El nombre y la descripción no pueden estar vacíos.");
                 pausa();
                 return;
            }

            Productos nuevoProducto = new Productos();
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setDescripcion(descripcion);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setStock(stock);

            if (productosCtrl.agregarProducto(nuevoProducto)) {
                System.out.println("\n¡Producto agregado exitosamente!");
                System.out.println("Nombre: " + nuevoProducto.getNombre() + ", Precio: $" + String.format("%.2f", nuevoProducto.getPrecio()) + ", Stock: " + nuevoProducto.getStock());
                System.out.println("(El ID será asignado automáticamente por el sistema)");
            } else {
                System.out.println("Error al agregar el producto.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
        pausa();
    }

    private static void editarProductoUI(ProductosClienteController productosCtrl) {
        limpiarpantalla();
        mostrarProductosPaginadosInteractivo();
        System.out.println("\n------ EDITAR PRODUCTO EXISTENTE ------");

        System.out.print("Ingresa el ID del producto que deseas editar (o 0 para cancelar): ");
        int idProducto;
        try {
            idProducto = Integer.parseInt(sc.nextLine());
            if (idProducto == 0) {
                System.out.println("Edición cancelada.");
                pausa();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número.");
            pausa();
            return;
        }

        Productos productoAEditar = productosCtrl.obtenerProductoPorId(idProducto);
        if (productoAEditar == null) {
            System.out.println("Producto no encontrado con ID: " + idProducto);
            pausa();
            return;
        }
        
        limpiarpantalla();
        System.out.println("------ EDITANDO PRODUCTO ------");
        System.out.println("ID: " + productoAEditar.getProductoId());
        System.out.println("Nombre actual: " + productoAEditar.getNombre());
        System.out.println("Descripción actual: " + productoAEditar.getDescripcion());
        System.out.println("Precio actual: $" + String.format("%.2f", productoAEditar.getPrecio()));
        System.out.println("Stock actual: " + productoAEditar.getStock());
        System.out.println("-----------------------------------");
        System.out.println("Ingresa los nuevos valores (deja en blanco y presiona ENTER para no cambiar):");

        try {
            System.out.print("Nuevo nombre (" + productoAEditar.getNombre() + "): ");
            String nombre = sc.nextLine();
            if (!nombre.trim().isEmpty()) {
                productoAEditar.setNombre(nombre);
            }

            System.out.print("Nueva descripción (" + productoAEditar.getDescripcion() + "): ");
            String descripcion = sc.nextLine();
            if (!descripcion.trim().isEmpty()) {
                productoAEditar.setDescripcion(descripcion);
            }

            System.out.print("Nuevo precio (" + String.format("%.2f", productoAEditar.getPrecio()) + "): ");
            String precioStr = sc.nextLine();
            if (!precioStr.trim().isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    if (precio < 0) {
                         System.out.println("El precio no puede ser negativo. Se mantiene el anterior.");
                    } else {
                        productoAEditar.setPrecio(precio);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Precio inválido. Se mantiene el anterior.");
                }
            }

            System.out.print("Nuevo stock (" + productoAEditar.getStock() + "): ");
            String stockStr = sc.nextLine();
            if (!stockStr.trim().isEmpty()) {
                try {
                    int stock = Integer.parseInt(stockStr);
                     if (stock < 0) {
                         System.out.println("El stock no puede ser negativo. Se mantiene el anterior.");
                    } else {
                        productoAEditar.setStock(stock);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Stock inválido. Se mantiene el anterior.");
                }
            }

            if (productosCtrl.actualizarProducto(productoAEditar)) {
                System.out.println("\n¡Producto actualizado exitosamente!");
                System.out.println("------ DETALLES ACTUALIZADOS ------");
                System.out.println("ID: " + productoAEditar.getProductoId());
                System.out.println("Nombre: " + productoAEditar.getNombre());
                System.out.println("Precio: $" + String.format("%.2f", productoAEditar.getPrecio()));
                System.out.println("Stock: " + productoAEditar.getStock());
                System.out.println("--------------------------------\n");
            } else {
                System.out.println("Error al actualizar el producto.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
        pausa();
    }

    private static void eliminarProductoUI(ProductosClienteController productosCtrl) {
        limpiarpantalla();
        mostrarProductosPaginadosInteractivo();
        System.out.println("\n------ ELIMINAR PRODUCTO ------");

        System.out.print("Ingresa el ID del producto que deseas eliminar (o 0 para cancelar): ");
        int idProducto;
        try {
            idProducto = Integer.parseInt(sc.nextLine());
            if (idProducto == 0) {
                System.out.println("Eliminación cancelada.");
                pausa();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número.");
            pausa();
            return;
        }

        Productos productoAEliminar = productosCtrl.obtenerProductoPorId(idProducto);
        if (productoAEliminar == null) {
            System.out.println("Producto no encontrado con ID: " + idProducto);
            pausa();
            return;
        }
        
        System.out.println("\n--- Detalles del producto a eliminar ---");
        System.out.println("ID: " + productoAEliminar.getProductoId());
        System.out.println("Nombre: " + productoAEliminar.getNombre());
        System.out.print("¿Estás seguro que deseas eliminar este producto? (S/N): ");
        String confirmacion = sc.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            if (productosCtrl.eliminarProducto(idProducto)) {
                System.out.println("\n¡Producto eliminado exitosamente!");
            } else {
                System.out.println("Error al eliminar el producto.");
                System.out.println("NOTA: Puede estar referenciado en carritos, pedidos, etc.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
        pausa();
    }
}