package ru.roafo.market.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class FileServiceImpl implements FileService {

    @Value("${directory}")
    private String DIRECTORY;

    private ImportService importService;
    private ConcurrentLinkedQueue<Path> filesQueue = new ConcurrentLinkedQueue<>();
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    public FileServiceImpl(ImportService importService) {
        this.importService = importService;
    }

    @Override
    public void checkDirectoryForNewCsv() {
        logger.info("checkDirectoryForNewCsv(): directory={} is STARTED", DIRECTORY);
        try {
            Files.walk(Paths.get(DIRECTORY))
                    .filter(path1 -> Files.isRegularFile(path1))
                    .filter(path -> path.getFileName().toString().endsWith(".csv"))
                    .filter(path -> !filesQueue.contains(path))
                    .forEach(path -> filesQueue.add(path));

            logger.info("checkDirectoryForNewCsv(): files queue={}", filesQueue);
            while (!filesQueue.isEmpty()) {
                Path file = filesQueue.poll();
                importService.importProductsAndPricesFromCsv(file);
                /*Уточнить поведение после прочтения файла: удалять/перемещать/переименовывать?*/
                Files.delete(file);
            }
        } catch (IOException e) {
            logger.error("checkDirectoryForNewCsv(): failed to get files from directory=" + DIRECTORY, e);
        }
    }
}
