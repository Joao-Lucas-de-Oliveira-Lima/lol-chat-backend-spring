package edu.jl.backend.infrastructure.controller;

import edu.jl.backend.application.usercase.AskAChampionInteractor;
import edu.jl.backend.application.usercase.ListChampionsInteractor;
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
    private final ListChampionsInteractor listChampionsInteractor;
    private final AskAChampionInteractor askAChampionInteractor;

    public ChampionController(
            ChampionMapper championMapper,
            ListChampionsInteractor listChampionsInteractor,
            AskAChampionInteractor askAChampionInteractor) {
        this.championMapper = championMapper;
        this.listChampionsInteractor = listChampionsInteractor;
        this.askAChampionInteractor = askAChampionInteractor;
    }

    @GetMapping
    public ResponseEntity<List<ChampionDTO>> findAll() throws Exception {
        List<ChampionDTO> result =
                listChampionsInteractor
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
                    askAChampionInteractor.askAChampion(championId, questionForAChampion.question());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AnswerFromTheChampionDTO(championResponse));
    }


}
