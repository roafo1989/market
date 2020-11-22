package ru.roafo.market.service.priceHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
@Transactional
public class PriceHistoryServiceImpl implements PriceHistoryService {
    private final PriceHistoryRepo priceHistoryRepo;
    private final ProductRepo productRepo;
    private static final Logger logger = LoggerFactory.getLogger(PriceHistoryServiceImpl.class);

    @Autowired
    public PriceHistoryServiceImpl(PriceHistoryRepo priceHistoryRepo, ProductRepo productRepo) {
        this.productRepo = productRepo;
        this.priceHistoryRepo = priceHistoryRepo;
    }

    @Override
    public List<PriceHistoryDTO> getActualByDate(LocalDate localDate) {
        logger.info("PriceHistoryServiceImpl.getActualByDate(): date= {} is STARTED", localDate);
        List<Price> priceHistories = priceHistoryRepo.findAllByDate(localDate);
        List<PriceHistoryDTO> result = new ArrayList<>();
        for (Price price : priceHistories) {
            PriceHistoryDTO priceHistoryDTO = new PriceHistoryDTO(price);
            logger.info("Created priceDTO: {}", priceHistoryDTO);
            result.add(priceHistoryDTO);
        }
        logger.info("Get total qty of prices: {}", result.size());
        return result;
    }

    @Override
    public List<PriceHistoryByProductDTO> getStatisticByProduct() {
        logger.info("PriceHistoryServiceImpl.getStatisticByProduct() is STARTED");
        List<PriceHistoryByProductDTO> result = new ArrayList<>();
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            PriceHistoryByProductDTO priceHistoryByProductDTO = new PriceHistoryByProductDTO(product.getName(), product.getPriceList().size());
            logger.info("Created priceHistoryByProductDTO: {}", priceHistoryByProductDTO);
            result.add(priceHistoryByProductDTO);
        }
        logger.info("Statistic report by product with: {} rows is CREATED", result.size());
        return result;
    }

    @Override
    public List<PriceHistoryByDateDTO> getStatisticByDate() {
        logger.info("PriceHistoryServiceImpl.getStatisticByDate() is STARTED");
        List<PriceHistoryByDateDTO> result = new ArrayList<>();
        List<?> tempList = priceHistoryRepo.getStatisticByDate();
        for (Object arrayFromList : tempList) {
            Object[] array = (Object[]) arrayFromList;
            LocalDate date = (LocalDate) array[0];
            Long qty = (Long) array[1];
            PriceHistoryByDateDTO priceHistoryByDateDTO = new PriceHistoryByDateDTO(date, qty.intValue());
            logger.info("Created priceHistoryByDateDTO: {}", priceHistoryByDateDTO);
            result.add(priceHistoryByDateDTO);
        }
        logger.info("Statistic report by date with {} rows is CREATED", result.size());
        return result;
    }

    @Override
    public StatisticDTO getStatistic() {
        logger.info("PriceHistoryServiceImpl.getStatistic() is STARTED");

        Callable<Integer> productQtyTask = () -> productRepo.findAll().size();
        FutureTask<Integer> productQtyFuture = new FutureTask<>(productQtyTask);
        new Thread(productQtyFuture).start();
        logger.info("Thread for find qty of product is STARTED");

        Callable<List<PriceHistoryByDateDTO>> statisticByDateTask = () -> getStatisticByDate();
        FutureTask<List<PriceHistoryByDateDTO>> statisticByDateFuture = new FutureTask<>(statisticByDateTask);
        new Thread(statisticByDateFuture).start();
        logger.info("Thread for get statistic by date is STARTED");

        Callable<List<PriceHistoryByProductDTO>> statisticByProductTask = () -> getStatisticByProduct();
        FutureTask<List<PriceHistoryByProductDTO>> statisticByProductFuture = new FutureTask<>(statisticByProductTask);
        new Thread(statisticByProductFuture).start();
        logger.info("Thread for get statistic by product is STARTED");

        Integer productQty;
        List<PriceHistoryByDateDTO> statisticByDate;
        List<PriceHistoryByProductDTO> statisticByProduct;
        try {
            productQty = productQtyFuture.get();
            logger.info("Thread for find qty of product is FINISHED");
            statisticByDate = statisticByDateFuture.get();
            logger.info("Thread for get statistic by date is FINISHED");
            statisticByProduct = statisticByProductFuture.get();
            logger.info("Thread for get statistic by product is FINISHED");
        } catch (InterruptedException | ExecutionException e) {
            logger.error("PriceHistoryServiceImpl.getStatistic(): fail to get statistics", e);
            return null;
        }

        StatisticDTO statisticDTO = new StatisticDTO(productQty, statisticByDate, statisticByProduct);
        logger.info("PriceHistoryServiceImpl.getStatistic(): statisticDTO {} is CREATED", statisticDTO);
        return statisticDTO;
    }
}
