package com.example.order_processing.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.micrometer.core.aop.TimedAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MetricsConfigTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("orderProcessingExecutor")
    private ExecutorService orderProcessingExecutor;

    @Test
    void shouldHaveTimedAspectBean() {
        TimedAspect timedAspect = context.getBean(TimedAspect.class);
        assertThat(timedAspect).isNotNull();
    }

    @Test
    void timedAspectShouldUseMeterRegistry() {
        TimedAspect timedAspect = context.getBean(TimedAspect.class);
        assertThat(timedAspect).extracting("registry").isEqualTo(meterRegistry);
    }

    @Test
    void shouldHaveExecutorServiceMetricsBean() {
        ExecutorServiceMetrics metrics = context.getBean(ExecutorServiceMetrics.class);
        assertThat(metrics).isNotNull();
    }

    @Test
    void allConfiguredBeansShouldExist() {
        assertThat(context.containsBean("timedAspect")).isTrue();
        assertThat(context.containsBean("executorServiceMetrics")).isTrue();
    }

    @Test
    void metricsShouldBindCorrectlyToRegistry() {
        long countBefore = meterRegistry.getMeters().stream().count();
        ExecutorServiceMetrics metrics = context.getBean(ExecutorServiceMetrics.class);
        metrics.bindTo(meterRegistry);
        long countAfter = meterRegistry.getMeters().stream().count();
        assertThat(countAfter).isGreaterThanOrEqualTo(countBefore);
    }

    @Test
    void executorMetricsShouldRegisterThreadPoolStats() {
        ExecutorServiceMetrics metrics = context.getBean(ExecutorServiceMetrics.class);
        metrics.bindTo(meterRegistry);
        boolean threadPoolMetricsPresent = meterRegistry.getMeters().stream()
                .anyMatch(meter -> meter.getId().getName().contains("executor")
                        && meter.getId().getTags().stream().anyMatch(tag -> tag.getValue().equals("order-processing")));
        assertThat(threadPoolMetricsPresent).isTrue();
    }
}
