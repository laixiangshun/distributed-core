package com.htwz.entity;
public enum RetryTypeEnum {
    /**
     * exponentialBackoffRetry
     */
    EXPONENTIAL_BACKOFF_RETRY("exponentialBackoffRetry"),
    BOUNDED_EXPONENTIAL_BACKOFF_RETRY("boundedExponentialBackoffRetry"),
    RETRY_NTIMES("retryNTimes"),
    RETRY_FOREVER("retryForever"),
    RETRY_UNTIL_ELAPSED("retryUntilElapsed");

    private String value;

    private RetryTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RetryTypeEnum fromString(String value) {
        for (RetryTypeEnum type : RetryTypeEnum.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}