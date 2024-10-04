package edu.jl.backend.infra.repository;

import edu.jl.backend.infra.model.ChampionModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChampionRepository extends JpaRepository<ChampionModel, Long> { }
