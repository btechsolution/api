package com.clinica.api.repository;

import com.clinica.api.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {


    Optional<Especialidade> findByNome(String nome);


    boolean existsByNomeIgnoreCase(String nome);


    List<Especialidade> findAllByOrderByIdAsc();

}