package com.epam.dto;

import java.io.Serializable;

public class Employee implements Serializable {
    private String name;
    private String company;
    private String position;
    private Integer experience;

    public Employee(String name, String company, String position, Integer experience) {
        this.name = name;
        this.company = company;
        this.position = position;
        this.experience = experience;
    }

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", experience=" + experience +
                '}';
    }
}
