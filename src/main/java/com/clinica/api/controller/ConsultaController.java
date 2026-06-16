package com.clinica.api.controller;

import com.clinica.api.model.Consulta;
import com.clinica.api.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Consulta> agendar(@RequestBody Consulta consulta) {
        Consulta salva = consultaService.agendar(consulta);
        return ResponseEntity.ok(salva);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Consulta> finalizar(@PathVariable Long id) {
        Consulta consulta = consultaService.finalizar(id);
        return ResponseEntity.ok(consulta);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Consulta> cancelar(@PathVariable Long id,
                                             @RequestBody(required = false) String motivo) {
        Consulta consulta = consultaService.cancelar(id, motivo);
        return ResponseEntity.ok(consulta);
    }
}