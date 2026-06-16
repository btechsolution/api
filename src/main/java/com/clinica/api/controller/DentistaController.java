package com.clinica.api.controller;

import com.clinica.api.model.Dentista;
import com.clinica.api.repository.DentistaRepository;
import com.clinica.api.repository.ConsultaRepository;
import com.clinica.api.service.DentistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/dentistas")
public class DentistaController {

    @Autowired
    private DentistaRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private DentistaService dentistaService;

    @GetMapping
    public ResponseEntity<Page<Dentista>> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(repository.findAll(paginacao));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Dentista> salvar(@RequestBody Dentista dentista) {
        dentista.setAtivo(true);
        dentista.setEmFerias(false);
        return ResponseEntity.ok(repository.save(dentista));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Dentista> atualizar(@PathVariable Long id, @RequestBody Dentista dados) {
        Dentista atualizado = dentistaService.atualizar(id, dados);
        return ResponseEntity.ok(atualizado);
    }

    @PutMapping("/{id}/alterar-status")
    @Transactional
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Dentista dentista = repository.findById(id).orElseThrow();
        boolean novoStatus = (boolean) payload.get("ativo");
        String justificativa = (String) payload.get("justificativaStatus");

        if (!novoStatus) {
            boolean possuiPendencias = consultaRepository.findAll().stream()
                    .anyMatch(c -> c.getDentista() != null
                            && c.getDentista().getId().equals(id)
                            && "AGENDADA".equals(c.getStatus()));

            if (possuiPendencias) {
                return ResponseEntity.badRequest()
                        .body("Não é possível desativar um dentista que possui consultas agendadas em sua grade.");
            }
            if (justificativa == null || justificativa.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("É obrigatório informar uma justificativa para a desativação.");
            }
            dentista.setJustificativaStatus(justificativa);
        } else {
            dentista.setJustificativaStatus(null);
        }

        dentista.setAtivo(novoStatus);
        repository.save(dentista);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/entrar-ferias")
    @Transactional
    public ResponseEntity<?> entrarFerias(@PathVariable Long id) {
        Dentista dentista = repository.findById(id).orElseThrow();

        boolean possuiConsultasAgendadas = consultaRepository.findAll().stream()
                .anyMatch(c -> c.getDentista() != null
                        && c.getDentista().getId().equals(id)
                        && "AGENDADA".equals(c.getStatus()));

        if (possuiConsultasAgendadas) {
            return ResponseEntity.badRequest()
                    .body("Operação recusada. Não é possível entrar em férias enquanto houver consultas ativas marcadas na sua agenda.");
        }

        dentista.setEmFerias(true);
        repository.save(dentista);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/retornar-ferias")
    @Transactional
    public ResponseEntity<?> retornarFerias(@PathVariable Long id) {
        Dentista dentista = repository.findById(id).orElseThrow();
        dentista.setEmFerias(false);
        repository.save(dentista);
        return ResponseEntity.ok().build();
    }
}