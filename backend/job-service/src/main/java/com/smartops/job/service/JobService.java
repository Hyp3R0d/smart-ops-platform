package com.smartops.job.service;

import java.util.Map;

public interface JobService {
    Map<String, Object> runLowStockScan();
}
