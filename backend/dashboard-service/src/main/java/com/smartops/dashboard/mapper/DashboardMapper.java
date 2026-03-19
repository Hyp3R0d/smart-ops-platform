package com.smartops.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {

    @Select("select count(1) from pur_order where deleted=0")
    Long purchaseTotal();

    @Select("select count(1) from pur_order where deleted=0 and status='PENDING_APPROVAL'")
    Long pendingApprovalTotal();

    @Select("select count(1) from inv_stock where deleted=0 and quantity <= threshold")
    Long lowStockTotal();

    @Select("""
            select date(create_time) as d, sum(case when flow_type like 'IN%' then quantity else 0 end) as qty
            from inv_stock_flow
            where deleted=0 and create_time >= date_sub(curdate(), interval 6 day)
            group by date(create_time)
            order by d
            """)
    List<Map<String, Object>> inboundTrendWeek();

    @Select("""
            select status as k, count(1) as v
            from pur_order where deleted=0
            group by status
            """)
    List<Map<String, Object>> purchaseStatusDistribution();

    @Select("""
            select date(create_time) as d, count(1) as qty
            from inv_alert
            where deleted=0 and create_time >= date_sub(curdate(), interval 6 day)
            group by date(create_time)
            order by d
            """)
    List<Map<String, Object>> stockAlertTrendWeek();

    @Select("""
            select i.material_name as name, sum(abs(f.quantity)) as qty
            from inv_stock_flow f
            join inv_material i on f.material_id = i.id and i.deleted=0
            where f.deleted=0
            group by i.material_name
            order by qty desc
            limit 10
            """)
    List<Map<String, Object>> hotMaterials();

    @Select("""
            select o.dept_id as deptId, count(1) as total
            from pur_order o
            where o.deleted=0
            group by o.dept_id
            order by total desc
            limit 10
            """)
    List<Map<String, Object>> deptPurchaseRanking();
}
