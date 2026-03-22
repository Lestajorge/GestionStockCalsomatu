package com.inventario.app.service;

import com.inventario.app.model.Articulo;
import com.inventario.app.repository.ArticuloRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service

public class ArticuloService {

    
    private final ArticuloRepository articuloRepo; 

    public ArticuloService(ArticuloRepository articuloRepo) {
        this.articuloRepo = articuloRepo;
    }

    public Articulo guardarArticulo(Articulo a) {

        return articuloRepo.save(a);

    }

    public List<Articulo> obtenerTodos() {

        return articuloRepo.findAll();

    }

    public Articulo modificar(Articulo a) {

        return articuloRepo.save(a);

    }

    public List<Articulo> listarTodos() {
           return articuloRepo.findAll();
    }

}
