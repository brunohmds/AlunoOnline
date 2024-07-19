package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.AlunoRelatorioListaDTO;
import br.com.alunoonline.api.service.AlunoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid  @RequestBody Aluno aluno) {
        alunoService.create(aluno);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Aluno> findAll() {
        return alunoService.findAll();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Aluno> findById(@PathVariable Long id) {
        return alunoService.findById(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Aluno> findByName(@PathVariable String name) {
        return alunoService.findByName(name);
    }

    @GetMapping("/relatorio/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoRelatorioListaDTO emitirRelatorio(@PathVariable Long id) {
        Aluno aluno = alunoService.findById(id).get();
        AlunoRelatorioListaDTO dto = new AlunoRelatorioListaDTO();
        dto.setName(aluno.getName());
        dto.setEmail(aluno.getEmail());
        return dto;
    }

    @GetMapping("/{email}/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Aluno> findAlunoByEmailAndCpf(@PathVariable String email ,
                                              @PathVariable String cpf) {
        return Optional.of(
                alunoService.findAlunoByEmailAndCpf(email, cpf)
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody Aluno aluno){
        alunoService.update(id, aluno);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        alunoService.deleteById(id);
    }
}
