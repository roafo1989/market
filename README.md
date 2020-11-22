# Лига Цифровой Экономики

Тестовое задание
<details>
ТЗ:

Реализовать приложение на базе фреймворка Spring Boot 2 и Java минимум 8 версии. 

Пример структуры таблиц в БД PostgreSQL.
1. Таблица товар. Хранит название товара.
Колонки: id, name.
2. Таблица цена товара. Хранит цену на товар и дату с которой цена актуальная. По каждому товару может быть несколько цен с разными датами.
Колонки: id, price, date, product_id.
Таблицы должны создаваться автоматически в БД при старте приложения или в приложении должен быть приложен файл со скриптом создания необходимых сущностей.

Функционал приложения.
1. Загрузка товаров и цен из csv-файла.
Приложение должно уметь загружать данные из csv-файла. Путь директории с файлами настраивается в конфигурационном файле приложения. 
Пример формат данных в csv-файла:
product_id;product_name;price_id;price;price_date
В логах должен быть отмечен факт старта обработки файла и результат обработки файла с количеством обработанных записей(товаров и цен).
Загрузка файла стартует при появлении файла в указанной директории.

2. Получение списка товаров и актуальных цен.
Приложение должно предоставлять Rest метод, возвращающий из БД список товаров с актуальными ценами, на указанный в запросе день.
GET /products?date=yyyy-mm-dd 
Формат данных ответа - json. Список {"name": "Товар 1", "price": 100.99} 

3. Получение статистики.
Приложение должно предоставлять Rest метод, возвращающий статистику по загруженным товарам и ценам.
GET /products/statistic 
Формат данных ответа - json.
Параметры статистики:
- Количество товаров в БД. Формат - просто цифра.
- Как часто менялась цена товара. Группировка по товарам. Формат - список {"name": "Товар 1", "frequency": 2} 
- Как часто менялась цена товара. Группировка по дням. Формат - список {"date": "yyyy-mm-dd", "frequency": 6} 
Каждый параметр статистики необходимо запрашивать в отдельном параллельном потоке.
</details>

Комментарии
<details>
Если бы это был реальный проект, то по ТЗ, думаю, стоило бы обсудить как минимум следующие вопросы:

1.В примере файла указываются id товаров и цен. Таким образом, мы создаём записи сами. Но в случае работы обычных методов create() генерацию id стоит сделать автоматической — например, @GeneratedValue(strategy=GenerationType.AUTO)​​​​​​​

2.Могут ли храниться в .csv названия в кавычках? (В текущей реализации не предусмотрено)

3.Что необходимо сделать с файлом после прочтения — удалить/переименовать/переместить?(В текущей реализации удаление — данные записаны в БД, папка очищается, чтобы не плодить мусор)

4.В каком формате хранить дату? В примере указана просто дата (в текущей реализации LocalDate), но на практике цены могут изменить несколько раз за день, тогда стоит использовать LocalDateTime

5.Возможны ли опечатки в названии товаров? Например, в файле для id=1 первый раз название «стол», второй — «стл», затем «Стол». Что указывать в БД? Требуется ли после первого создания перезаписывать? (В текущей реализации старался минимизировать запросы в БД)

6.Какова стратегия решения конфликтов: перезаписывать/стирать/игнорировать? (Если в новом файле есть записи, идентичные тем, что в БД)

7.Какого размера могут быть .csv? При больших размерах имеет смысл либо обрабатывать кусками, либо построчно читать-парсить-записывать. (В текущей реализации - полная выгрузка файла в память. Так чище получаются функции и оптимальнее разовая запись в бд. Но такая реализация не подойдёт для больших (например, миллионы строк) файлов).
</details>


### Путь директории и период сканирования директории в application.properties
```
directory=C:/IdeaProjects/csv
directory.scan.period=10000
```

### Логи обработки файла
```
2020-11-22 20:08:28.608  INFO 10428 --- [lTaskScheduler1] r.r.market.service.file.FileServiceImpl  : checkDirectoryForNewCsv(): directory=C:/IdeaProjects/csv is STARTED
2020-11-22 20:08:28.611  INFO 10428 --- [lTaskScheduler1] r.r.market.service.file.FileServiceImpl  : checkDirectoryForNewCsv(): files queue=[C:\IdeaProjects\csv\prices — копия.csv]
2020-11-22 20:08:28.611  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : ImportServiceImpl.importProductsAndPricesFromCsv(): file=prices — копия.csv is STARTED
2020-11-22 20:08:28.611  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : ImportServiceImpl.parseCsvToRows() with path=C:\IdeaProjects\csv\prices — копия.csv is STARTED
2020-11-22 20:08:28.613  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : ImportServiceImpl.parseCsvToRows(): rows count=8
2020-11-22 20:08:28.613  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : Parse prices.csv file is STARTED...
2020-11-22 20:08:28.614  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : Processing completed. Processed 8 lines in prices.csv
2020-11-22 20:08:28.633  INFO 10428 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-11-22 20:08:28.635  INFO 10428 --- [  restartedMain] ru.roafo.market.Application              : Started Application in 4.243 seconds (JVM running for 4.965)
2020-11-22 20:08:28.677  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : Created 3 products
2020-11-22 20:08:28.686  INFO 10428 --- [lTaskScheduler1] r.r.m.service.file.ImportServiceImpl     : Created 8 prices
```
### Логи обработки запроса в разных потоках
```
2020-11-22 20:11:44.258  INFO 9352 --- [nio-8080-exec-1] r.r.market.controller.ProductController  : ProductController.getStatistic() is STARTED
2020-11-22 20:11:44.265  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : PriceHistoryServiceImpl.getStatistic() is STARTED
2020-11-22 20:11:44.266  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for find qty of product is STARTED
2020-11-22 20:11:44.266  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for get statistic by date is STARTED
2020-11-22 20:11:44.267  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : PriceHistoryServiceImpl.getStatisticByDate() is STARTED
2020-11-22 20:11:44.267  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for get statistic by product is STARTED
2020-11-22 20:11:44.267  INFO 9352 --- [      Thread-24] r.r.m.s.p.PriceHistoryServiceImpl        : PriceHistoryServiceImpl.getStatisticByProduct() is STARTED
2020-11-22 20:11:44.305  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByDateDTO: "date": 2020-10-15, "frequency": 1
2020-11-22 20:11:44.305  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByDateDTO: "date": 2020-10-20, "frequency": 2
2020-11-22 20:11:44.306  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByDateDTO: "date": 2020-11-21, "frequency": 1
2020-11-22 20:11:44.306  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByDateDTO: "date": 2020-11-22, "frequency": 1
2020-11-22 20:11:44.306  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByDateDTO: "date": 2020-10-01, "frequency": 3
2020-11-22 20:11:44.306  INFO 9352 --- [      Thread-23] r.r.m.s.p.PriceHistoryServiceImpl        : Statistic report by date with 5 rows is CREATED
2020-11-22 20:11:44.328  INFO 9352 --- [      Thread-24] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByProductDTO: "name": Lack, "frequency": 2
2020-11-22 20:11:44.328  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for find qty of product is FINISHED
2020-11-22 20:11:44.328  INFO 9352 --- [      Thread-24] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByProductDTO: "name": Billy, "frequency": 3
2020-11-22 20:11:44.328  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for get statistic by date is FINISHED
2020-11-22 20:11:44.328  INFO 9352 --- [      Thread-24] r.r.m.s.p.PriceHistoryServiceImpl        : Created priceHistoryByProductDTO: "name": Kaustby, "frequency": 3
2020-11-22 20:11:44.328  INFO 9352 --- [      Thread-24] r.r.m.s.p.PriceHistoryServiceImpl        : Statistic report by product with: 3 rows is CREATED
2020-11-22 20:11:44.328  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : Thread for get statistic by product is FINISHED
2020-11-22 20:11:44.328  INFO 9352 --- [nio-8080-exec-1] r.r.m.s.p.PriceHistoryServiceImpl        : PriceHistoryServiceImpl.getStatistic(): statisticDTO {productQty=3, statisticByProduct=["name": Lack, "frequency": 2, "name": Billy, "frequency": 3, "name": Kaustby, "frequency": 3], statisticByDate=["date": 2020-10-15, "frequency": 1, "date": 2020-10-20, "frequency": 2, "date": 2020-11-21, "frequency": 1, "date": 2020-11-22, "frequency": 1, "date": 2020-10-01, "frequency": 3]} is CREATED
2020-11-22 20:11:44.329  INFO 9352 --- [nio-8080-exec-1] r.r.market.controller.ProductController  : ProductController.statisticDTO= {productQty=3, statisticByProduct=["name": Lack, "frequency": 2, "name": Billy, "frequency": 3, "name": Kaustby, "frequency": 3], statisticByDate=["date": 2020-10-15, "frequency": 1, "date": 2020-10-20, "frequency": 2, "date": 2020-11-21, "frequency": 1, "date": 2020-11-22, "frequency": 1, "date": 2020-10-01, "frequency": 3]}

```
### 1 Request 'GET /products?date=2020-10-20 ' - получение всех товаров с ценами на указанную дату

```
{
[
{
name: "Billy",
price: 3700,
},
{
name: "Kaustby",
price: 2799,
},
]
}
```
### 2 Response 'GET /products/statistic' - получение статистики
```
{
productQty: 3,
statisticByDate: [
{
date: "2020-10-15",
frequency: 1,
},
{
date: "2020-10-20",
frequency: 2,
},
{
date: "2020-11-21",
frequency: 1,
},
{
date: "2020-11-22",
frequency: 1,
},
{
date: "2020-10-01",
frequency: 3,
},
],
statisticByProduct: [
{
name: "Lack",
frequency: 2,
},
{
name: "Billy",
frequency: 3,
},
{
name: "Kaustby",
frequency: 3,
},
],
}
```
----


