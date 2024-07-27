package br.com.alunoonline.api.service;

import br.com.alunoonline.api.client.ViaCepClient;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@AllArgsConstructor
@Service
public class AlunoService {

    private AlunoRepository alunoRepository;
    private CacheManager cacheManager;
    private ViaCepClient viaCepClient;
    private List<Aluno> alunos = new ArrayList<>();

    public void create(Aluno aluno) {
        log.info("Iniciando criação de aluno");
        atualizaEnderecoPorCep(aluno);
        alunoRepository.save(aluno);
        log.info("Encerrando criação de aluno");
        cacheManager.getCache("lista_alunos").clear();
    }

    public void createAll(List<Aluno> listaAlunos){
        alunoRepository.saveAll(listaAlunos);
    }

    @Cacheable("lista_alunos")
    public List<Aluno> findAll() {
        log.info("Listagem total de alunos");
        return alunoRepository.findAll();
    }

    public Aluno findAlunoByEmailAndCpf(String email,
                                         String cpf){
        log.info("Listagem total de alunos por email e cpf");
        return alunoRepository.findAlunoByEmailAndCpf(email, cpf);
    }

    public Optional<Aluno> findById(Long id) {
        return alunoRepository.findById(id);
    }

    public List<Aluno> findByName(String name) {
        return alunoRepository.findByName(name);
    }

    public void update(Long id, Aluno aluno) {
        log.info("Iniciando atualização de aluno");
        Optional<Aluno> alunoFromDb = findById(id);

        if (alunoFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados");
        }

        Aluno alunoUpdated = alunoFromDb.get();

        alunoUpdated.setName(aluno.getName());
        alunoUpdated.setCpf(aluno.getCpf());
        alunoUpdated.setEmail(aluno.getEmail());

        alunoRepository.save(alunoUpdated);
        log.info("Encerrando criação de aluno");
        cacheManager.getCache("lista_aluno").clear();
    }

    public void deleteById(Long id) {
        log.info("Iniciando exclusão de alunos");
        Optional<Aluno> alunoFromDb = findById(id);
        alunoRepository.deleteById(id);
        cacheManager.getCache("lista_aluno").clear();
        log.info("Encerrando exclusão de alunos");
    }

    public void atualizaEnderecoPorCep(Aluno aluno) {
        var cep = aluno.getEndereco().getCep();
        log.info("Consultando CEP {} ", cep);
        try{
            var enderecoResponse = viaCepClient.consultaCep(cep);

            aluno.getEndereco().setLocalidade(enderecoResponse.getLocalidade());
            aluno.getEndereco().setUf(enderecoResponse.getUf());
            aluno.getEndereco().setBairro(enderecoResponse.getBairro());
            aluno.getEndereco().setComplemento(enderecoResponse.getComplemento());
            aluno.getEndereco().setLogradouro(enderecoResponse.getLogradouro());
        }
        catch(Exception e){
            log.warn("Erro de integração: CEP não encontrado {} ", cep);
        }
    }
}
