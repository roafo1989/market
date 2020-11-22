package ru.roafo.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.roafo.market.service.priceHistory.PriceHistoryService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
public class Application {
    @Value("${filePath}")
    private String filePath;

    @Autowired
    private PriceHistoryService priceHistoryService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void scanFiles() throws IOException {
        Path PATH = Paths.get(filePath);
        Integer filesQty = 0;
        while (true) {
            List<Path> list = Files.find(PATH, 1, (p, a) ->
                    a.isRegularFile() && p.getFileName().toString().endsWith(".csv")).collect(Collectors.toList());
            System.out.println(list.size());
            if (filesQty < list.size()) {
                filesQty = list.size();
                List<String> lines = Files.readAllLines(list.get(list.size() - 1));
                priceHistoryService.parseCsv(lines);
            }
        }


    }
}