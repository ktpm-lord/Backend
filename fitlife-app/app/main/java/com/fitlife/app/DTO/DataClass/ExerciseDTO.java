package com.fitlife.app.DTO.DataClass;

import com.fitlife.app.Model.Exercise.Steps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    private Long id;
    private String name;
    private String description;
    private String equipment;
    private String exerciseCategory;
    private String gifUrl;
    private List<Steps> steps;
    private int sets;
    private int reps;
    private double caloriesPerMinute;
}
