package com.pragma.pagos.utils;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class PerformanceHelper {
    private final AtomicInteger processedCount = new AtomicInteger(0);
    private final AtomicInteger failedCount = new AtomicInteger(0);
    private final List<Long> executionTimes = Collections.synchronizedList(new ArrayList<>());
    private final Instant startTime;
    private volatile Instant endTime;
    private final int targetThroughputPerHour;
    private final Map<String, AtomicInteger> statusBreakdown = new ConcurrentHashMap<>();

    public PerformanceHelper(int targetThroughputPerHour) {
        this.targetThroughputPerHour = targetThroughputPerHour;
        this.startTime = Instant.now();
    }

    public PerformanceHelper() {
        this(5000);
    }

    public void recordProcessing(long executionTimeMs, String status) {
        executionTimes.add(executionTimeMs);
        processedCount.incrementAndGet();
        statusBreakdown.computeIfAbsent(status, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void recordFailure(String errorType) {
        failedCount.incrementAndGet();
        statusBreakdown.computeIfAbsent("FAILED_" + errorType, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void finish() {
        this.endTime = Instant.now();
    }

    public double calculateThroughputPerHour() {
        if (endTime == null) {
            endTime = Instant.now();
        }
        long durationSeconds = Duration.between(startTime, endTime).getSeconds();
        if (durationSeconds == 0) {
            return 0.0;
        }
        return (processedCount.get() * 3600.0) / durationSeconds;
    }

    public double calculateAverageExecutionTimeMs() {
        if (executionTimes.isEmpty()) {
            return 0.0;
        }
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    public long calculatePercentile(long percentile) {
        if (executionTimes.isEmpty()) {
            return 0;
        }
        List<Long> sorted = new ArrayList<>(executionTimes);
        Collections.sort(sorted);
        int index = (int) Math.ceil((percentile / 100.0) * sorted.size()) - 1;
        return sorted.get(Math.max(0, index));
    }

    public boolean meetsThroughputThreshold() {
        double currentThroughput = calculateThroughputPerHour();
        return currentThroughput >= targetThroughputPerHour;
    }

    public double getThroughputPercentage() {
        double current = calculateThroughputPerHour();
        return (current / targetThroughputPerHour) * 100.0;
    }

    public int getTotalProcessed() {
        return processedCount.get();
    }

    public int getTotalFailed() {
        return failedCount.get();
    }

    public long getDurationSeconds() {
        Instant referenceEnd = (endTime != null) ? endTime : Instant.now();
        return Duration.between(startTime, referenceEnd).getSeconds();
    }

    public Map<String, Integer> getStatusBreakdown() {
        Map<String, Integer> result = new HashMap<>();
        statusBreakdown.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    public long getP50() {
        return calculatePercentile(50);
    }

    public long getP95() {
        return calculatePercentile(95);
    }

    public long getP99() {
        return calculatePercentile(99);
    }

    public long getMaxExecutionTime() {
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
    }

    public long getMinExecutionTime() {
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0);
    }

    public double getSuccessRate() {
        int total = processedCount.get() + failedCount.get();
        if (total == 0) {
            return 0.0;
        }
        return (processedCount.get() * 100.0) / total;
    }

    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE DE RENDIMIENTO ===\n");
        report.append("Umbral objetivo: ").append(targetThroughputPerHour).append(" pagos/hora\n");
        report.append("Pagos procesados: ").append(getTotalProcessed()).append("\n");
        report.append("Pagos fallidos: ").append(getTotalFailed()).append("\n");
        report.append("Duracion: ").append(getDurationSeconds()).append(" segundos\n");
        report.append("Throughput actual: ").append(String.format("%.2f", calculateThroughputPerHour()))
              .append(" pagos/hora\n");
        report.append("Porcentaje del umbral: ").append(String.format("%.2f%%", getThroughputPercentage())).append("\n");
        report.append("Cumple umbral: ").append(meetsThroughputThreshold() ? "SI" : "NO").append("\n");
        report.append("Tasa de exito: ").append(String.format("%.2f%%", getSuccessRate())).append("\n");
        report.append("Tiempo promedio: ").append(String.format("%.2f", calculateAverageExecutionTimeMs())).append(" ms\n");
        report.append("Tiempo minimo: ").append(getMinExecutionTime()).append(" ms\n");
        report.append("Tiempo maximo: ").append(getMaxExecutionTime()).append(" ms\n");
        report.append("P50: ").append(getP50()).append(" ms\n");
        report.append("P95: ").append(getP95()).append(" ms\n");
        report.append("P99: ").append(getP99()).append(" ms\n");
        report.append("=== Desglose por estado ===\n");
        getStatusBreakdown().forEach((status, count) -> 
            report.append(status).append(": ").append(count).append("\n")
        );
        return report.toString();
    }

    public PerformanceMetrics getMetrics() {
        return new PerformanceMetrics(
            getTotalProcessed(),
            getTotalFailed(),
            getDurationSeconds(),
            calculateThroughputPerHour(),
            calculateAverageExecutionTimeMs(),
            getP50(),
            getP95(),
            getP99(),
            getSuccessRate(),
            meetsThroughputThreshold()
        );
    }

    public record PerformanceMetrics(
        int totalProcessed,
        int totalFailed,
        long durationSeconds,
        double throughputPerHour,
        double averageExecutionTimeMs,
        long p50,
        long p95,
        long p99,
        double successRate,
        boolean meetsThreshold
    ) {}

    public static class ThroughputValidator {
        private final int expectedPayments;
        private final Duration maxDuration;

        public ThroughputValidator(int expectedPayments, Duration maxDuration) {
            this.expectedPayments = expectedPayments;
            this.maxDuration = maxDuration;
        }

        public boolean validate(PerformanceHelper helper) {
            if (helper.getTotalProcessed() < expectedPayments) {
                return false;
            }
            Duration actualDuration = Duration.between(helper.startTime, 
                helper.endTime != null ? helper.endTime : Instant.now());
            return actualDuration.compareTo(maxDuration) <= 0;
        }

        public String getValidationMessage(PerformanceHelper helper) {
            Duration actualDuration = Duration.between(helper.startTime, 
                helper.endTime != null ? helper.endTime : Instant.now());
            return String.format(
                "Validacion: %d pagos en %d segundos (maximo: %d segundos). %s",
                helper.getTotalProcessed(),
                actualDuration.getSeconds(),
                maxDuration.getSeconds(),
                validate(helper) ? "CUMPLE" : "NO CUMPLE"
            );
        }
    }
}