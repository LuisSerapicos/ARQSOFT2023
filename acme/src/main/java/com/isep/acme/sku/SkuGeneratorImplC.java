package com.isep.acme.sku;

import org.springframework.stereotype.Component;

@Component("skuC")
public class SkuGeneratorImplC implements SkuGenerator{
    @Override
    public String generateSku(String designation) {
        System.out.println("Generate skuC");
        return designation;
    }
}
