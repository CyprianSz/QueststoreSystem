package pl.coderampart.controller;

import java.util.UUID;
import java.lang.Byte;

public class UUIDController {

    public static String createUUID() {
        UUID uuid = UUID.randomUUID(); // Create type 4 (pseudo randomly generated) UUID

        return uuid.toString();
    }

    public static String createUUID(String name) {
        byte[] nameInBytes = name.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(nameInBytes); // Create type 3 (name based) UUID

        return uuid.toString();
    }
}
