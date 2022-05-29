package com.example.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static void main(String[] args) {
        // Path basePath = Paths.get("C:\\Users\\ashl");
        Path basePath = Paths.get("/Users/ashl");

        // resolve()
        Path dataPath = basePath.resolve("data");
        Path picturePath = basePath.resolve("data").resolve("pictures");
        System.out.println(basePath);
        System.out.println(dataPath);
        System.out.println(picturePath);

        // relativize()
        System.out.println(basePath.relativize(picturePath));
        System.out.println(picturePath.relativize(basePath));

        System.out.println();

    }

    public static void searchFileInDirectory(Path dir) throws IOException {
        List<Path> files = Files.walk(dir).filter(p -> p.getFileName().endsWith(".txt")).collect(Collectors.toList());
    }

    public static void deleteDirectoryRecursively(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }
}
