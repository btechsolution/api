package com.clinica.api.repository;

import com.clinica.api.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {


    Optional<Dentista> findByCro(String cro);

    Optional<Dentista> findByEmail(String email);


    boolean existsByEmail(String email);


    List<Dentista> findByEspecialidadesNomeIgnoreCase(String nome);


    List<Dentista> findAllByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);


    long countByAtivoTrue();
}