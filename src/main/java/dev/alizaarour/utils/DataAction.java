package dev.alizaarour.utils;

import java.io.*;

public class DataAction {

    public static <T> T deserialize(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            @SuppressWarnings("unchecked")
            T object = (T) in.readObject();
            in.close();
            return object;
        }
    }

    public static <T> void serialize(T object, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(object);
            out.flush();
        }
    }
}
