package com.FitnessApp.Service.ExcerciseService;

import com.FitnessApp.DTO.Request.FetchExerciseRequest;
import com.FitnessApp.Service.Generic.GenericSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.FitnessApp.Model.Exercise;
import com.FitnessApp.Repository.ExerciseRepository;
import com.FitnessApp.Service.Generic.GenericService;

import java.util.List;

@Service
public class ExerciseService extends GenericSearchService<Exercise> {
	public ExerciseService(ExerciseRepository exerciseRepository) {
		super(exerciseRepository);
	}

	public Page<Exercise> searchExercise(FetchExerciseRequest request) {
		return super.searchAllOf(List.of(
				specification("name",request.name()),
				specification("target",request.target()),
				specification("bodyPart",request.bodyPart())
			)
		);
	}
}
