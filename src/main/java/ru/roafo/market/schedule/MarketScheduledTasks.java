package ru.roafo.market.schedule;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface MarketScheduledTasks {

    @EventListener
    void onApplicationStartOrRefreshContext(ContextRefreshedEvent event);
}
