package com.nncompany.api.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "briefing")
public class Briefing {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "interval_in_months")
    private Integer intervalInMonths;

    public Briefing(){}

    public Briefing(String name, Integer intervalInMonths){
        this.name = name;
        this.intervalInMonths = intervalInMonths;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIntervalInMonths() {
        return intervalInMonths;
    }

    public void setIntervalInMonths(Integer intervalInMonths) {
        this.intervalInMonths = intervalInMonths;
    }
}
