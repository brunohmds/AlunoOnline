package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class DisciplinaService {

    private DisciplinaRepository disciplinaRepository;
    private CacheManager cacheManager;

    public void create(Disciplina disciplina) {
        log.info("Iniciando criação de disciplina");
        disciplinaRepository.save(disciplina);
        log.info("Encerrando criação de disciplina");
        cacheManager.getCache("lista_disciplinas_por_professor").clear();
    }

    public Optional<Disciplina> findById(Long id) {
        return disciplinaRepository.findById(id);
    }

    @Cacheable("lista_disciplinas_por_professor")
    public List<Disciplina> findByProfessorId(Long professorId) {
        log.info("Listando disciplinas por professor");
        return disciplinaRepository.findByProfessorId(professorId);
    }

    public List<Disciplina> findAllOrderByName() {
        return disciplinaRepository.findAllOrderByName();
    }

    public void deleteById(Long disciplinaId) {
        log.info("Iniciando exclusão de disciplinas");
        Optional<Disciplina> disciplinaFromDb = findById(disciplinaId);
        disciplinaRepository.deleteById(disciplinaId);
        cacheManager.getCache("lista_disciplinas_por_professor").clear();
        log.info("Encerrando exclusão de disciplinas");
    }

}
