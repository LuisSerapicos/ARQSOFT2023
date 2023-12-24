package acme2.sku;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component("skuC")
public class SkuGeneratorImplC implements SkuGenerator {
    @Override
    public String generateSku(String designation) {
        SkuGeneratorImplA skuA = new SkuGeneratorImplA();
        SkuGeneratorImplB skuB = new SkuGeneratorImplB();

        String methodA = skuA.generateSku(designation);
        String methodB = skuB.generateSku(designation);
        Random random = new Random();
        StringBuilder randomStringA = new StringBuilder();
        StringBuilder randomStringB = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(methodA.length());
            char randomChar = methodA.charAt(randomIndex);
            randomStringA.append(randomChar);
        }

        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(methodB.length());
            char randomChar = methodB.charAt(randomIndex);
            randomStringB.append(randomChar);
        }

        String skuC = randomStringA + "" + randomStringB;
        System.out.println("SkuC: "+ skuC);
        return skuC;
    }
}

