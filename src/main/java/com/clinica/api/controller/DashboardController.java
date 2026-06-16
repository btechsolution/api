package com.clinica.api.controller;

import com.clinica.api.model.Financeiro;
import com.clinica.api.repository.ConsultaRepository;
import com.clinica.api.repository.DentistaRepository;
import com.clinica.api.repository.FinanceiroRepository;
import com.clinica.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> getResumo() {
        Map<String, Object> resumo = new HashMap<>();


        resumo.put("totalPacientes", pacienteRepository.count());


        long dentistasAtivos = dentistaRepository.countByAtivoTrue();


        resumo.put("totalDentistas", dentistasAtivos);
        resumo.put("totalDentistasAtivos", dentistasAtivos);
        resumo.put("dentistasAtivos", dentistasAtivos);


        LocalDateTime inicioHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime fimHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);


        long consultasHoje = consultaRepository.findByDataInicioBetween(inicioHoje, fimHoje).size();
        resumo.put("consultasHoje", consultasHoje);

        // Lógica do Saldo Geral
        List<Financeiro> lancamentos = financeiroRepository.findAll();

        double saldoMensal = lancamentos.stream()
                .mapToDouble(f -> {
                    if (f.getValor() == null) return 0.0;


                    return "ENTRADA".equalsIgnoreCase(f.getTipo()) ? f.getValor() : -f.getValor();
                })
                .sum();

        resumo.put("saldoMensal", saldoMensal);

        return ResponseEntity.ok(resumo);
    }
}