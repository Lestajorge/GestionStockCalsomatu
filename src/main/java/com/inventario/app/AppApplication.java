package com.inventario.app;

import com.inventario.app.model.Articulo;
import com.inventario.app.repository.ArticuloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

//    @Bean 
//    public CommandLineRunner test(ArticuloRepository articuloRepo) {
//        return args -> {
//            // 1. Borramos todo lo que haya en la tabla
//            articuloRepo.deleteAll();
//            System.out.println("--- Tabla limpia ---");
//
//            // 2. Insertamos solo uno limpio
//            Articulo a1 = Articulo.builder()
//                    .nombre("Radial Bosh")
//                    .cantidad(10)
//                    .build();
//
//            articuloRepo.save(a1);
//            System.out.println("¡Ahora solo queda un artículo!");
//        };
//    }
}
