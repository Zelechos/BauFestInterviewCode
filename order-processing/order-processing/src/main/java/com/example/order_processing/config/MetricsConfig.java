package com.example.order_processing.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.Arrays;

@Configuration
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public ExecutorServiceMetrics executorServiceMetrics(
            @Qualifier("orderProcessingExecutor") ExecutorService executorService,
            MeterRegistry registry) {

        // Crear tags adecuados para las métricas
        Iterable<Tag> tags = Arrays.asList(
                Tag.of("executor", "order-processing"),
                Tag.of("type", "thread-pool")
        );

        return new ExecutorServiceMetrics(
                executorService,
                "order.processing.pool",
                tags
        );
    }
}
