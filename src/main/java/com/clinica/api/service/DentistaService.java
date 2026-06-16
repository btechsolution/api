package com.clinica.api.service;

import com.clinica.api.model.Dentista;
import com.clinica.api.repository.DentistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DentistaService {

    @Autowired
    private DentistaRepository repository;

    public List<Dentista> listarTodos() {
        return repository.findAll();
    }

    public Dentista cadastrar(Dentista d) {
        d.setAtivo(true);
        d.setEmFerias(false);
        return repository.save(d);
    }

    public Dentista atualizar(Long id, Dentista dados) {
        Dentista d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
        d.setNome(dados.getNome());
        d.setEmail(dados.getEmail());
        d.setCro(dados.getCro());
        d.setEspecialidades(dados.getEspecialidades());
        // não mexe em ativo / emFerias / justificativaStatus aqui
        return repository.save(d);
    }
}