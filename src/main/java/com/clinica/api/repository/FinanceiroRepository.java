package com.clinica.api.repository;

import com.clinica.api.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importe Optional

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {

    @Query("SELECT f FROM Financeiro f ORDER BY f.dataLancamento DESC")
    List<Financeiro> findAllOrderByDataLancamentoDesc();


    Optional<Financeiro> findByConsultaIdAndStatus(Long consultaId, String status);


    Optional<Financeiro> findByConsultaId(Long consultaId);
}