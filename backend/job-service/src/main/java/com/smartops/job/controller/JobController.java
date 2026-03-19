package com.smartops.job.controller;

import com.smartops.common.api.ApiResponse;
import com.smartops.job.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/public/health")
    public ApiResponse<?> health() {
        return ApiResponse.success("ok");
    }

    @GetMapping("/run-low-stock-scan")
    public ApiResponse<?> runLowStockScan() {
        return ApiResponse.success(jobService.runLowStockScan());
    }
}
