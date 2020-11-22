package ru.roafo.market.service.priceHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PriceHistoryServiceImplement implements PriceHistoryService {
    private final PriceHistoryRepo priceHistoryRepo;
    private final ProductRepo productRepo;
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

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
        Integer productQty = productRepo.findAll().size();
        List<PriceHistoryByDateDTO> statisticByDate = getStatisticByDate();
        List<PriceHistoryByProductDTO> statisticByProduct = getStatisticByProduct();
        StatisticDTO statisticDTO = new StatisticDTO(productQty, statisticByDate, statisticByProduct);
        logger.info("Created statisticDTO: " + statisticDTO);
        return statisticDTO;
    }
}
