package com.example.meteo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;


@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "code"}))
public class Country
{
    @Id @GeneratedValue @Column
    @JsonIgnore
    @Getter @Setter
    private int idCountry;
    @Getter @Setter
    @Column
    private String name;
    @Getter @Setter
    @Column
    private String code;

    public Country() {}

    public Country(String name, String code)
    {
        super();
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "Country [idCountry=" + idCountry + ", name=" + name + ", code=" + code + "]";
    }
}