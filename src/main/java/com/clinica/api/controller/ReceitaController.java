package com.clinica.api.controller;

import com.clinica.api.model.Paciente;
import com.clinica.api.model.Receita;
import com.clinica.api.repository.PacienteRepository;
import com.clinica.api.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;


    @GetMapping
    public ResponseEntity<List<Receita>> listarTodas() {
        List<Receita> lista = receitaRepository.findAll();
        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<Receita> salvar(@RequestBody Receita receita) {
        if (receita.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(receita.getPacienteId()).orElse(null);
            if (paciente != null) {
                receita.setPacienteNome(paciente.getNome());
            } else {
                receita.setPacienteNome("Paciente Avulso");
            }
        } else {
            receita.setPacienteNome("Paciente Avulso");
        }

        // data de emissão automática no servidor
        receita.setDataEmissao(LocalDate.now());

        Receita receitaSalva = receitaRepository.save(receita);
        return ResponseEntity.ok(receitaSalva);
    }
}