package com.fitlife.app.Repository.Exercise;

import com.fitlife.app.Model.Exercise.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment,Long> {
}
