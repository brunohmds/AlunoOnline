package br.com.alunoonline.api.dtos;

import lombok.Data;

import java.util.List;

@Data
public class HistoricoAlunoResponse {

    private String nomeDoAluno;
    private String cpfDoAluno;
    private String emailDoAluno;

    private List<DisciplinasAlunoResponse> disciplinasAlunoResponseList;
}
