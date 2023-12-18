package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SolarSystem", schema = "planets")
public class SolarSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SolarSystemId", nullable = false)
    private Integer id;

    @Column(name = "SolarSystemName", nullable = false)
    private String solarSystemName;

    @Column(name = "GalaxyName")
    private String galaxyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolarSystemName() {
        return solarSystemName;
    }

    public void setSolarSystemName(String solarSystemName) {
        this.solarSystemName = solarSystemName;
    }

    public String getGalaxyName() {
        return galaxyName;
    }

    public void setGalaxyName(String galaxyName) {
        this.galaxyName = galaxyName;
    }

    @Override
    public String toString() {
        return solarSystemName;
    }
}