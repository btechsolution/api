package com.clinica.api.service;

import com.clinica.api.infra.exception.RegraNegocioException;
import com.clinica.api.model.Consulta;
import com.clinica.api.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public Consulta agendar(Consulta consulta) {

        if (consulta.getDataInicio() == null || consulta.getDataFim() == null) {
            throw new RegraNegocioException("Data e hora de início e fim são obrigatórias.");
        }

        validarDuracao(consulta.getDataInicio(), consulta.getDataFim());
        validarConflitosAgenda(consulta);
        validarReagendamentoMesmoDia(consulta);


        consulta.setStatus("AGENDADA");
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta finalizar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada."));

        if (!"AGENDADA".equals(consulta.getStatus())) {
            throw new RegraNegocioException("Somente consultas AGENDADAS podem ser finalizadas.");
        }


        consulta.setStatus("REALIZADA");

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta cancelar(Long id, String motivo) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Consulta não encontrada."));

        if (!"AGENDADA".equals(consulta.getStatus())) {
            throw new RegraNegocioException("Somente consultas AGENDADAS podem ser canceladas.");
        }

        consulta.setMotivoCancelamento(motivo);
        consulta.setStatus("CANCELADA");
        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    // ===== validações =====

    private void validarDuracao(LocalDateTime inicio, LocalDateTime fim) {
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("Horário final não pode ser anterior ao horário inicial.");
        }

        Duration duracao = Duration.between(inicio, fim);

        // mínimo 30 minutos
        if (duracao.compareTo(Duration.ofMinutes(30)) < 0) {
            throw new RegraNegocioException("Duração deve ser de no mínimo 30 minutos.");
        }
    }

    private void validarConflitosAgenda(Consulta novaConsulta) {
        List<Consulta> todas = consultaRepository.findAll();

        LocalDateTime novoInicio = novaConsulta.getDataInicio();
        LocalDateTime novoFim = novaConsulta.getDataFim();

        for (Consulta existente : todas) {
            if (novaConsulta.getId() != null &&
                    existente.getId() != null &&
                    existente.getId().equals(novaConsulta.getId())) {
                continue;
            }

            if ("CANCELADA".equals(existente.getStatus())) {
                continue;
            }

            boolean mesmoDentista = existente.getDentista() != null &&
                    novaConsulta.getDentista() != null &&
                    existente.getDentista().getId().equals(novaConsulta.getDentista().getId());

            boolean mesmoPaciente = existente.getPaciente() != null &&
                    novaConsulta.getPaciente() != null &&
                    existente.getPaciente().getId().equals(novaConsulta.getPaciente().getId());

            if (!mesmoDentista && !mesmoPaciente) {
                continue;
            }

            LocalDateTime extInicio = existente.getDataInicio();
            LocalDateTime extFim = existente.getDataFim();

            boolean sobrepoe = novoInicio.isBefore(extFim) && novoFim.isAfter(extInicio);

            if (sobrepoe) {
                if (mesmoDentista) {
                    throw new RegraNegocioException(
                            "O profissional selecionado já possui um atendimento marcado neste horário.");
                }
                if (mesmoPaciente) {
                    throw new RegraNegocioException(
                            "Este paciente já possui outro atendimento marcado neste horário.");
                }
            }
        }
    }

    private void validarReagendamentoMesmoDia(Consulta novaConsulta) {
        List<Consulta> todas = consultaRepository.findAll();

        LocalDate dataNova = novaConsulta.getDataInicio().toLocalDate();
        Long pacienteId = novaConsulta.getPaciente() != null ? novaConsulta.getPaciente().getId() : null;

        if (pacienteId == null) {
            return;
        }

        boolean cancelouNesseDia = todas.stream()
                .anyMatch(c ->
                        c.getPaciente() != null &&
                                c.getPaciente().getId().equals(pacienteId) &&
                                "CANCELADA".equals(c.getStatus()) &&
                                c.getDataInicio().toLocalDate().equals(dataNova)
                );

        if (cancelouNesseDia) {
            throw new RegraNegocioException(
                    "O paciente teve uma consulta cancelada nesta data. " +
                            "Novas marcações só são permitidas a partir do dia seguinte."
            );
        }
    }
}