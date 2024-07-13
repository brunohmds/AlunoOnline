package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.service.DisciplinaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    DisciplinaService disciplinaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Disciplina disciplina) {
        disciplinaService.create(disciplina);
    }

    @GetMapping("/professor/{professorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findByProfessorId(@PathVariable Long professorId) {
        return disciplinaService.findByProfessorId(professorId);
    }
}
