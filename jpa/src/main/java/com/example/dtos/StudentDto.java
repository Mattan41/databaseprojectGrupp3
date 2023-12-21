package com.example.dtos;

import com.example.entities.Student;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */


public record StudentDto(String studentName, Integer studentAge) implements Serializable {
    @Override
    public String toString() {
        return studentName + " " + studentAge;
    }
}