package com.smartops.common.constant;

public interface CommonConstants {
    String AUTH_HEADER = "Authorization";
    String BEARER_PREFIX = "Bearer ";
    String LOGIN_UID_HEADER = "X-LOGIN-UID";
    String LOGIN_USER_HEADER = "X-LOGIN-USER";

    String CACHE_DEPT_TREE = "org:dept:tree";
    String CACHE_PERMISSION_PREFIX = "auth:perm:";
    String CACHE_MENU_TREE = "sys:menu:tree";

    String TOPIC_PURCHASE_APPROVED = "purchase-approved-topic";
    String TOPIC_STOCK_ALERT = "stock-alert-topic";

    String PURCHASE_STATUS_DRAFT = "DRAFT";
    String PURCHASE_STATUS_PENDING_APPROVAL = "PENDING_APPROVAL";
    String PURCHASE_STATUS_APPROVED = "APPROVED";
    String PURCHASE_STATUS_REJECTED = "REJECTED";
    String PURCHASE_STATUS_ORDERED = "ORDERED";
    String PURCHASE_STATUS_STOCKED = "STOCKED";
}
