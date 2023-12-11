package com.fitlife.app.Repository;

import com.fitlife.app.Model.Workout.DailyWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWorkoutRepository extends JpaRepository<DailyWorkout, Long> {}
