package acme2.sku;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component("skuA")
public class SkuGeneratorImplA implements SkuGenerator {

    private static final int LETTERS_GROUP_1 = 3;
    private static final int DIGITS_GROUP_1 = 3;
    private static final int LETTERS_GROUP_2 = 2;
    private static final int DIGITS_GROUP_2 = 2;

    @Override
    public String generateSku(String designationProduct) {
        StringBuilder sku = new StringBuilder();

        generateRandomLetters(sku, LETTERS_GROUP_1);
        generateRandomDigits(sku, DIGITS_GROUP_1);

        // Generate the second group (2 letters + 1 number)
        generateRandomLetters(sku, LETTERS_GROUP_2);
        generateRandomDigits(sku, DIGITS_GROUP_2);

        // Add a special character
        sku.append(generateRandomSpecialCharacter());

        System.out.println(sku);
        return sku.toString();
    }

    private void generateRandomLetters(StringBuilder builder, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            char letter = (char) (random.nextInt(26) + 'a'); // Generating lowercase letters
            builder.append(letter);
        }
    }

    private void generateRandomDigits(StringBuilder builder, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
    }

    private char generateRandomSpecialCharacter() {
        // Generating a random character from a set of special characters
        String specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        Random random = new Random();
        int index = random.nextInt(specialCharacters.length());
        return specialCharacters.charAt(index);
    }
}
