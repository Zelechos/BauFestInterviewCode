package com.eldarinterview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NationalBankFraudDetectionTest {

    /**
     * Test for the case where the number of days (`d`) is 0.
     * Expects no notifications because there is no data to analyze.
     */
    @Test
    public void testCountFraudNotifications_DaysZero() {
        int[] expenses = {10, 20, 30, 40, 50};
        int d = 0;
        int expectedNotifications = 0;
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }

    /**
     * Test for the case where the number of days (`d`) is greater than the number of expenses.
     * Expects no notifications because there is insufficient data.
     */
    @Test
    public void testCountFraudNotifications_InsufficientData() {
        int[] expenses = {10, 20, 30};
        int d = 4;
        int expectedNotifications = 0;
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }

    /**
     * Test for the standard case with sufficient data to calculate notifications.
     * Expects 2 notifications as spending exceeds twice the median on specific days.
     */
    @Test
    public void testCountFraudNotifications_MultipleNotifications() {
        int[] expenses = {10, 20, 30, 40, 50, 80, 90};
        int d = 3;
        int expectedNotifications = 2; // Days 4,6 trigger notifications
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }

    /**
     * Test for the case where no notifications should be triggered.
     * Expects 1 notification as spending exceeds twice the median on one day.
     */
    @Test
    public void testCountFraudNotifications_NoNotifications() {
        int[] expenses = {10, 20, 30, 40, 50};
        int d = 3;
        int expectedNotifications = 1;
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }

    /**
     * Test for the case with an even number of expenses in the sliding window.
     * Expects 2 notifications as spending exceeds twice the median on specific days.
     */
    @Test
    public void testCountFraudNotifications_EvenDaysWindow() {
        int[] expenses = {10, 20, 30, 50, 80, 100};
        int d = 4;
        int expectedNotifications = 2;
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }

    /**
     * Test for the case where all expenses are equal.
     * Expects no notifications as spending does not exceed twice the median on any day.
     */
    @Test
    public void testCountFraudNotifications_AllEqualExpenses() {
        int[] expenses = {20, 20, 20, 20, 20, 20};
        int d = 3;
        int expectedNotifications = 0;
        assertEquals(expectedNotifications, NationalBankFraudDetection.countFraudNotifications(expenses, d));
    }
}
