package com.isep.acme;

import com.isep.acme.Model.*;
import com.isep.acme.Service.ProductService;
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

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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
        if(service.verifyIfExists(productUser)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This product Manager already approved");
        final Boolean productDTO = service.approveByUser(productUser.getSku(), productUser.getUsername());
        if(!productDTO) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Is not a Product Manager");
        service.updateStatus(productUser);
        Optional<Product> updatedProductToPublish = service.getProductBySku(productUser.getSku());
        ProductType productWithType = new ProductType(updatedProductToPublish.get(),"update");
        rabbitMQMessageProducer.publish(
                productWithType,
                "internal.exchange",
                "internal.product.routing-key");
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

        Product product = service.deleteBySku(sku);
        ProductType productWithType = new ProductType(product,"delete");
        rabbitMQMessageProducer.publish(
                productWithType,
                "internal.exchange",
                "internal.product.routing-key");
        return ResponseEntity.noContent().build();
    }
}
