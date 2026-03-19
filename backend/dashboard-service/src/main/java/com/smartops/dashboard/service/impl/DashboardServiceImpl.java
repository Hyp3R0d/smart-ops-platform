package com.smartops.dashboard.service.impl;

import com.smartops.dashboard.mapper.DashboardMapper;
import com.smartops.dashboard.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("purchaseTotal", nvl(dashboardMapper.purchaseTotal()));
        result.put("pendingApprovalTotal", nvl(dashboardMapper.pendingApprovalTotal()));
        result.put("lowStockTotal", nvl(dashboardMapper.lowStockTotal()));
        result.put("inboundTrendWeek", defaultList(dashboardMapper.inboundTrendWeek()));
        result.put("purchaseStatusDistribution", defaultList(dashboardMapper.purchaseStatusDistribution()));
        result.put("stockAlertTrendWeek", defaultList(dashboardMapper.stockAlertTrendWeek()));
        result.put("hotMaterials", defaultList(dashboardMapper.hotMaterials()));
        result.put("deptPurchaseRanking", defaultList(dashboardMapper.deptPurchaseRanking()));
        return result;
    }

    private Long nvl(Long val) {
        return val == null ? 0L : val;
    }

    private List<Map<String, Object>> defaultList(List<Map<String, Object>> list) {
        return list == null ? List.of() : list;
    }
}
