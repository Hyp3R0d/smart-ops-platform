package com.smartops.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartops.inventory.entity.InvStock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvStockMapper extends BaseMapper<InvStock> {}
