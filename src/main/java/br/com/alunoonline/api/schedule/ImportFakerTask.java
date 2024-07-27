package br.com.alunoonline.api.schedule;

import br.com.alunoonline.api.client.AlunoImportClient;
import br.com.alunoonline.api.dtos.AlunoFakerResponseDTO;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Endereco;
import br.com.alunoonline.api.service.AlunoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImportFakerTask {
    private final AlunoService alunoService;
    private final AlunoImportClient alunoImportClient;

    @Scheduled(fixedRate = 60000)
    public void importarAlunos(){
        var listaAluno = new ArrayList<Aluno>();
        log.info("Start - ImportFaker");
        List<AlunoFakerResponseDTO> lista = alunoImportClient.getListaAlunos();
        for(AlunoFakerResponseDTO alunoFaker: lista) {
            var endereco = new Endereco();
            endereco.setNumero(alunoFaker.getNumeroCasa());
            endereco.setCep(alunoFaker.getCep());
            Aluno aluno = Aluno.builder()
                    .name(alunoFaker.getName())
                    .anoNascimento(alunoFaker.getAnoNascimento())
                    .cpf(alunoFaker.getCpf())
                    .email(alunoFaker.getEmail())
                    .endereco(endereco)
                    .build();
            alunoService.atualizaEnderecoPorCep(aluno);
            listaAluno.add(aluno);
        }
        alunoService.createAll(listaAluno);
        }
}
