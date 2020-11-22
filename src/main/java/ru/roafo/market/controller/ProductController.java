package ru.roafo.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.roafo.market.dto.PriceHistoryDTO;
import ru.roafo.market.dto.StatisticDTO;
import ru.roafo.market.service.priceHistory.PriceHistoryService;

import java.time.LocalDate;
import java.util.List;

import static ru.roafo.market.controller.ProductController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    public static final String REST_URL = "/products";

    private PriceHistoryService priceHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<PriceHistoryDTO> getActualByDate(@RequestParam String date) {
        logger.info("ProductController.getActualByDate(): localDate= {} is STARTED", date);
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            logger.info("LocalDate.parse(date): failToParseDate", e);
            e.printStackTrace();
            return null;
        }
        List<PriceHistoryDTO> priceHistoryDTOList = this.priceHistoryService.getActualByDate(localDate);
        logger.info("ProductController.priceHistoryDTOList size={}", priceHistoryDTOList.size());
        return priceHistoryDTOList;
    }

    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public StatisticDTO getStatistic() {
        logger.info("ProductController.getStatistic() is STARTED");
        StatisticDTO statisticDTO = this.priceHistoryService.getStatistic();
        logger.info("ProductController.statisticDTO= {}", statisticDTO);
        return statisticDTO;
    }
}
