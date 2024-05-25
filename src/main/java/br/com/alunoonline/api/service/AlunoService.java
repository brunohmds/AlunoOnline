package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    AlunoRepository alunoRepository;

    public void create(Aluno aluno){
        alunoRepository.save(aluno);
    }

    public List<Aluno> findAll(){
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id){
        Optional<Aluno> alunoFromDb = alunoRepository.findById(id);

        if(alunoFromDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados");
        }
        return alunoFromDb;
    }

    public void update (Long id, Aluno aluno){
        // Buscar o aluno no banco de dados
        Optional<Aluno> alunoFromDb = findById(id);

        // Lança exceção se não encontrar o aluno no BD
        if(alunoFromDb.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados");
        }

        // Se chegar aqui, existe aluno, então precisa armazenar em uma variável
        Aluno alunoUpdated = alunoFromDb.get();

        // Pego o alunoUpdated e faz os sets necessarios para atualizar os atributos dele
        // Alunoupdated: Aluno que está na memoria ram para ser atualizado sem vazio
        // aluno: é o objeto java que anteriormente era um JSON vindo do front
        alunoUpdated.setName(aluno.getName());
        alunoUpdated.setCpf(aluno.getCpf());
        alunoUpdated.setEmail(aluno.getEmail());

        // Peguei a cópia do aluno alterado na memória ram e devolvi esse aluno agora atualizado
        // para o banco de dados através do alunoRepository
        alunoRepository.save(alunoUpdated);
    }

    public void deleteById(Long id){
        Optional<Aluno> alunoFromDb = findById(id);
        alunoRepository.deleteById(id);
    }
}
