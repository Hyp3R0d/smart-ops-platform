package com.smartops.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobMapper {

    @Select("""
            select s.id, s.warehouse_id as warehouseId, s.material_id as materialId,
                   s.quantity, s.threshold, m.material_name as materialName
            from inv_stock s
            left join inv_material m on s.material_id = m.id and m.deleted=0
            where s.deleted=0 and s.quantity <= s.threshold
            order by s.id desc
            limit 200
            """)
    List<Map<String, Object>> lowStockRows();
}
