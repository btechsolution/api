package com.clinica.api.controller;

import com.clinica.api.model.Paciente;
import com.clinica.api.repository.PacienteRepository;
import com.clinica.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public ResponseEntity<Page<Paciente>> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(repository.findAll(paginacao));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Paciente> salvar(@RequestBody Paciente paciente) {
        paciente.setAtivo(true);
        return ResponseEntity.ok(repository.save(paciente));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id, @RequestBody Paciente dadosNovos) {
        Paciente existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        existente.setNome(dadosNovos.getNome());
        existente.setEmail(dadosNovos.getEmail());
        existente.setCpf(dadosNovos.getCpf());
        existente.setTelefone(dadosNovos.getTelefone());

        return ResponseEntity.ok(repository.save(existente));
    }

    @PutMapping("/{id}/alterar-status")
    @Transactional
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Paciente paciente = repository.findById(id).orElseThrow();
        boolean novoStatus = (boolean) payload.get("ativo");
        String justificativa = (String) payload.get("justificativaStatus");

        if (!novoStatus) {
            boolean possuiPendencias = consultaRepository.findAll().stream()
                    .anyMatch(c -> c.getPaciente() != null
                            && c.getPaciente().getId().equals(id)
                            && "AGENDADA".equals(c.getStatus()));

            if (possuiPendencias) {
                return ResponseEntity.badRequest()
                        .body("Não é possível desativar um paciente que possui consultas agendadas pendentes.");
            }
            if (justificativa == null || justificativa.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("É obrigatório informar uma justificativa para a desativação.");
            }
            paciente.setJustificativaStatus(justificativa);
        } else {
            paciente.setJustificativaStatus(null);
        }

        paciente.setAtivo(novoStatus);
        repository.save(paciente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}