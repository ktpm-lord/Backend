package com.fitlife.app.Service.DailyWorkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitlife.app.Model.Workout.DailyWorkout;
import com.fitlife.app.Repository.DailyWorkoutRepository;
import com.fitlife.app.Service.Generic.GenericService;

@Service
public class DailyServiceImpl extends GenericService<DailyWorkout, Long, DailyWorkoutRepository> implements IDailyService{

	@Autowired
	public DailyServiceImpl(DailyWorkoutRepository genericRepository) {
		super(genericRepository);
	}

}
