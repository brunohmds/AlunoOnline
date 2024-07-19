package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.AtualizarNotasRequest;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponse;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.service.MatriculaAlunoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/matricula-aluno")
public class MatriculaAlunoController {

    private MatriculaAlunoService matriculaAlunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody MatriculaAluno matriculaAluno) {
        matriculaAlunoService.create(matriculaAluno);
    }

    @PatchMapping("/atualizar-notas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarNotas(@PathVariable Long id, @RequestBody AtualizarNotasRequest atualizarNotasRequest) {
        matriculaAlunoService.atualizarNotas(id, atualizarNotasRequest);
    }

    @PatchMapping("/trancar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarStatusParaTrancado(@PathVariable Long id) {
        matriculaAlunoService.atualizarStatusParaTrancado(id);
    }

    @GetMapping("/{aluno}")
    @ResponseStatus(HttpStatus.OK)
    public List<MatriculaAluno> findMatriculasByAlunoName(@PathVariable String alunoName) {
        return matriculaAlunoService.findMatriculasByAlunoName(alunoName);
    }

    @GetMapping("/{disciplina}")
    @ResponseStatus(HttpStatus.OK)
    public List<MatriculaAluno> findMatriculasByDisciplinaName(@PathVariable String disciplinaName) {
        return matriculaAlunoService.findMatriculasByDisciplninaName(disciplinaName);
    }

    @GetMapping("/emitir-historico/{alunoId}")
    @ResponseStatus(HttpStatus.OK)
    public HistoricoAlunoResponse emitirHistoricoDoAluno(@PathVariable Long alunoId) {
        return matriculaAlunoService.emitirHistoricoDoAluno(alunoId);
    }
}
