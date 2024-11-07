package edu.jl.backend.infrastructure.repository;

import edu.jl.backend.infrastructure.model.ChampionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ChampionRepository extends JpaRepository<ChampionModel, Long> { }
