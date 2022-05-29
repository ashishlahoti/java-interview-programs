package com.example.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileUtil {


    public static void main(String[] args) throws IOException {
        //zipFile(Path.of("/Users/ashl/toZip/1.png"), Path.of("/Users/ashl/zip/singleFileArchive.zip"));
        List<Path> filesToZip = Arrays.asList(Path.of("/Users/ashl/toZip/1.png"), Path.of("/Users/ashl/toZip/2.png"));
        zipMultipleFiles(filesToZip, Path.of("/Users/ashl/zip/multiFileArchive.zip"));
        zipDirectory(Path.of("/Users/ashl/toZip"), Path.of("/Users/ashl/zip/fullDirectoryArchive.zip"));
        //unzipDirectory(Path.of("/Users/ashl/zip/myZip.zip"), Path.of("/Users/ashl/toUnzip"));
    }

    public static void zipFile(Path fileToZip, Path zipFile) throws IOException {
        Files.createDirectories(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            ZipEntry zipEntry = new ZipEntry(fileToZip.toFile().getName());
            zipOutputStream.putNextEntry(zipEntry);
            if (Files.isRegularFile(fileToZip)) {
                Files.copy(fileToZip, zipOutputStream);
            }
        }
    }

    public static void zipMultipleFiles(List<Path> filesToZip, Path zipFile) throws IOException {
        Files.createDirectories(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            for(Path fileToZip: filesToZip){
                ZipEntry zipEntry = new ZipEntry(fileToZip.toFile().getName());
                zipOutputStream.putNextEntry(zipEntry);
                if (Files.isRegularFile(fileToZip)) {
                    Files.copy(fileToZip, zipOutputStream);
                }
            }
        }
    }

    public static void zipDirectory(Path directoryToZip, Path zipFile) throws IOException {
        Files.deleteIfExists(zipFile);
        Files.createDirectories(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isDirectory(directoryToZip)) {
                Files.walk(directoryToZip).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                    System.out.println(path.toAbsolutePath());
                    ZipEntry zipEntry = new ZipEntry(directoryToZip.relativize(path).toString());
                    try {
                        zipOutputStream.putNextEntry(zipEntry);
                        if (Files.isRegularFile(path)) {
                            Files.copy(path, zipOutputStream);
                        }
                        zipOutputStream.closeEntry();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                });
            }
        }
    }

    public static void unzipDirectory(Path zipFile, Path targetDirectory) throws IOException {
        if (!Files.exists(zipFile)) {
            return;
        }
        deleteDirectoryRecursively(targetDirectory);
        Files.createDirectory(targetDirectory);
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final Path toPath = targetDirectory.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(toPath);
                } else {
                    if (!Files.exists(toPath.getParent())) {
                        Files.createDirectories(toPath.getParent());
                    }
                    Files.copy(zipInputStream, toPath);
                }
            }
        }
    }

    private static void deleteDirectoryRecursively(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }
}
