package com.gminds.graphql_api_gateway.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmployeePage {

    @JsonProperty("content")
    private List<EmployeeDTO> content;

    @JsonProperty("number")
    private int number;

    @JsonProperty("size")
    private int size;

    @JsonProperty("totalElements")
    private long totalElements;

    @JsonProperty("totalPages")
    private int totalPages;


    public List<EmployeeDTO> getContent() {
        return content;
    }

    public void setContent(List<EmployeeDTO> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
