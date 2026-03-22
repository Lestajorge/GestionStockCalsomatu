package com.inventario.app.controller;


import com.inventario.app.model.Articulo;
import com.inventario.app.model.Empleado;
import com.inventario.app.model.Movimientos;
import com.inventario.app.repository.ArticuloRepository;
import com.inventario.app.repository.EmpleadoRepository;
import com.inventario.app.repository.MovimientosRepository;
import com.inventario.app.service.ArticuloService;
import com.inventario.app.service.EmpleadoService;
import com.inventario.app.service.MovimientosService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class MovimientoWebController {

    @Autowired
    private ArticuloRepository artRepo;

    @Autowired
    private EmpleadoRepository empRepo;

    @Autowired
    private MovimientosRepository movRepo;
    @Autowired
    private com.inventario.app.service.ExportadorPDF exportadorService;

    private final ArticuloService articuloService;
    private final EmpleadoService empleadoService;
    private final MovimientosService movimientoService; 


 public MovimientoWebController(ArticuloService articuloService, EmpleadoService empleadoService,
            MovimientosService movimientosService) {
        this.articuloService = articuloService;
        this.empleadoService = empleadoService;
        this.movimientoService = movimientosService;
    }

@GetMapping("/menu")
public String mostrarMenu() {
    return "menu"; // Esto abre los 4 botones
}
@GetMapping("/inventario/{seccion}")
public String mostrarSeccion(@PathVariable("seccion") String seccion, 
                             @RequestParam("idEmpleado") Integer idEmpleado, 
                             @SessionAttribute(name = "carrito", required = false) Map<Integer, Integer> carrito,
                             Model model) {
    
    // Si el carrito no existe en la sesión, lo creamos vacío
    if (carrito == null) {
        carrito = new HashMap<>();
    }

    List<Articulo> filtrados = articuloService.listarTodos().stream()
            .filter(a -> seccion.equalsIgnoreCase(a.getSeccion()))
            .collect(Collectors.toList());

    model.addAttribute("articulos", filtrados);
    model.addAttribute("nombreSeccion", seccion.toUpperCase());
    model.addAttribute("empleadoSeleccionado", empRepo.findById(idEmpleado).orElse(null));
    model.addAttribute("carrito", carrito); // Pasamos el carrito actual a la vista
    
    return "categoria";
}

@GetMapping("/inventario")
public String mostrarInventario(
        @RequestParam(value = "idEmpleado", required = false) Integer idEmpleado, 
        Model model) {
    
    // 1. Cargamos empleados para el selector
    model.addAttribute("empleados", empleadoService.listarEmpleados());
    
    // 2. ID seleccionado por si venimos de una categoría
    model.addAttribute("idSeleccionado", idEmpleado);
    
    // 3. AÑADIMOS ESTO: Obtener los últimos 15 movimientos
    // Usamos el repositorio para traer todo y limitamos a 15 (o crea un método en el repo)
    List<Movimientos> ultimosMovimientos = movRepo.findAll();
    // Invertimos la lista para que el más reciente salga primero y cortamos a 15
    List<Movimientos> listaCorta = ultimosMovimientos.stream()
            .sorted((m1, m2) -> m2.getFecha().compareTo(m1.getFecha()))
            .limit(10)
            .collect(Collectors.toList());
            
    model.addAttribute("ultimosMovimientos", listaCorta);
    
    return "inventario";
}
    
@GetMapping("/reporte-pdf")
public void generarPdfFiltrado(
        @RequestParam("idEmpleado") Integer idEmpleado,
        @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inicio,
        @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fin,
        HttpServletResponse response) {
   try {
        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=Reporte_Inventario_" + idEmpleado + ".pdf";
        response.setHeader("Content-Disposition", headerValue);

        List<Movimientos> lista = movRepo.findByEmpleadoAndFechaBetween(idEmpleado, inicio, fin);
        
        exportadorService.exportar(response, lista, inicio, fin);
        
        // AÑADE ESTO:
        response.flushBuffer(); 
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @GetMapping("/inventario/reportes")
public String mostrarPaginaReportes(Model model) {
    model.addAttribute("empleados", empRepo.findAll());
    return "reportes"; // Esto buscará el archivo reportes.html que creamos antes
}
 @PostMapping("/inventario/guardar") 
public String guardarRegistro(
        @RequestParam("idEmpleado") Integer idEmpleado,
        @RequestParam Map<String, String> allParams) { // Recibimos todo en un mapa

    // 1. Buscamos al empleado
    Empleado empleado = empRepo.findById(idEmpleado).orElse(null);
    if (empleado == null) return "redirect:/inventario?error=empleado";

    boolean huboMovimiento = false;

    // 2. Filtramos los parámetros para sacar solo los que empiezan por "cantidades["
    for (String key : allParams.keySet()) {
        if (key.startsWith("cantidades[")) {
            // Extraemos el ID del artículo de la clave: "cantidades[5]" -> "5"
            String idStr = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
            Integer idArt = Integer.parseInt(idStr);
            Integer cantidad = Integer.parseInt(allParams.get(key));

            // 3. Si la cantidad es mayor a 0, procesamos
            if (cantidad > 0) {
                Optional<Articulo> optArt = artRepo.findById(idArt);
                if (optArt.isPresent()) {
                    Articulo articulo = optArt.get();
                    
                    // Crear el movimiento
                    Movimientos mov = new Movimientos();
                    mov.setIdArticulo(articulo);
                    mov.setIdEmpleado(empleado);
                    mov.setCantidad(cantidad);
                    mov.setFecha(new Date());
                    movRepo.save(mov);

                    // Restar stock
                    articulo.setCantidad(articulo.getCantidad() - cantidad);
                    artRepo.save(articulo);
                    
                    huboMovimiento = true;
                }
            }
        }
    }

    // 4. Validación: ¿Se registró algo?
    if (!huboMovimiento) {
        return "redirect:/inventario?error=vacio";
    }

  // Al final de guardarRegistro en el controlador:
return "redirect:/inventario?exito=guardado&idEmpleado=" + idEmpleado;
}

    @GetMapping("/empleados/nuevo")
    public String formularioNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "nuevo-empleado";
    }

    @PostMapping("/empleados/guardar")
    public String guardarEmpleado(@ModelAttribute Empleado empleado) {
        empleado.setActivo(true);
        empRepo.save(empleado);
        return "redirect:/inventario";
    }

    @GetMapping("/articulos/nuevo")
    public String formularioNuevoArticulo(Model model) {
        model.addAttribute("articulo", new Articulo());
        return "nuevo-articulo";
    }

   @PostMapping("/articulos/guardar")
public String guardarArticulo(@ModelAttribute Articulo articulo) {
    if (articulo.getCantidad() == null) {
        articulo.setCantidad(0);
    }
    // Aseguramos que la sección se guarde en mayúsculas para evitar errores de filtrado
    if (articulo.getSeccion() != null) {
        articulo.setSeccion(articulo.getSeccion().toUpperCase());
    }
    artRepo.save(articulo);
    
    // Redirige al inventario (menú de botones) con mensaje de éxito
    return "redirect:/inventario?exito=articulo_creado";
}

    // --- CLASES INTERNAS LIMPIAS CON LOMBOK ---

    public static class MovimientoDTO {

        private Integer idEmpleado;

        public Integer getIdEmpleado() {
            return idEmpleado;
        }

        public void setIdEmpleado(Integer idEmpleado) {
            this.idEmpleado = idEmpleado;
        }

        public void setItems(List<ItemDTO> items) {
            this.items = items;
        }

        public List<ItemDTO> getItems() {
            return items;
        }
        private List<ItemDTO> items;
    }
  
   
    public static class ItemDTO {

        public Integer getIdArticulo() {
            return idArticulo;
        }

        public void setIdArticulo(Integer idArticulo) {
            this.idArticulo = idArticulo;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        private Integer idArticulo;
        private Integer cantidad;
    }
}
