package com.inventario.app.controller;

import com.inventario.app.service.ExportadorPDF;
import com.inventario.app.model.Articulo;
import com.inventario.app.model.Movimientos;
import com.inventario.app.service.ArticuloService;
import com.inventario.app.service.MovimientosService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final MovimientosService movService;

    public ArticuloController(ArticuloService articuloService, MovimientosService mov) {
        this.articuloService = articuloService;
        this.movService = mov;
    }
 

    @PutMapping("/modificar")
    public Articulo modificarArticulo(@RequestBody Articulo a) {

        return articuloService.modificar(a);

    }

    @PostMapping("/crear")
    public Articulo guardarArticulo(@RequestBody Articulo a) {
        // @RequestBody le dice a Spring: "Coge el JSON de Postman y mételo en la variable 'a'"
        return articuloService.guardarArticulo(a);
    }

    @GetMapping("/listar")
    public List<Articulo> listarArticulos() {

        return articuloService.obtenerTodos();

    }

}
