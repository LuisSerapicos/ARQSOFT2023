package com.isep.acme;

import com.isep.acme.Model.Product;
import com.isep.acme.Model.ProductDTO;
import com.isep.acme.Model.ProductUser;
import com.isep.acme.Service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("api/v1/product")
@AllArgsConstructor
public class Controller {

    @Autowired
    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> create(@RequestBody Product manager) {
        final ProductDTO product = service.create(manager);
        log.info("Post customer registration");
        return new ResponseEntity<ProductDTO>(product, HttpStatus.CREATED);

    }

    @PatchMapping(value = "/approve")
    public ResponseEntity<ProductUser> approve(@RequestBody final ProductUser productUser) {
        System.out.println("Approve");
        final Boolean productDTO = service.approveByUser(productUser.getSku(), productUser.getUsername());
        if(service.verifyIfExists(productUser.getUsername())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This product Manager already approved");
        if(!productDTO) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Is not a Product Manager");
        /*service.updateStatus();*/
        return ResponseEntity.ok().body(productUser);
    }


    @PatchMapping(value = "/{sku}")
    public ResponseEntity<ProductDTO> Update(@PathVariable("sku") final String sku, @RequestBody final Product product) {

        final ProductDTO productDTO = service.updateBySku(sku, product);

        if (productDTO == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        else
            return ResponseEntity.ok().body(productDTO);
    }


    @DeleteMapping(value = "/{sku}")
    public ResponseEntity<Product> delete(@PathVariable("sku") final String sku) {

        service.deleteBySku(sku);
        return ResponseEntity.noContent().build();
    }
}
