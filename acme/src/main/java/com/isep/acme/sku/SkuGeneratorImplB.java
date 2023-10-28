package com.isep.acme.sku;

import org.springframework.stereotype.Component;

@Component("skuB")
public class SkuGeneratorImplB implements SkuGenerator {
    @Override
    public void generateSku(String designation) {

        System.out.println("Generate skuB");
    }
}
