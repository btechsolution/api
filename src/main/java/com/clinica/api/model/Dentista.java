package com.clinica.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "dentistas")
@Entity(name = "Dentista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dentista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;


    @Column(unique = true, nullable = false)
    private String cpf;


    @Column(unique = true, nullable = false)
    private String cro;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dentista_especialidade",
            joinColumns = @JoinColumn(name = "id_dentista"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidade")
    )
    private Set<Especialidade> especialidades = new HashSet<>();

    private boolean ativo = true;


    private boolean emFerias = false;

    @Column(name = "justificativa_status", length = 500)
    private String justificativaStatus;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    public void excluir() {
        this.ativo = false;
    }
}