package edu.jl.backend.presentation.controller;

import edu.jl.backend.application.usercase.ListChampionsIteractor;
import edu.jl.backend.presentation.DTO.ChampionResponseDTO;
import edu.jl.backend.shared.mapper.ChampionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/champions")
public class ChampionController {
    private final ChampionMapper championMapper;
    private final ListChampionsIteractor listChampionsIteractor;

    public ChampionController(
            ChampionMapper championMapper,
            ListChampionsIteractor listChampionsIteractor) {
        this.championMapper = championMapper;
        this.listChampionsIteractor = listChampionsIteractor;
    }

    @GetMapping
    public ResponseEntity<List<ChampionResponseDTO>> findAll() throws Exception {
        List<ChampionResponseDTO> result =
                listChampionsIteractor
                        .listChampions()
                        .stream()
                        .map(championMapper::mapToResponseDTO)
                        .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
