package com.smartops.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartops.common.api.PageResult;
import com.smartops.common.constant.CommonConstants;
import com.smartops.organization.dto.DeptSaveRequest;
import com.smartops.organization.dto.EmployeeSaveRequest;
import com.smartops.organization.dto.PostSaveRequest;
import com.smartops.organization.entity.OrgDepartment;
import com.smartops.organization.entity.OrgEmployee;
import com.smartops.organization.entity.OrgPost;
import com.smartops.organization.mapper.OrgDepartmentMapper;
import com.smartops.organization.mapper.OrgEmployeeMapper;
import com.smartops.organization.mapper.OrgPostMapper;
import com.smartops.organization.service.OrganizationService;
import com.smartops.organization.vo.DeptTreeNode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrgDepartmentMapper deptMapper;
    private final OrgPostMapper postMapper;
    private final OrgEmployeeMapper employeeMapper;
    private final StringRedisTemplate redisTemplate;

    public OrganizationServiceImpl(OrgDepartmentMapper deptMapper, OrgPostMapper postMapper, OrgEmployeeMapper employeeMapper, StringRedisTemplate redisTemplate) {
        this.deptMapper = deptMapper;
        this.postMapper = postMapper;
        this.employeeMapper = employeeMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Map<String, Object> deptTree() {
        List<OrgDepartment> list = deptMapper.selectList(new LambdaQueryWrapper<OrgDepartment>().orderByAsc(OrgDepartment::getSort).orderByAsc(OrgDepartment::getId));
        Map<Long, DeptTreeNode> map = new LinkedHashMap<>();
        for (OrgDepartment d : list) {
            DeptTreeNode node = new DeptTreeNode();
            node.setId(d.getId());
            node.setParentId(d.getParentId());
            node.setDeptName(d.getDeptName());
            node.setDeptCode(d.getDeptCode());
            map.put(node.getId(), node);
        }
        List<DeptTreeNode> roots = new ArrayList<>();
        for (DeptTreeNode node : map.values()) {
            if (node.getParentId() == null || node.getParentId() == 0 || !map.containsKey(node.getParentId())) roots.add(node);
            else map.get(node.getParentId()).getChildren().add(node);
        }
        redisTemplate.opsForValue().set(CommonConstants.CACHE_DEPT_TREE, String.valueOf(System.currentTimeMillis()));
        return Map.of("tree", roots.stream().sorted(Comparator.comparing(DeptTreeNode::getId)).collect(Collectors.toList()));
    }

    @Override
    public void saveDept(DeptSaveRequest req) {
        OrgDepartment d = req.getId() == null ? new OrgDepartment() : deptMapper.selectById(req.getId());
        d.setParentId(req.getParentId() == null ? 0L : req.getParentId());
        d.setDeptName(req.getDeptName());
        d.setDeptCode(req.getDeptCode());
        d.setSort(req.getSort() == null ? 0 : req.getSort());
        d.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) deptMapper.insert(d); else deptMapper.updateById(d);
        redisTemplate.delete(CommonConstants.CACHE_DEPT_TREE);
    }

    @Override
    public void deleteDept(Long id) {
        deptMapper.deleteById(id);
        redisTemplate.delete(CommonConstants.CACHE_DEPT_TREE);
    }

    @Override
    public Map<String, Object> postList() {
        return Map.of("list", postMapper.selectList(new LambdaQueryWrapper<OrgPost>().orderByAsc(OrgPost::getId)));
    }

    @Override
    public void savePost(PostSaveRequest req) {
        OrgPost p = req.getId() == null ? new OrgPost() : postMapper.selectById(req.getId());
        p.setPostName(req.getPostName());
        p.setPostCode(req.getPostCode());
        p.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) postMapper.insert(p); else postMapper.updateById(p);
    }

    @Override
    public void deletePost(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> employeePage(long page, long size, String keyword) {
        LambdaQueryWrapper<OrgEmployee> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) qw.like(OrgEmployee::getEmployeeName, keyword).or().like(OrgEmployee::getEmployeeNo, keyword);
        Page<OrgEmployee> p = employeeMapper.selectPage(new Page<>(page, size), qw.orderByDesc(OrgEmployee::getId));
        return Map.of("page", PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @Override
    public void saveEmployee(EmployeeSaveRequest req) {
        OrgEmployee e = req.getId() == null ? new OrgEmployee() : employeeMapper.selectById(req.getId());
        e.setUserId(req.getUserId());
        e.setDeptId(req.getDeptId());
        e.setPostId(req.getPostId());
        e.setEmployeeNo(req.getEmployeeNo());
        e.setEmployeeName(req.getEmployeeName());
        e.setEmail(req.getEmail());
        e.setPhone(req.getPhone());
        e.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        if (req.getId() == null) employeeMapper.insert(e); else employeeMapper.updateById(e);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeMapper.deleteById(id);
    }
}
