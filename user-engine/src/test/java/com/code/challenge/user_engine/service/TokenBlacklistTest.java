package com.code.challenge.user_engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenBlacklistTest {

    private TokenBlacklist tokenBlacklist;

    @BeforeEach
    void setUp() {
        tokenBlacklist = new TokenBlacklist();
    }

    @Test
    void shouldReturnFalseForTokenNotBlacklisted() {
        assertThat(tokenBlacklist.isBlacklisted("token123")).isFalse();
    }

    @Test
    void shouldReturnTrueAfterTokenIsBlacklisted() {
        tokenBlacklist.blacklistToken("token123");
        assertThat(tokenBlacklist.isBlacklisted("token123")).isTrue();
    }

    @Test
    void shouldBlacklistMultipleTokens() {
        tokenBlacklist.blacklistToken("token1");
        tokenBlacklist.blacklistToken("token2");

        assertThat(tokenBlacklist.isBlacklisted("token1")).isTrue();
        assertThat(tokenBlacklist.isBlacklisted("token2")).isTrue();
    }

    @Test
    void shouldNotAffectOtherTokensWhenOneIsBlacklisted() {
        tokenBlacklist.blacklistToken("token1");
        assertThat(tokenBlacklist.isBlacklisted("token2")).isFalse();
    }

    @Test
    void shouldHandleEmptyStringToken() {
        tokenBlacklist.blacklistToken("");
        assertThat(tokenBlacklist.isBlacklisted("")).isTrue();
    }

    @Test
    void shouldAllowBlacklistingSameTokenMultipleTimes() {
        tokenBlacklist.blacklistToken("tokenX");
        tokenBlacklist.blacklistToken("tokenX");

        assertThat(tokenBlacklist.isBlacklisted("tokenX")).isTrue();
    }

    @Test
    void shouldRemainBlacklistedAfterMultipleChecks() {
        tokenBlacklist.blacklistToken("tokenY");

        assertThat(tokenBlacklist.isBlacklisted("tokenY")).isTrue();
        assertThat(tokenBlacklist.isBlacklisted("tokenY")).isTrue(); // again
    }

    @Test
    void shouldHandleConcurrencySafely() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String token = "tokenConcurrent";

        Callable<Void> task = () -> {
            tokenBlacklist.blacklistToken(token);
            return null;
        };

        Future<?>[] futures = new Future[100];
        for (int i = 0; i < 100; i++) {
            futures[i] = executor.submit(task);
        }
        for (Future<?> f : futures) {
            f.get();
        }

        executor.shutdown();

        assertThat(tokenBlacklist.isBlacklisted(token)).isTrue();
    }

    @Test
    void shouldHandleCheckingConcurrency() throws InterruptedException, ExecutionException {
        tokenBlacklist.blacklistToken("parallelToken");

        Callable<Boolean> task = () -> tokenBlacklist.isBlacklisted("parallelToken");

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future<Boolean>[] futures = new Future[50];
        for (int i = 0; i < 50; i++) {
            futures[i] = executor.submit(task);
        }

        for (Future<Boolean> f : futures) {
            assertThat(f.get()).isTrue();
        }

        executor.shutdown();
    }

    @Test
    void shouldNotThrowOnConcurrentAddAndCheck() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable add = () -> {
            for (int i = 0; i < 1000; i++) {
                tokenBlacklist.blacklistToken("t" + i);
            }
        };

        Runnable check = () -> {
            for (int i = 0; i < 1000; i++) {
                tokenBlacklist.isBlacklisted("t" + i);
            }
        };

        executor.submit(add);
        executor.submit(check);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void shouldBlacklistUnicodeToken() {
        String token = "tokén☕️";
        tokenBlacklist.blacklistToken(token);
        assertThat(tokenBlacklist.isBlacklisted(token)).isTrue();
    }

    @Test
    void shouldHandleVeryLongToken() {
        String longToken = "a".repeat(10_000);
        tokenBlacklist.blacklistToken(longToken);
        assertThat(tokenBlacklist.isBlacklisted(longToken)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenNeverBlacklisted() {
        assertThat(tokenBlacklist.isBlacklisted("neverAdded")).isFalse();
    }
}
