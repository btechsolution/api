package com.clinica.api.dto;

import java.math.BigDecimal;

public record DashboardDTO(
        Long totalPacientes,
        Long consultasHoje,
        BigDecimal saldoMensal,
        Long totalDentistas
) {}