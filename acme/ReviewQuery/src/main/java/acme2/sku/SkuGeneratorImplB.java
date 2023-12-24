package acme2.sku;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component("skuB")
public class SkuGeneratorImplB implements SkuGenerator {
    @Override
    public String generateSku(String designation) {
        try {
            // Create an MD5 hash of the product designation
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(designation.getBytes());

            // Convert the MD5 hash to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                hexString.append(String.format("%02x", b));
            }

            // Select the 10 middle characters of the hexadecimal hash
            int startIndex = (hexString.length() - 10) / 2;
            int endIndex = startIndex + 10;
            String sku = hexString.substring(startIndex, endIndex);

            System.out.println("SkuB: " + sku);

            return sku;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
