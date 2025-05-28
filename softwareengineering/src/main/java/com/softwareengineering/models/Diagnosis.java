package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("diagnoses")
public class Diagnosis extends Model {
    public Diagnosis(int id, String name, String details) {
        this.set("id", id);
        this.set("name", name);
        this.set("details", details);
    }

    public Diagnosis() {
    }

    public int getDiagnosisId() {
        return (int) this.get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getDetails() {
        return (String) get("details");
    }

    public void setDetails(String details) {
        set("details", details);
    }
}
