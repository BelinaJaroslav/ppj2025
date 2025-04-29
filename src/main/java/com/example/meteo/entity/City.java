package com.example.meteo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Getter @Setter
@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"name", "country"}))
public class City
{

    @Id @GeneratedValue @Column
    @JsonIgnore
    private int idCity;
    @Column
    private String name;
    @ManyToOne @JoinColumn(name="idCountry") @OnDelete(action = OnDeleteAction.CASCADE)
    private Country country;
    @Column
    private double latitude;
    @Column
    private double longitude;

    public City() {
        log.info("Creating new City Empty");
    }

    public static Logger log = LoggerFactory.getLogger(City.class);

    public City(String name, Country country, double latitude, double longitude)
    {

        super();
        log.info("Creating new City");
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "City [idCity=" + idCity + ", name=" + name + ", country=" + country + ", latitude=" + latitude
                + ", longitude=" + longitude + "]";
    }
}