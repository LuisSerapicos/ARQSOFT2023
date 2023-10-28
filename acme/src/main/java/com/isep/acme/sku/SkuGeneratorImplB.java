package com.isep.acme.sku;

import org.springframework.stereotype.Component;

@Component("skuB")
public class SkuGeneratorImplB implements SkuGenerator {
    @Override
    public String generateSku(String designation) {

        System.out.println("Generate skuB");
        return designation;
    }
}
