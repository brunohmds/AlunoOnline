package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.AtualizarNotasRequest;
import br.com.alunoonline.api.dtos.DisciplinasAlunoResponse;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponse;
import br.com.alunoonline.api.enums.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaAlunoService {

    public static final Double MEDIA_PARA_SER_APROVADO = 7.00;

    @Autowired
    MatriculaAlunoRepository matriculaAlunoRepository;

    public void create (MatriculaAluno matriculaAluno) {
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        matriculaAlunoRepository.save(matriculaAluno);
    }

    public void atualizarNotas(Long matriculaAlunoId, AtualizarNotasRequest atualizarNotasRequest){
        MatriculaAluno matriculaAluno = matriculaAlunoRepository.
                findById(matriculaAlunoId).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        Double nota1 = atualizarNotasRequest.getNota1();
        Double nota2 = atualizarNotasRequest.getNota2();
        // O front mandou a nota 1? Então atualiza em martriculaaluno com essa nota
        if (nota1 != null){
            matriculaAluno.setNota1(nota1);
        }

        // O front mandou a nota 2? Então atualiza em martriculaaluno com essa nota
        if (nota2 != null){
            matriculaAluno.setNota2(nota2);
        }

        // E se a matrícula tiver ambas as notas? Já posso calcular a média
        if (nota1 != null && nota2 != null) {
            Double media = (nota1 + nota2) / 2;
            matriculaAluno.setStatus(media >= MEDIA_PARA_SER_APROVADO ? MatriculaAlunoStatusEnum.APROVADO : MatriculaAlunoStatusEnum.REPROVADO);
        }

        matriculaAlunoRepository.save(matriculaAluno);
    }
    
    public void atualizarStatusParaTrancado(Long matriculaAlunoId){
        MatriculaAluno matriculaAluno = matriculaAlunoRepository.
                findById(matriculaAlunoId).orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));

        if(!MatriculaAlunoStatusEnum.MATRICULADO.equals(matriculaAluno.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível trancar uma matrícula com status MATRICULADO");
        }
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.TRANCADO);

        matriculaAlunoRepository.save(matriculaAluno);
    }

    public HistoricoAlunoResponse emitirHistoricoDoAluno(Long alunoId){
        List<MatriculaAluno> matriculasAluno = matriculaAlunoRepository.findByAlunoId(alunoId);

        if(matriculasAluno.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matrículas");
        }

        // primeira parte do histórico
        HistoricoAlunoResponse historicoAlunoResponse = new HistoricoAlunoResponse();
        historicoAlunoResponse.setNomeDoAluno(matriculasAluno.get(0).getAluno().getName());
        historicoAlunoResponse.setCpfDoAluno(matriculasAluno.get(0).getAluno().getCpf());
        historicoAlunoResponse.setEmailDoAluno(matriculasAluno.get(0).getAluno().getEmail());

        List<DisciplinasAlunoResponse> disciplinasList = new ArrayList<>();
        for (MatriculaAluno matriculaAluno : matriculasAluno){
            DisciplinasAlunoResponse disciplinasAlunoResponse = new DisciplinasAlunoResponse();
            disciplinasAlunoResponse.setNomeDaDisciplina(matriculaAluno.getDisciplina().getName());
            disciplinasAlunoResponse.setNomeDoProfessor(matriculaAluno.getDisciplina().getProfessor().getName());
            disciplinasAlunoResponse.setNota1(matriculaAluno.getNota1());
            disciplinasAlunoResponse.setNota2(matriculaAluno.getNota2());

            disciplinasAlunoResponse.setMedia(calcularMedia(matriculaAluno.getNota1(), matriculaAluno.getNota2()));

            disciplinasAlunoResponse.setStatus(matriculaAluno.getStatus());

            disciplinasList.add(disciplinasAlunoResponse);
        }
        historicoAlunoResponse.setDisciplinasAlunoResponseList(disciplinasList);

        return historicoAlunoResponse;
    }

    private Double calcularMedia(Double nota1, Double nota2){
        if (nota1 == null || nota2 == null){
            return null;
        }
        return (nota1 + nota2)/2;
    }
}