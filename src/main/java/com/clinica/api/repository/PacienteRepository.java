package com.clinica.api.repository;

import com.clinica.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findAllByAtivoTrue();

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByNomeIgnoreCase(String nome);
}