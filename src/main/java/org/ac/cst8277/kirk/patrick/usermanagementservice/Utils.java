package org.ac.cst8277.kirk.patrick.usermanagementservice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public final class Utils {
    public static final String HASH_ALGO = "SHA-256";

    public static byte[] toBytes(UUID id) {
        byte[] bytes = new byte[16];

        ByteBuffer.wrap(bytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(id.getMostSignificantBits())
                .putLong(id.getLeastSignificantBits());

        return bytes;
    }

    public static UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

    public static List<byte[]> toBytesList(List<UUID> ids) {
        List<byte[]> bytes = new ArrayList<>();

        for (UUID id : ids) {
            bytes.add(toBytes(id));
        }

        return bytes;
    }

    public static String hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGO);
            byte[] bytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(bytes);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return input;
    }
}