package com.inventario.app.controller;


import com.inventario.app.model.Articulo;
import com.inventario.app.model.Empleado;
import com.inventario.app.model.Movimientos;
import com.inventario.app.repository.ArticuloRepository;
import com.inventario.app.repository.EmpleadoRepository;
import com.inventario.app.repository.MovimientosRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;


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

    
    
    @GetMapping("/inventario")
    public String verPagina(Model model) {
        model.addAttribute("articulos", artRepo.findAll());
        model.addAttribute("empleados", empRepo.findAll());
        model.addAttribute("movimientos", movRepo.findAllByOrderByIdMovimientosDesc());
        return "inventario";
    }
    

//     @GetMapping("/reporte-pdf")
// @ResponseBody // Añade esta anotación temporalmente para ver el texto en el navegador
// public String pruebaLlegada(
//         @RequestParam("idEmpleado") Integer idEmpleado,
//         @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
//         @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
    
//     return "¡CONEXIÓN EXITOSA! Has pedido el empleado: " + idEmpleado + 
//          " desde " + inicio + " hasta " + fin;
// }
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
   @PostMapping("/inventario/guardar") // Cambiado para coincidir con el HTML que te pasé
    public String guardarRegistro(
            @RequestParam("idEmpleado") Integer idEmpleado,
            @RequestParam("articulosIds") List<Integer> articulosIds,
            @RequestParam("cantidades") List<Integer> cantidades) {

    // 1. VALIDACIÓN: ¿Ha seleccionado algo?
    // Comprobamos si la lista es nula, está vacía o si todas las cantidades son 0
    boolean tieneArticulos = articulosIds != null && !articulosIds.isEmpty() &&  cantidades.stream().anyMatch(c -> c != null && c > 0);

    if (!tieneArticulos) {
        // Si no hay nada, redirigimos con un mensaje de aviso
        return "redirect:/inventario?error=vacio";
    }

        // 1. Buscamos al empleado
        Empleado empleado = empRepo.findById(idEmpleado).orElse(null);
        if (empleado == null) return "redirect:/inventario?error=empleado";

        // 2. Recorremos las listas que vienen de las tarjetas
        for (int i = 0; i < articulosIds.size(); i++) {
            Integer cantidad = cantidades.get(i);
            
            // Solo guardamos si el usuario puso un número mayor a 0 en la tarjeta
            if (cantidad != null && cantidad > 0) {
                Integer idArt = articulosIds.get(i);
                artRepo.findById(idArt).ifPresent(articulo -> {
                    // Crear movimiento
                    Movimientos mov = new Movimientos();
                    mov.setIdArticulo(articulo);
                    mov.setIdEmpleado(empleado);
                    mov.setCantidad(cantidad);
                    mov.setFecha(new Date());
                    movRepo.save(mov);

                    // Restar stock
                    articulo.setCantidad(articulo.getCantidad() - cantidad);
                    artRepo.save(articulo);
                });
            }
        }

        // 3. Redirigir de vuelta a la página principal
        return "redirect:/inventario";
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
        artRepo.save(articulo);
        return "redirect:/inventario";
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
