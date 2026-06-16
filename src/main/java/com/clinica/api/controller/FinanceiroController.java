package com.clinica.api.controller;

import com.clinica.api.model.Financeiro;
import com.clinica.api.service.FinanceiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financeiro")
@CrossOrigin(origins = "http://localhost:4200")
public class FinanceiroController {

    @Autowired
    private FinanceiroService service;

    @GetMapping
    public ResponseEntity<List<Financeiro>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<?> cadastrarLancamento(@RequestBody Financeiro lancamento) {
        try {
            if (lancamento.getConsultaId() != null && lancamento.getStatus() == null) {
                lancamento.setStatus("PENDENTE");
            } else if (lancamento.getConsultaId() == null && lancamento.getStatus() == null) {
                lancamento.setStatus("PAGO");
            }
            // Garante que o tipo seja ENTRADA para lançamentos de consulta ou manuais pagos
            if (lancamento.getTipo() == null && "PAGO".equals(lancamento.getStatus())) {
                lancamento.setTipo("ENTRADA");
            } else if (lancamento.getTipo() == null && "PENDENTE".equals(lancamento.getStatus())) {
                lancamento.setTipo("ENTRADA"); // Lançamentos pendentes são entradas esperadas
            }
            Financeiro salvo = service.salvar(lancamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-status-por-consulta/{consultaId}")
    public ResponseEntity<?> atualizarStatusLancamentoPorConsulta(@PathVariable Long consultaId, @RequestBody Map<String, String> payload) {
        String novoStatus = payload.get("status");
        String motivoCancelamento = payload.get("motivoCancelamento"); // Captura o motivo do cancelamento

        if (novoStatus == null || (!novoStatus.equals("PENDENTE") && !novoStatus.equals("PAGO") && !novoStatus.equals("CANCELADO"))) {
            return ResponseEntity.badRequest().body("Status de lançamento inválido. Deve ser PENDENTE, PAGO ou CANCELADO.");
        }

        if ("CANCELADO".equals(novoStatus) && (motivoCancelamento == null || motivoCancelamento.trim().isEmpty())) {
            return ResponseEntity.badRequest().body("Motivo do cancelamento é obrigatório para status CANCELADO.");
        }

        try {
            service.atualizarStatusLancamentoPorConsulta(consultaId, novoStatus, motivoCancelamento); // Passa o motivo
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar status do lançamento financeiro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Financeiro lancamento) {
        try {
            return service.buscarPorId(id)
                    .map(lancamentoExistente -> {
                        lancamento.setId(id);

                        if (lancamento.getConsultaId() == null) {
                            lancamento.setConsultaId(lancamentoExistente.getConsultaId());
                        }
                        if (lancamento.getDataLancamento() == null) {
                            lancamento.setDataLancamento(lancamentoExistente.getDataLancamento());
                        }
                        if (lancamento.getPaciente() == null) {
                            lancamento.setPaciente(lancamentoExistente.getPaciente());
                        }
                        if (lancamento.getTipo() == null) {
                            lancamento.setTipo(lancamentoExistente.getTipo());
                        }
                        if (lancamento.getStatus() == null) {
                            lancamento.setStatus(lancamentoExistente.getStatus());
                        }

                        Financeiro atualizado = service.salvar(lancamento);
                        return ResponseEntity.ok(atualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}