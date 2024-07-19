package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/professor")
public class ProfessorController {

    private ProfessorService professorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Professor professor) {
        professorService.create(professor);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Professor> findAll() {
        return professorService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Professor> findById(@PathVariable Long id) {
        return professorService.findById(id);
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Professor> findByName(@PathVariable String name) {
        return professorService.findByName(name);
    }

    @GetMapping("/{email}/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Professor> findProfessorByEmailAndCpf(@PathVariable String email ,
                                                  @PathVariable String cpf) {
        return Optional.of(
                professorService.findProfessorByEmailAndCpf(email, cpf)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody Professor professor){
        professorService.update(id, professor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        professorService.deleteById(id);
    }
}
