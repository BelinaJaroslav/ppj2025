package com.example.meteo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "code"}))
public class Country
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_country")
    @JsonIgnore
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