package com.clinica.api.service;

import com.clinica.api.model.Paciente;
import com.clinica.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    @Autowired
    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Paciente cadastrar(Paciente paciente) {
        if (repository.existsByCpf(paciente.getCpf())) {
            throw new RuntimeException("Paciente com este CPF já cadastrado!");
        }

        if (repository.existsByEmail(paciente.getEmail())) {
            throw new RuntimeException("Paciente com este e-mail já cadastrado!");
        }

        paciente.setAtivo(true);
        return repository.save(paciente);
    }

    public List<Paciente> listarTodos() {
        return repository.findAllByAtivoTrue();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Paciente atualizar(Paciente paciente) {
        return repository.save(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        var paciente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado para exclusão!"));

        paciente.excluir();
    }
}