package com.gminds.employee_service.model;

import jakarta.persistence.*;

@Entity
public class Clause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clauseType;
    private String clauseTitle;
    private String clauseDescription;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClauseType() {
        return clauseType;
    }

    public void setClauseType(String clauseType) {
        this.clauseType = clauseType;
    }

    public String getClauseTitle() {
        return clauseTitle;
    }

    public void setClauseTitle(String clauseTitle) {
        this.clauseTitle = clauseTitle;
    }

    public String getClauseDescription() {
        return clauseDescription;
    }

    public void setClauseDescription(String clauseDescription) {
        this.clauseDescription = clauseDescription;
    }

}
