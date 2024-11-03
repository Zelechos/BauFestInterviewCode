package com.eldarinterview;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * The com.eldarinterview.NationalBankFraudDetection class is responsible for detecting potential fraudulent activities
 * based on daily spending of customers. It provides a method to count the number of notifications
 * sent to customers when their spending exceeds a certain threshold relative to their historical
 * spending data.
 */
public class NationalBankFraudDetection {

    /**
     * Calculates the number of notifications sent to the customer based on their daily spending.
     *
     * @param expenses An array of daily spending amounts.
     * @param d       The number of previous days to consider for calculating the median.
     * @return The number of notifications sent to the customer.
     */
    public static int countFraudNotifications(int[] expenses, int d) {
        if (d <= 0 || expenses.length <= d) {
            return 0; // No notifications if not enough data
        }

        int notifications = 0;
        PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()); // Max heap for the lower half
        PriorityQueue<Integer> high = new PriorityQueue<>(); // Min heap for the upper half

        for (int i = 0; i < expenses.length; i++) {
            // Only start checking for notifications after we have d days of data
            if (i >= d) {
                double median = getMedian(low, high);

                // Check if the current expense triggers a notification
                if (expenses[i] >= 2 * median) {
                    notifications++;
                }

                // Remove the expense that is no longer considered
                removeExpense(low, high, expenses[i - d]);
            }

            // Add the current expense to the heaps
            addExpense(low, high, expenses[i]);
        }

        return notifications;
    }

    /**
     * Adds an expense to the appropriate heaps to maintain the median calculation.
     *
     * @param low     Max heap containing the lower half of the expenses.
     * @param high    Min heap containing the upper half of the expenses.
     * @param expense The expense amount to be added.
     */
    private static void addExpense(PriorityQueue<Integer> low, PriorityQueue<Integer> high, int expense) {
        if (low.isEmpty() || expense <= low.peek()) {
            low.offer(expense);
        } else {
            high.offer(expense);
        }

        balanceHeaps(low, high);
    }

    /**
     * Removes an expense from the heaps and rebalances them if necessary.
     *
     * @param low     Max heap containing the lower half of the expenses.
     * @param high    Min heap containing the upper half of the expenses.
     * @param expense The expense amount to be removed.
     */
    private static void removeExpense(PriorityQueue<Integer> low, PriorityQueue<Integer> high, int expense) {
        if (low.contains(expense)) {
            low.remove(expense);
        } else {
            high.remove(expense);
        }

        balanceHeaps(low, high);
    }

    /**
     * Balances the two heaps to ensure that the difference in their sizes is at most one.
     * The `low` heap (max-heap) is allowed to have one more element than the `high` heap (min-heap)
     * if the total number of elements is odd. This ensures that the median can be efficiently
     * retrieved from the top of these heaps.
     *
     * @param low  Max heap containing the lower half of the expenses.
     * @param high Min heap containing the upper half of the expenses.
     */
    private static void balanceHeaps(PriorityQueue<Integer> low, PriorityQueue<Integer> high) {
        // If `low` has more than one extra element, move the top of `low` to `high`
        if (low.size() > high.size() + 1) {
            high.offer(low.poll());
        }
        // If `high` has more elements than `low`, move the top of `high` to `low`
        else if (high.size() > low.size()) {
            low.offer(high.poll());
        }
    }

    /**
     * Calculates the median from the current state of the heaps.
     *
     * @param low  Max heap containing the lower half of the expenses.
     * @param high Min heap containing the upper half of the expenses.
     * @return The median value.
     */
    public static double getMedian(PriorityQueue<Integer> low, PriorityQueue<Integer> high) {
        if (low.size() > high.size()) {
            return low.peek();
        } else {
            return (low.peek() + high.peek()) / 2.0;
        }
    }

    /**
     * Main method for testing the functionality of the fraud detection system.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        int[] expenses = {10, 20, 30, 40, 50};
        int d = 3;

        int notifications = countFraudNotifications(expenses, d);
        System.out.println("Number of notifications sent: " + notifications);
    }
}
