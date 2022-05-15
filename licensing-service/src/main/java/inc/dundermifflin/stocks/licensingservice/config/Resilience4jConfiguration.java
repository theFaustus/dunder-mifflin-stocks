package inc.dundermifflin.stocks.licensingservice.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Resilience4jConfiguration {

    @Bean
    public RegistryEventConsumer<Retry> customRetryRegistryEventConsumer() {

        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }
        };
    }

    @Bean
    public RegistryEventConsumer<CircuitBreaker> customCircuitBreakerRegistryEventConsumer() {

        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }
        };
    }

    @Bean
    public RegistryEventConsumer<Bulkhead> customBulkheadBreakerRegistryEventConsumer() {

        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Bulkhead> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Bulkhead> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Bulkhead> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }
        };
    }
}
