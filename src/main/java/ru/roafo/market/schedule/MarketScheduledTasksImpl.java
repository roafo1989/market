package ru.roafo.market.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.roafo.market.service.file.FileService;

@Service
public class MarketScheduledTasksImpl implements MarketScheduledTasks {

    @Value("${directory.scan.period}")
    private long DIRECTORY_SCAN_PERIOD;
    private final ThreadPoolTaskScheduler scheduler;
    private final FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(MarketScheduledTasksImpl.class);

    public MarketScheduledTasksImpl(ThreadPoolTaskScheduler scheduler, FileService fileService) {
        this.scheduler = scheduler;
        this.fileService = fileService;
    }

    @EventListener
    @Override
    public void onApplicationStartOrRefreshContext(ContextRefreshedEvent event) {
        logger.info("onApplicationStartOrRefresh() refresh source: {}", event.getSource());
        scheduler.scheduleAtFixedRate(() -> fileService.checkDirectoryForNewCsv(), DIRECTORY_SCAN_PERIOD);
    }
}
