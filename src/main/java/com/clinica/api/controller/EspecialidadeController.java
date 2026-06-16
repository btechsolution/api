package com.clinica.api.controller;

import com.clinica.api.model.Especialidade;
import com.clinica.api.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeRepository repository;

    @GetMapping
    public ResponseEntity<Page<Especialidade>> listar(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(repository.findAll(paginacao));
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Especialidade especialidade) {
        if (repository.existsByNomeIgnoreCase(especialidade.getNome())) {
            return ResponseEntity.badRequest().body("Esta especialidade já está cadastrada.");
        }

        var especialidadeSalva = repository.save(especialidade);
        return ResponseEntity.ok(especialidadeSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}