package com.clinica.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "consultas")
@Entity(name = "Consulta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_dentista")
    private Dentista dentista;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String descricao;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @CreationTimestamp
    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;

    private String status; // AGENDADA, CANCELADA, FINALIZADA

    public LocalDateTime getDataHoraInicio() {
        return this.dataInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return this.dataFim;
    }
}