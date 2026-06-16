package com.clinica.api.repository;

import com.clinica.api.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    List<Consulta> findByPacienteId(Long pacienteId);


    List<Consulta> findAllByDentistaId(Long dentistaId);


    List<Consulta> findAllByUsuarioId(Long usuarioId);


    List<Consulta> findByDataInicioBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Valida se o DENTISTA já tem consulta que sobreponha o horário desejado.
     */
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.dentista.id = :dentistaId " +
            "AND c.status <> :status " +
            "AND (:inicio < c.dataFim AND :fim > c.dataInicio)")
    boolean existsByDentistaIdAndStatusNotAndDataInicioLessThanAndDataFimGreaterThan(
            @Param("dentistaId") Long dentistaId,
            @Param("status") String status,
            @Param("fim") LocalDateTime fim,
            @Param("inicio") LocalDateTime inicio
    );

    /**
     * Valida se o PACIENTE já tem consulta que sobreponha o horário desejado.
     */
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.paciente.id = :pacienteId " +
            "AND c.status <> :status " +
            "AND (:inicio < c.dataFim AND :fim > c.dataInicio)")
    boolean existsByPacienteIdAndStatusNotAndDataInicioLessThanAndDataFimGreaterThan(
            @Param("pacienteId") Long pacienteId,
            @Param("status") String status,
            @Param("fim") LocalDateTime fim,
            @Param("inicio") LocalDateTime inicio
    );
}