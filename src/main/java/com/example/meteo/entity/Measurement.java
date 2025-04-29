package com.example.meteo.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"city", "date"}))
public class Measurement
{
    @Id @GeneratedValue
    @Getter @Setter
    @Column
    @JsonIgnore
    private int idMeasurement;
    @Getter @Setter
    @ManyToOne @JoinColumn(name="idCity") @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;
    @Getter @Setter
    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;
    @Getter @Setter
    @Column
    private double temperature;
    @Getter @Setter
    @Column
    private double pressure;
    @Getter @Setter
    @Column
    private double humidity;
    @Getter @Setter
    @Column
    private double wind;
    @Getter @Setter
    @Column
    private double rain;
    @Getter @Setter
    @Column
    private Instant timestamp;



    public Measurement() {}

    public Measurement(City city, Date date, double temperature, double pressure, double humidity, double wind, double rain)
    {
        super();
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.rain = rain;
    }


    @Override
    public String toString()
    {
        return "Measurement [idMeasurement=" + idMeasurement + ", city=" + city + ", date=" + date + ", temperature="
                + temperature + ", pressure=" + pressure + ", humidity=" + humidity + ", wind=" + wind + ", rain="
                + rain + "]";
    }
}