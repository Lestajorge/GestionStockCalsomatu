package com.inventario.app.controller;

import com.inventario.app.model.Movimientos;
import com.inventario.app.service.EmpleadoService;
import com.inventario.app.service.MovimientosService;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/api/movimientos")
@RestController

public class MovimientosController {

    private final MovimientosService movService;
    private final EmpleadoService empService;

    public MovimientosController(MovimientosService movService, EmpleadoService empService) {
        this.movService = movService;
        this.empService = empService;
    }

   

    @PostMapping("/crear")
    public Movimientos crearMovimiento(@RequestBody Movimientos m) {

        return movService.crearMovimiento(m);

    }

    @PostMapping("/guardar")
    public Movimientos guardarMovimiento(@RequestBody Movimientos m) {

        return movService.guardarMovimiento(m);
    }

    @GetMapping("/obtener")
    public Movimientos obtenerMovimientoPorId(@RequestParam Integer id) {
        return movService.obtenerMovimientoPorId(id);
    }

    @GetMapping("/listar")
    public List<Movimientos> obtenerMovimientos(@PathVariable Integer id) {

        return movService.obtenerMovimientos(id);

    }

}
