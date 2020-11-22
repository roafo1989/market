package ru.roafo.market.service.priceHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.roafo.market.Application;
import ru.roafo.market.domain.Price;
import ru.roafo.market.domain.Product;
import ru.roafo.market.dto.PriceHistoryByDateDTO;
import ru.roafo.market.dto.PriceHistoryByProductDTO;
import ru.roafo.market.dto.PriceHistoryDTO;
import ru.roafo.market.dto.StatisticDTO;
import ru.roafo.market.repository.PriceHistoryRepo;
import ru.roafo.market.repository.ProductRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PriceHistoryServiceImplement implements PriceHistoryService {
    private final PriceHistoryRepo priceHistoryRepo;
    private final ProductRepo productRepo;
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private Integer productQty;
    private List<PriceHistoryByDateDTO> statisticByDate;
    private List<PriceHistoryByProductDTO> statisticByProduct;
    private StatisticDTO statisticDTO;

    @Value("${filePath}")
    private String filePath;

    @Autowired
    public PriceHistoryServiceImplement(PriceHistoryRepo priceHistoryRepo, ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.priceHistoryRepo = priceHistoryRepo;
    }

    @Override
    public Price create(Price history) {
        return priceHistoryRepo.save(history);
    }

    @Override
    public List<Price> createList(List<Price> historyList) {
        return priceHistoryRepo.saveAll(historyList);
    }

    @Override
    public Price update(Price history) {
        return priceHistoryRepo.save(history);
    }

    @Override
    public void delete(Long id) {
        priceHistoryRepo.deleteById(id);
    }

    @Override
    public Price get(Long id) {
        return priceHistoryRepo.getOne(id);
    }

    @Override
    public List<Price> getAll() {
        return priceHistoryRepo.findAll();
    }

    @Override
    public List<Price> getByProduct(Long productId) {
        return priceHistoryRepo.findAllByProduct(productId);
    }

    @Override
    public List<PriceHistoryDTO> getActualByDate(LocalDate localDate) {
        List<Price> priceHistories = priceHistoryRepo.findAllByDate(localDate);
        List<PriceHistoryDTO> result = new ArrayList<>();
        for (Price price : priceHistories) {
            result.add(new PriceHistoryDTO(price));
        }
        return result;
    }

    @Override
    public List<PriceHistoryByProductDTO> getStatisticByProduct() {
        logger.info("PriceHistoryServiceImplement.getStatisticByProduct() is started");
        List<PriceHistoryByProductDTO> result = new ArrayList<>();
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            PriceHistoryByProductDTO priceHistoryByProductDTO = new PriceHistoryByProductDTO(product.getName(), product.getPriceList().size());
            logger.info("Created priceHistoryByProductDTO: " + priceHistoryByProductDTO);
            result.add(priceHistoryByProductDTO);
        }
        return result;
    }

    @Override
    public List<PriceHistoryByDateDTO> getStatisticByDate() {
        logger.info("PriceHistoryServiceImplement.getStatisticByDate() is started");
        List<PriceHistoryByDateDTO> result = new ArrayList<>();
        List<?> tempList = priceHistoryRepo.getStatisticByDate();
        for (Object arrayFromList : tempList) {
            Object[] array = (Object[]) arrayFromList;
            LocalDate date = (LocalDate) array[0];
            Long qty = (Long) array[1];
            PriceHistoryByDateDTO priceHistoryByDateDTO = new PriceHistoryByDateDTO(date, qty.intValue());
            logger.info("Created priceHistoryByDateDTO: " + priceHistoryByDateDTO);
            result.add(priceHistoryByDateDTO);
        }
        return result;
    }

    @Override
    public StatisticDTO getStatistic() {
        logger.info("PriceHistoryServiceImplement.getStatistic() is started");

        productQty = productRepo.findAll().size();
        statisticByDate = getStatisticByDate();
        statisticByProduct = getStatisticByProduct();
        statisticDTO = new StatisticDTO(productQty, statisticByDate, statisticByProduct);

        logger.info("Created statisticDTO: " + statisticDTO);
        return null;
    }

    @Override
    public void parseCsv(List<String> fileLines) throws IOException {
        logger.info("Start parse prices.csv file...");
        List<Product> products = new ArrayList<>();
        List<Long> productIdList = new ArrayList<>();

        List<Price> prices = new ArrayList<>();
        List<Long> priceIdList = new ArrayList<>();

       // List<String> fileLines = Files.readAllLines(Paths.get(filePath));
        for (String fileLine : fileLines) {
            String[] row = fileLine.split(";");
            List<String> cellContentArray = new ArrayList<>();
            Collections.addAll(cellContentArray, row);

            Long productId = Long.parseLong(cellContentArray.get(0));
            String productName = cellContentArray.get(1);
            Long priceId = Long.parseLong(cellContentArray.get(2));
            Float price = Float.parseFloat(cellContentArray.get(3));
            LocalDate date = LocalDate.parse(cellContentArray.get(4));

            Product productEntity = new Product(productId, productName);
            if(!productIdList.contains(productId)) {
                productIdList.add(productId);
                products.add(productEntity);
            }
            Price priceEntity = new Price(priceId, price, date, productEntity);
            if(!priceIdList.contains(priceId)){
                priceIdList.add(priceId);
                prices.add(priceEntity);
            }
        }
        logger.info("Processing completed. Processed " + fileLines.size() + " lines in prices.csv");
        productRepo.saveAll(products);
        logger.info("Created " + products.size() + " products");
        priceHistoryRepo.saveAll(prices);
        logger.info("Created " + prices.size() + " prices");
    }

}
