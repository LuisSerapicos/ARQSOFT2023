package com.isep.acme.sku;

import org.springframework.stereotype.Component;

@Component("skuA")
public class SkuGeneratorImplA implements SkuGenerator {
    @Override
    public void generateSku(String designationProduct) {
        System.out.println("Generate skuA");
    }
}
