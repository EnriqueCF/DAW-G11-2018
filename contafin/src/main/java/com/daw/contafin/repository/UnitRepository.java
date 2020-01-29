package com.daw.contafin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.contafin.model.Unit;


public interface UnitRepository extends JpaRepository <Unit, Long>{
	Unit findById(long Id);
	Page<Unit> findAll(Pageable page);
}
