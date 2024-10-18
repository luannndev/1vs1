package dev.luan.vs.utilities;

import lombok.SneakyThrows;

public class RandomStringGenerator {

    public static enum Mode {
        ALPHA, ALPHANUMERIC, ALPHANUMERICLOWERCASE, ALPHANUMERICUPPERCASE, NUMERIC
    }

    @SneakyThrows
    public static String generateRandomString(int length, Mode mode) {

        StringBuffer buffer = new StringBuffer();
        String characters = "";

        switch (mode) {

            case ALPHA:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                break;

            case ALPHANUMERIC:
                characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678901234567890";
                break;

            case ALPHANUMERICLOWERCASE:
                characters = "abcdefabcdefabcdefabcdef12345678901234567890";
                break;

            case ALPHANUMERICUPPERCASE:
                characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345678901234567890";
                break;

            case NUMERIC:
                characters = "1234567890";
                break;
        }

        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }
}

