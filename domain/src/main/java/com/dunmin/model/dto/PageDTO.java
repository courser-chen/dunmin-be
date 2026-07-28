package com.dunmin.model.dto;

/**
 * 分页查询参数 DTO
 */
public class PageDTO {

    private int page = 1;
    private int pageSize = 10;
    private String keyword;
    private String orderBy;
    private String orderDirection = "desc";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 10 : (pageSize > 100 ? 100 : pageSize);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public boolean isAsc() {
        return "asc".equalsIgnoreCase(orderDirection);
    }
}
