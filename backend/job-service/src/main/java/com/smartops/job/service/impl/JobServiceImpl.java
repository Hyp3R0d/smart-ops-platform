package com.smartops.job.service.impl;

import com.smartops.common.constant.CommonConstants;
import com.smartops.common.dto.StockAlertMessage;
import com.smartops.job.mapper.JobMapper;
import com.smartops.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobMapper jobMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${smart.mail.mock:true}")
    private boolean mailMock;

    @Value("${smart.mail.to:ops@example.com}")
    private String mailTo;

    public JobServiceImpl(JobMapper jobMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.jobMapper = jobMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Map<String, Object> runLowStockScan() {
        List<Map<String, Object>> rows = jobMapper.lowStockRows();
        int sent = 0;
        for (Map<String, Object> row : rows) {
            StockAlertMessage msg = new StockAlertMessage();
            msg.setWarehouseId(asLong(row.get("warehouseId")));
            msg.setMaterialId(asLong(row.get("materialId")));
            msg.setMaterialName(String.valueOf(row.getOrDefault("materialName", "N/A")));
            msg.setCurrentStock(asInt(row.get("quantity")));
            msg.setThreshold(asInt(row.get("threshold")));
            msg.setMessage("Low stock detected by scheduled scan");
            msg.setAlertTime(LocalDateTime.now());
            kafkaTemplate.send(CommonConstants.TOPIC_STOCK_ALERT, String.valueOf(msg.getMaterialId()), msg);
            sent++;
        }
        mockSendMail(rows.size());
        Map<String, Object> result = new HashMap<>();
        result.put("scanCount", rows.size());
        result.put("kafkaSent", sent);
        result.put("mailMode", mailMock ? "mock" : "real");
        return result;
    }

    @Scheduled(cron = "${smart.job.low-stock-cron:0 */5 * * * ?}")
    public void scheduleLowStockScan() {
        Map<String, Object> res = runLowStockScan();
        log.info("Low-stock scan finished: {}", res);
    }

    private void mockSendMail(int lowStockCount) {
        if (mailMock) {
            log.info("[MAIL-MOCK] to={} title=SmartOps库存预警 body=当前低库存条目: {}", mailTo, lowStockCount);
        } else {
            log.info("[MAIL] to={} lowStockCount={} (SMTP extension point)", mailTo, lowStockCount);
        }
    }

    private Long asLong(Object value) {
        if (value == null) return 0L;
        return Long.parseLong(String.valueOf(value));
    }

    private Integer asInt(Object value) {
        if (value == null) return 0;
        return Integer.parseInt(String.valueOf(value));
    }
}
