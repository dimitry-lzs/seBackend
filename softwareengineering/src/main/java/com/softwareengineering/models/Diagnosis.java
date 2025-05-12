package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("diagnoses")
public class Diagnosis extends Model {
    private int id;
    private String name; //decease name
    private String details;

    public Diagnosis(int id, String name, String details) {
        this.set("id", id);
        this.set("name", name);
        this.set("details", details);
    }

    public Diagnosis() {
    }

    public int getDiagnosisId() {
        return id;
    }

    public void setId(int id) {
        this.set("id", id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.set("name", name);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.set("details", details);
    }
}
