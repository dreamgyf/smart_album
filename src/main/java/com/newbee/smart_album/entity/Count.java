package com.newbee.smart_album.entity;

import java.io.Serializable;

public class Count implements Serializable {

    private int successCount = 0;

    private int failedCount = 0;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
}
