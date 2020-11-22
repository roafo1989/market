package ru.roafo.market.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.roafo.market.domain.Price;
import ru.roafo.market.domain.Product;
import ru.roafo.market.repository.PriceHistoryRepo;
import ru.roafo.market.repository.ProductRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ImportServiceImpl implements ImportService{

    private final PriceHistoryRepo priceHistoryRepo;
    private final ProductRepo productRepo;

    private static final Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);

    @Autowired
    public ImportServiceImpl(PriceHistoryRepo priceHistoryRepo, ProductRepo productRepo) {
        this.priceHistoryRepo = priceHistoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void importProductsAndPricesFromCsv(Path path){
        logger.info("ImportServiceImpl.importProductsAndPricesFromCsv(): file={} is STARTED", path.getFileName());
        List<List<String>> csvRows = parseCsvToRows(path);
        mapRowsAndSaveProductAndPrices(csvRows);
    }

    private List<List<String>> parseCsvToRows(Path path) {
        logger.info("ImportServiceImpl.parseCsvToRows() with path={} is STARTED", path);
        List<String> fileLines;
        List<List<String>> rowContentList = new ArrayList<>();
        try {
            fileLines = Files.readAllLines(path);
            for (String fileLine : fileLines) {
                String[] cellContentArray = fileLine.split(";");
                List<String> cellContentList = new ArrayList<>();
                Collections.addAll(cellContentList, cellContentArray);
                rowContentList.add(cellContentList);
            }
        } catch (IOException e) {
            logger.error("ImportServiceImpl.parseCsvToRows(): fail to parse csv", e);
        }

        logger.info("ImportServiceImpl.parseCsvToRows(): rows count={}", rowContentList.size());
        return rowContentList;
    }

    private void mapRowsAndSaveProductAndPrices(List<List<String>> csvRows) {
        logger.info("Parse prices.csv file is STARTED...");
        List<Product> products = new ArrayList<>();
        List<Long> productIdList = new ArrayList<>();

        List<Price> prices = new ArrayList<>();
        List<Long> priceIdList = new ArrayList<>();

        for (List<String> csvRow : csvRows) {
            Long productId = Long.parseLong(csvRow.get(0));
            String productName = csvRow.get(1);
            Long priceId = Long.parseLong(csvRow.get(2));
            Float price = Float.parseFloat(csvRow.get(3));
            LocalDate date = LocalDate.parse(csvRow.get(4));

            Product productEntity = new Product(productId, productName);
            if (!productIdList.contains(productId)) {
                productIdList.add(productId);
                products.add(productEntity);
            }
            Price priceEntity = new Price(priceId, price, date, productEntity);
            if (!priceIdList.contains(priceId)) {
                priceIdList.add(priceId);
                prices.add(priceEntity);
            }
        }
        logger.info("Processing completed. Processed " + csvRows.size() + " lines in prices.csv");
        productRepo.saveAll(products);
        logger.info("Created {} products", products.size());
        priceHistoryRepo.saveAll(prices);
        logger.info("Created {} prices", prices.size());
    }
}
