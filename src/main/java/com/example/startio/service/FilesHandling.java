package com.example.startio.service;

import com.example.startio.domain.FileType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FilesHandling {

    public void readFileAndInsertToDB(String fileName) {
        Supplier<Stream<String>> streamSupplier = validateFile(fileName);
        if (streamSupplier == null) {
            return;
        }

        streamSupplier.get().findFirst();

        FileType fileType = FileType.valueOf(fileName.split(".")[0]);
        runBatch(fileType, streamSupplier);
    }

    protected Supplier<Stream<String>> validateFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Empty file name");
            return null;
        }
        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader.getResource(fileName) == null) {
            System.out.println("File does not exists");
            return null;
        }

        Supplier<Stream<String>> streamSupplier = () -> {
            try {
                return Files.lines(Paths.get(classLoader.getResource(fileName).toURI()));
            } catch (IOException e) {
                System.out.println("Empty file");
                return null;
            } catch (URISyntaxException e) {
                System.out.println("Empty file");
                return null;
            }
        };

        if (streamSupplier == null || streamSupplier.get() == null || !streamSupplier.get().findFirst().isPresent()) {
            System.out.println("Empty file");
            return null;
        }

        return streamSupplier;
    }

    public void runBatch(FileType fileType, Supplier<Stream<String>> streamSupplier){
        RunnableBatch runnableBatch = new RunnableBatch(fileType, 5000);
        Thread t = new Thread(runnableBatch);
        t.start();

        streamSupplier.get().forEach(runnableBatch::send);
        t.stop();
    }
}
