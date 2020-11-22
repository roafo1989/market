package ru.roafo.market.controller;

import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.roafo.market.Application;
import ru.roafo.market.dto.PriceHistoryByDateDTO;
import ru.roafo.market.dto.PriceHistoryByProductDTO;
import ru.roafo.market.dto.PriceHistoryDTO;
import ru.roafo.market.dto.StatisticDTO;
import ru.roafo.market.service.priceHistory.PriceHistoryService;
import ru.roafo.market.service.product.ProductService;

import java.time.LocalDate;
import java.util.List;

import static ru.roafo.market.controller.ProductController.REST_URL;

@RestController
@RequestMapping(value = REST_URL,produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    public static final String REST_URL = "/products";

    private ProductService productService;
    private PriceHistoryService priceHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    public ProductController(ProductService productService, PriceHistoryService priceHistoryService) {
        this.productService = productService;
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public List<PriceHistoryDTO> getActualByDate(@RequestParam String date){
        LocalDate localDate = getDate(date);
        logger.info("ProductController localDate: " + localDate);
        List<PriceHistoryDTO> priceHistoryDTOList = this.priceHistoryService.getActualByDate(localDate);
        logger.info("ProductController priceHistoryDTOList size: " + priceHistoryDTOList.size());
        return priceHistoryDTOList;
    }

    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public StatisticDTO getStatistic(){
        StatisticDTO statisticDTO = this.priceHistoryService.getStatistic();
        logger.info("ProductController get statisticDTO: " + statisticDTO);
        return statisticDTO;
    }


    private static LocalDate getDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate == null ? LocalDate.now() : localDate;
    }
}
