package com.smartops.organization.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smartops.common.entity.BaseEntity;

@TableName("org_post")
public class OrgPost extends BaseEntity {
    private String postName;
    private String postCode;
    private Integer status;

    public String getPostName() { return postName; }
    public void setPostName(String postName) { this.postName = postName; }
    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
