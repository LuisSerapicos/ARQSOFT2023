package com.isep.acme1;

import com.isep.acme1.Model.ProductDTO;
import com.isep.acme1.Model.ProductUserDTO;
import com.isep.acme1.Service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/product")
@AllArgsConstructor
public class Controller {

    @Autowired
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Iterable<ProductUserDTO>> getCatalog() {
        log.info("Get customer registration");
        final var products = service.getCatalog();
        return ResponseEntity.ok().body(products);
    }


    @GetMapping(value = "/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable("sku") final String sku) {
        System.out.println("Controller:" + sku);
        final Optional<ProductDTO> product = service.findBySku(sku);

        if (product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        else
            return ResponseEntity.ok().body(product.get());
    }

    @GetMapping(value = "/designation/{designation}")
    public ResponseEntity<Iterable<ProductDTO>> findAllByDesignation(@PathVariable("designation") final String designation) {

        final Iterable<ProductDTO> products = service.findByDesignation(designation);

        return ResponseEntity.ok().body(products);
    }

}
