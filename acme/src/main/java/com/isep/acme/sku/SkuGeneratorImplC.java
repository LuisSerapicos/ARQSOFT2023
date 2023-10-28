package com.isep.acme.sku;

import org.springframework.stereotype.Component;

@Component("skuC")
public class SkuGeneratorImplC implements SkuGenerator{
    @Override
    public void generateSku(String designation) {
        System.out.println("Generate skuC");
    }
}
