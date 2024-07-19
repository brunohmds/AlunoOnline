package br.com.alunoonline.api.repository;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("SELECT p, COUNT(d) FROM Professor p JOIN p.disciplinas d GROUP BY p")
    List<Object[]> countDisciplinasByProfessor();

    @Query("SELECT p FROM Professor p WHERE p.name=:nome")
    List<Professor> findByName(@Param("nome") String nome);

    List<Professor> findAllByEmail(String email);

    @Query("SELECT p FROM Professor p where p.email=:email AND p.cpf=:cpf")
    Professor findProfessorByEmailAndCpf(@Param("email") String email,
                                 @Param("cpf") String cpf);
}
