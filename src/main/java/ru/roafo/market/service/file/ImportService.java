package ru.roafo.market.service.file;

import java.nio.file.Path;

public interface ImportService {
    void importProductsAndPricesFromCsv(Path path);
}
