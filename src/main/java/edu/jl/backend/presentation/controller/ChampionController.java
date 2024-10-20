package edu.jl.backend.presentation.controller;

import edu.jl.backend.application.usercase.AskAChampionIteractor;
import edu.jl.backend.application.usercase.ListChampionsIteractor;
import edu.jl.backend.domain.exception.InvalidQuestionException;
import edu.jl.backend.presentation.DTO.ChampionDTO;
import edu.jl.backend.presentation.DTO.QuestionForAChampionDTO;
import edu.jl.backend.presentation.DTO.AnswerFromTheChampionDTO;
import edu.jl.backend.shared.mapper.ChampionMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/champions")
public class ChampionController {
    private final ChampionMapper championMapper;
    private final ListChampionsIteractor listChampionsIteractor;
    private final AskAChampionIteractor askAChampionIteractor;

    public ChampionController(
            ChampionMapper championMapper,
            ListChampionsIteractor listChampionsIteractor,
            AskAChampionIteractor askAChampionIteractor) {
        this.championMapper = championMapper;
        this.listChampionsIteractor = listChampionsIteractor;
        this.askAChampionIteractor = askAChampionIteractor;
    }

    @GetMapping
    public ResponseEntity<List<ChampionDTO>> findAll() throws Exception {
        List<ChampionDTO> result =
                listChampionsIteractor
                        .listChampions()
                        .stream()
                        .map(championMapper::mapToDTO)
                        .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);

    }

    @PostMapping(value = "/ask/{id}")
    public ResponseEntity<AnswerFromTheChampionDTO> ask(
            @PathVariable(name = "id") Long championId,
            @RequestBody @Valid QuestionForAChampionDTO questionForAChampion,
            BindingResult bindingResult) throws Exception
    {
            if(bindingResult.hasErrors())
                throw new InvalidQuestionException("Question cannot be blank or missing!");
            String championResponse =
                    askAChampionIteractor.askAChampion(championId, questionForAChampion.question());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AnswerFromTheChampionDTO(championResponse));
    }


}
