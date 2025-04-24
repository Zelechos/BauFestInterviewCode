package com.example.order_processing.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsyncConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void executorBeanShouldExist() {
        ExecutorService executor = (ExecutorService) context.getBean("orderProcessingExecutor");
        assertThat(executor).isNotNull();
    }

    @Test
    void executorShouldBeThreadPoolExecutor() {
        ExecutorService executor = (ExecutorService) context.getBean("orderProcessingExecutor");
        assertThat(executor).isInstanceOf(ThreadPoolExecutor.class);
    }

    @Test
    void executorCorePoolSizeShouldMatchAvailableProcessors() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        int cores = Runtime.getRuntime().availableProcessors();
        assertThat(executor.getCorePoolSize()).isEqualTo(cores);
    }

    @Test
    void executorMaxPoolSizeShouldBe200() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        assertThat(executor.getMaximumPoolSize()).isEqualTo(200);
    }

    @Test
    void executorQueueCapacityShouldBe1000() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        assertThat(executor.getQueue().remainingCapacity() + executor.getQueue().size()).isEqualTo(1000);
    }

    @Test
    void executorThreadNamePrefixShouldStartWithOrderProc() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        executor.execute(() -> assertThat(Thread.currentThread().getName()).startsWith("OrderProc-"));
    }

    @Test
    void rejectedExecutionHandlerShouldBeCallerRunsPolicy() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        assertThat(executor.getRejectedExecutionHandler()).isInstanceOf(ThreadPoolExecutor.CallerRunsPolicy.class);
    }

    @Test
    void allowCoreThreadTimeoutShouldBeTrue() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        assertThat(executor.allowsCoreThreadTimeOut()).isTrue();
    }

    @Test
    void keepAliveSecondsShouldBe60() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getBean("orderProcessingExecutor");
        assertThat(executor.getKeepAliveTime(java.util.concurrent.TimeUnit.SECONDS)).isEqualTo(60);
    }

}
