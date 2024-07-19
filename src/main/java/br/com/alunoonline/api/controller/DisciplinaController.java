package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.service.DisciplinaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    private DisciplinaService disciplinaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Disciplina disciplina) {
        disciplinaService.create(disciplina);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findAllOrderByName(){
        return disciplinaService.findAllOrderByName();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Disciplina> findById(@PathVariable Long id){
        return disciplinaService.findById(id);
    }

    @GetMapping("/professor/{professorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findByProfessorId(@PathVariable Long professorId) {
        return disciplinaService.findByProfessorId(professorId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long disciplinaId){
        disciplinaService.deleteById(disciplinaId);
    }

}
