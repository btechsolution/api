package com.clinica.api.service;

import com.clinica.api.model.Financeiro;
import com.clinica.api.repository.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {

    @Autowired
    private FinanceiroRepository repository;

    public List<Financeiro> listarTodos() {
        return repository.findAll();
    }

    public Optional<Financeiro> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Financeiro salvar(Financeiro lancamento) {
        if (lancamento.getDataLancamento() == null) {
            lancamento.setDataLancamento(LocalDateTime.now());
        }
        return repository.save(lancamento);
    }

    public Optional<Financeiro> findByConsultaId(Long consultaId) {
        return repository.findByConsultaId(consultaId);
    }

    @Transactional
    public void atualizarStatusLancamentoPorConsulta(Long consultaId, String novoStatus, String motivoCancelamento) {
        Optional<Financeiro> optionalLancamento = repository.findByConsultaId(consultaId);

        if (optionalLancamento.isPresent()) {
            Financeiro lancamento = optionalLancamento.get();
            lancamento.setStatus(novoStatus);

            if ("CANCELADO".equals(novoStatus)) {
                lancamento.setValor(0.0);
                lancamento.setTipo("CANCELADO");

                String descricaoOriginal = lancamento.getDescricao();

                if (!descricaoOriginal.contains("Motivo do Cancelamento:")) {
                    lancamento.setDescricao(descricaoOriginal + " | Motivo do Cancelamento: " + motivoCancelamento);
                } else {

                    lancamento.setDescricao(descricaoOriginal.split(" \\| Motivo do Cancelamento: ")[0] + " | Motivo do Cancelamento: " + motivoCancelamento);
                }
            } else if ("PAGO".equals(novoStatus)) {

                lancamento.setTipo("ENTRADA");

            }
            repository.save(lancamento);
        } else {
            throw new RuntimeException("Lançamento financeiro não encontrado para a consulta ID: " + consultaId);
        }
    }
}