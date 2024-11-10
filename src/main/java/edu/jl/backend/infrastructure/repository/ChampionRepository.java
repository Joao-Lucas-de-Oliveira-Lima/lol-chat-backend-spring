package edu.jl.backend.infrastructure.repository;

import edu.jl.backend.infrastructure.model.ChampionModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChampionRepository extends JpaRepository<ChampionModel, Long> { }
