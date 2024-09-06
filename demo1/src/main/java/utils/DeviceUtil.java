package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class DeviceUtil {

    private static final String DEVICE_ID_FILE = "device_id.txt";

    public static String getDeviceId() {
        File file = new File(DEVICE_ID_FILE);

        if (!file.exists()) {
            String deviceId = UUID.randomUUID().toString();
            try {
                Files.write(file.toPath(), deviceId.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return deviceId;
        } else {
            try {
                return new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

