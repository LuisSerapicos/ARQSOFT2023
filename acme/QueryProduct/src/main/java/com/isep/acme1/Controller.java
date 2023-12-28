package com.isep.acme1;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/product")
@AllArgsConstructor
public class Controller {
    @GetMapping
    public void getProduct() {
        log.info("Get customer registration");
    }
}
