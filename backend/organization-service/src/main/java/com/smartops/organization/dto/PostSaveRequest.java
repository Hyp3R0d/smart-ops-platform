package com.smartops.organization.dto;

import jakarta.validation.constraints.NotBlank;

public class PostSaveRequest {
    private Long id;
    @NotBlank
    private String postName;
    @NotBlank
    private String postCode;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPostName() { return postName; }
    public void setPostName(String postName) { this.postName = postName; }
    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
