package com.inventario.app.controller;

import com.inventario.app.service.EmpleadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final EmpleadoService empService;

    // Constructor para inyectar el servicio de empleados
    public ReporteController(EmpleadoService empService) {
        this.empService = empService;
    }

    // Esta es la ruta que abrirá tu página de selección
    @GetMapping("/seleccionar-empleado")
    public String mostrarFormularioSeleccion(Model model) {
        // Cargamos los empleados para el desplegable del HTML
        model.addAttribute("empleados", empService.listarEmpleados());
        return "seleccionar-empleado"; // Nombre exacto de tu archivo .html
    }
}