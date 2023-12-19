package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "moon", schema = "planets")
public class Moon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moonId", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "size")
    private Double size;

    @Column(name = "planetId")
    private Integer planetId;

    public Integer getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Integer planetId) {
        this.planetId = planetId;
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

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name;
    }
}