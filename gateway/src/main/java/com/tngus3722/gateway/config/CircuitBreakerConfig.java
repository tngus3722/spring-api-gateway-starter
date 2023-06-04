package com.tngus3722.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CircuitBreakerConfig {

    @Bean
    public RegistryEventConsumer<CircuitBreaker> displayCircuitBreakerRegistryEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(@NotNull EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher().onStateTransition(event -> {
                    loggingTransition(entryAddedEvent.getAddedEntry().getMetrics(), event.getStateTransition());
                });
            }

            @Override
            public void onEntryRemovedEvent(@NotNull EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                entryRemoveEvent.getRemovedEntry().getEventPublisher().onStateTransition(event -> {
                    loggingTransition(entryRemoveEvent.getRemovedEntry().getMetrics(), event.getStateTransition());
                });
            }

            @Override
            public void onEntryReplacedEvent(@NotNull EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                entryReplacedEvent.getNewEntry().getEventPublisher().onStateTransition(event -> {
                    loggingTransition(entryReplacedEvent.getNewEntry().getMetrics(), event.getStateTransition());
                });
            }
        };
    }

    private void loggingTransition(CircuitBreaker.Metrics metrics, CircuitBreaker.StateTransition stateTransition) {
        log.error("[{}][CircuitBreaker metrics] bufferedCall(rate: {}, total: {}, success: {}, failed: {}), slowCall(rate: {}, total: {}, success: {}, failed: {})",
            stateTransition.name(),
            metrics.getFailureRate(), metrics.getNumberOfBufferedCalls(), metrics.getNumberOfSuccessfulCalls(), metrics.getNumberOfFailedCalls(),
            metrics.getSlowCallRate(), metrics.getNumberOfSlowCalls(), metrics.getNumberOfSuccessfulCalls(), metrics.getNumberOfSlowFailedCalls()
        );
    }
}
