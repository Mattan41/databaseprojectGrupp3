package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "planet", schema = "planets")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planetId", nullable = false)
    private Integer id;

    @Column(name = "planetName")
    private String planetName;

    @Column(name = "planetSize")
    private Integer planetSize;

    @Column(name = "planetType")
    private String planetType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public Integer getPlanetSize() {
        return planetSize;
    }

    public void setPlanetSize(Integer planetSize) {
        this.planetSize = planetSize;
    }

    public String getPlanetType() {
        return planetType;
    }

    public void setPlanetType(String planetType) {
        this.planetType = planetType;
    }

    @Override
    public String toString() {
        return planetName;
    }
}