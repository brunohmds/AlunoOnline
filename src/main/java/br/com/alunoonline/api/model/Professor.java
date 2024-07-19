package br.com.alunoonline.api.model;

import br.com.alunoonline.api.validators.EmailValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Professor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Nome do professor não pode ser em branco")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    private String name;

    @EmailValidation
    private String email;

    @CPF
    private String cpf;

    @OneToMany(mappedBy = "professor")
    private Set<Disciplina> disciplinas;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    private Endereco endereco;

}
