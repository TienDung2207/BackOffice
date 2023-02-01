package com.example.sideproject1.repositories;

import com.example.sideproject1.entities.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Integer> {
}
