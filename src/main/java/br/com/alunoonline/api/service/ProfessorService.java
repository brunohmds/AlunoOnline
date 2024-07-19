package br.com.alunoonline.api.service;

import br.com.alunoonline.api.client.ViaCepClient;
import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.ProfessorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class ProfessorService {

    private CacheManager cacheManager;
    private ProfessorRepository professorRepository;
    private ViaCepClient viaCepClient;

    public void create(Professor professor) {
        log.info("Iniciando criação de professor");
        atualizaEnderecoPorCep(professor);
        professorRepository.save(professor);
        log.info("Encerrando criação de professor");
        cacheManager.getCache("lista-professores").clear();
    }

    @Cacheable("lista_professores")
    public List<Professor> findAll() {
        log.info("Listagem total de professores");
        return professorRepository.findAll();
    }

    public Professor findProfessorByEmailAndCpf(String email,
                                        String cpf){
        log.info("Listagem total de professores por email e cpf");
        return professorRepository.findProfessorByEmailAndCpf(email, cpf);
    }

    public Optional<Professor> findById(Long id) {
        return professorRepository.findById(id);
    }

    public List<Professor> findByName(String name) {
        return professorRepository.findByName(name);
    }

    public void update(Long id, Professor professor) {
        log.info("Iniciando atualização de professor");
        Optional<Professor> professorFromDb = findById(id);

        if (professorFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado no banco de dados");
        }

        Professor professorUpdated = professorFromDb.get();

        professorUpdated.setName(professor.getName());
        professorUpdated.setCpf(professor.getCpf());
        professorUpdated.setEmail(professor.getEmail());

        professorRepository.save(professorUpdated);
        log.info("Encerrando criação de professor");
        cacheManager.getCache("lista_professor").clear();
    }

    public void deleteById(Long id) {
        log.info("Iniciando exclusão de professores");
        Optional<Professor> professorFromDb = findById(id);
        professorRepository.deleteById(id);
        cacheManager.getCache("lista_professor").clear();
        log.info("Encerrando exclusão de professores");
    }

    private void atualizaEnderecoPorCep(Professor professor) {
        var cep = professor.getEndereco().getCep();
        var enderecoResponse = viaCepClient.consultaCep(cep);
        professor.getEndereco().setLocalidade(enderecoResponse.getLocalidade());
        professor.getEndereco().setUf(enderecoResponse.getUf());
        professor.getEndereco().setBairro(enderecoResponse.getBairro());
        professor.getEndereco().setComplemento(enderecoResponse.getComplemento());
        professor.getEndereco().setLogradouro(enderecoResponse.getLogradouro());
    }
}
