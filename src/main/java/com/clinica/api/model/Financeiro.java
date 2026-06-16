package com.clinica.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double valor;
    private String tipo;
    private String status;

    @Column(name = "data_lancamento")
    private LocalDateTime dataLancamento;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "consulta_id")
    private Long consultaId;
}