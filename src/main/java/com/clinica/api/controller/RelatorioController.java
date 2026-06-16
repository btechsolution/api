package com.clinica.api.controller;

import com.clinica.api.model.Consulta;
import com.clinica.api.model.Financeiro;
import com.clinica.api.model.Paciente;
import com.clinica.api.repository.ConsultaRepository;
import com.clinica.api.repository.PacienteRepository;
import com.clinica.api.service.FinanceiroService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;


    //  BALANÇO FINANCEIRO

    @GetMapping("/financeiro")
    public ResponseEntity<byte[]> gerarRelatorioFinanceiro() {
        try {
            List<Financeiro> lancamentos = financeiroService.listarTodos();

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();

            Color deepBlue = new Color(27, 73, 101); // #1B4965

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, deepBlue);
            Paragraph title = new Paragraph("Risum Odonto - Balanço Financeiro", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2.5f, 3f, 1.5f, 1.5f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
            String[] headers = {"Data", "Paciente", "Descrição", "Tipo", "Valor (R$)"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(deepBlue);
                cell.setPadding(8);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            double totalEntradas = 0;
            double totalSaidas = 0;
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Financeiro f : lancamentos) {
                table.addCell(new Phrase(f.getDataLancamento().format(formatter), dataFont));

                String nomePaciente = f.getPaciente() != null ? f.getPaciente().getNome() : "N/A";
                table.addCell(new Phrase(nomePaciente, dataFont));

                table.addCell(new Phrase(f.getDescricao(), dataFont));

                PdfPCell tipoCell = new PdfPCell(new Phrase(f.getTipo(), dataFont));
                tipoCell.setBackgroundColor(f.getTipo().equals("ENTRADA") ? new Color(232, 248, 245) : new Color(253, 237, 236));
                tipoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(tipoCell);

                PdfPCell valorCell = new PdfPCell(new Phrase(String.format("%.2f", f.getValor()), dataFont));
                valorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(valorCell);

                if (f.getTipo().equals("ENTRADA")) totalEntradas += f.getValor();
                else totalSaidas += f.getValor();
            }

            document.add(table);

            document.add(new Paragraph("\n\n"));
            Font resumenFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, deepBlue);

            Paragraph resumo = new Paragraph("Resumo do Caixa", resumenFont);
            resumo.setAlignment(Element.ALIGN_RIGHT);
            document.add(resumo);

            Font textResumo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph entradas = new Paragraph("Total de Entradas: R$ " + String.format("%.2f", totalEntradas), textResumo);
            entradas.setAlignment(Element.ALIGN_RIGHT);
            document.add(entradas);

            Paragraph saidas = new Paragraph("Total de Saídas: R$ " + String.format("%.2f", totalSaidas), textResumo);
            saidas.setAlignment(Element.ALIGN_RIGHT);
            document.add(saidas);

            Font saldoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, (totalEntradas - totalSaidas) >= 0 ? new Color(46, 204, 113) : Color.RED);
            Paragraph saldo = new Paragraph("Saldo Líquido: R$ " + String.format("%.2f", (totalEntradas - totalSaidas)), saldoFont);
            saldo.setAlignment(Element.ALIGN_RIGHT);
            document.add(saldo);

            document.close();

            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.setContentType(MediaType.APPLICATION_PDF);
            headersResponse.setContentDispositionFormData("filename", "relatorio_risum.pdf");

            return new ResponseEntity<>(baos.toByteArray(), headersResponse, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // DECLARAÇÃO DE COMPARECIMENTO

    @PostMapping("/declaracao")
    public ResponseEntity<byte[]> gerarDeclaracao(@RequestBody Map<String, String> dados) {
        try {
            Long pacienteId = Long.valueOf(String.valueOf(dados.get("pacienteId")));
            Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
            String nome = paciente != null ? paciente.getNome() : "____________________";

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Color deepBlue = new Color(27, 73, 101);
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, deepBlue);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("DECLARAÇÃO DE COMPARECIMENTO\n\n\n", fontTitulo);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            String dataFormatada = dados.get("data");
            try {
                LocalDate dt = LocalDate.parse(dados.get("data"));
                dataFormatada = dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception ignored) {}

            String textoCorpo = String.format(
                    "Declaro para os devidos fins de comprovação que o(a) paciente %s, inscrito(a) sob o " +
                            "CPF nº %s, compareceu a esta clínica odontológica no dia %s, permanecendo em consulta " +
                            "odontológica especializada no período das %s às %s.\n\n" +
                            "Por ser a expression da verdade, firmo a presente declaração.",
                    nome, (paciente != null ? paciente.getCpf() : "________"), dataFormatada, dados.get("horarioInicio"), dados.get("horarioFim")
            );

            Paragraph corpo = new Paragraph(textoCorpo, fontTexto);
            corpo.setAlignment(Element.ALIGN_JUSTIFIED);
            corpo.setLeading(22);
            document.add(corpo);

            Paragraph dataEmissao = new Paragraph("\n\n\n\nEmissão: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontTexto);
            dataEmissao.setAlignment(Element.ALIGN_RIGHT);
            document.add(dataEmissao);

            Paragraph assinatura = new Paragraph("\n\n\n\n\n___________________________________\nRisum Odonto - Carimbo e Assinatura", fontTexto);
            assinatura.setAlignment(Element.ALIGN_CENTER);
            document.add(assinatura);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    // RECEITA ODONTOLÓGICA

    @PostMapping("/receita-pdf")
    public ResponseEntity<byte[]> gerarReceitaPDF(@RequestBody Map<String, String> dados) {
        try {
            Long pacienteId = Long.valueOf(String.valueOf(dados.get("pacienteId")));
            Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
            String nome = paciente != null ? paciente.getNome() : "____________________";

            Document document = new Document(PageSize.A5);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Color deepBlue = new Color(27, 73, 101);
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, deepBlue);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, deepBlue);

            Paragraph title = new Paragraph("RECEITUÁRIO ODONTOLÓGICO\n\n", fontTitulo);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Paciente: " + nome, fontTexto));
            document.add(new Paragraph("Data de Emissão: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n", fontTexto));

            document.add(new Paragraph("PRESCRIÇÃO CLÍNICA:\n", fontSub));
            document.add(new Paragraph("Medicamento: " + dados.get("medicamento") + "\n\n", fontTexto));
            document.add(new Paragraph("Orientação de Posologia:\n" + dados.get("posologia") + "\n\n", fontTexto));

            Paragraph assinatura = new Paragraph("\n\n\n___________________________________\nAssinatura do Profissional Responsável", fontTexto);
            assinatura.setAlignment(Element.ALIGN_CENTER);
            document.add(assinatura);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    //  PRONTUÁRIO CLÍNICO

    @GetMapping("/prontuario/{id}")
    public ResponseEntity<byte[]> gerarProntuario(@PathVariable Long id) {
        try {
            Paciente paciente = pacienteRepository.findById(id).orElse(null);
            if (paciente == null) return ResponseEntity.notFound().build();


            List<Consulta> consultas = consultaRepository.findByPacienteId(id);

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Color deepBlue = new Color(27, 73, 101);
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, deepBlue);
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, deepBlue);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font fontHeaderTabela = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

            Paragraph title = new Paragraph("FICHA DE PRONTUÁRIO ODONTOLÓGICO\n\n\n", fontTitulo);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("DADOS CADASTRAIS DO PACIENTE:\n", fontSub));
            document.add(new Paragraph("Nome Completo: " + paciente.getNome(), fontTexto));
            document.add(new Paragraph("CPF: " + paciente.getCpf(), fontTexto));
            document.add(new Paragraph("Telefone de Contato: " + paciente.getTelefone(), fontTexto));
            document.add(new Paragraph("E-mail: " + paciente.getEmail(), fontTexto));
            document.add(new Paragraph("Status do Cadastro: " + (paciente.isAtivo() ? "ATIVO" : "INATIVADO"), fontTexto));

            if (!paciente.isAtivo() && paciente.getJustificativaStatus() != null) {
                document.add(new Paragraph("Motivo da Inativação: " + paciente.getJustificativaStatus(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, Color.RED)));
            }

            document.add(new Paragraph("\n\nHISTÓRICO CRONOLÓGICO DE ATENDIMENTOS:\n\n", fontSub));


            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 3.5f, 3f, 1.5f}); // Define a proporção das colunas

            String[] headersTabela = {"Data", "Procedimento", "Profissional", "Status"};
            for (String h : headersTabela) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeaderTabela));
                cell.setBackgroundColor(deepBlue);
                cell.setPadding(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            if (consultas.isEmpty()) {
                PdfPCell emptyCell = new PdfPCell(new Phrase("Nenhum registro de consulta localizado para este paciente.", fontTexto));
                emptyCell.setColspan(4);
                emptyCell.setPadding(10);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(emptyCell);
            } else {
                for (Consulta c : consultas) {
                    // Coluna Data
                    String dataStr = c.getDataInicio() != null ? c.getDataInicio().format(formatter) : "N/A";
                    table.addCell(new Phrase(dataStr, fontTexto));

                    // Coluna Procedimento
                    table.addCell(new Phrase(c.getDescricao() != null ? c.getDescricao() : "N/A", fontTexto));

                    // Coluna Profissional (Dentista)
                    String nomeDentista = c.getDentista() != null ? c.getDentista().getNome() : "Não Informado";
                    table.addCell(new Phrase(nomeDentista, fontTexto));

                    // Coluna Status
                    PdfPCell statusCell = new PdfPCell(new Phrase(c.getStatus(), fontTexto));
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(statusCell);
                }
            }

            document.add(table);

            Paragraph notaRodape = new Paragraph("\n\n\n\n* Nota Legal: O histórico cronológico detalhado contendo os procedimentos executados, " +
                    "evoluções clínicas, receitas médicas e agendamentos cancelados permanecem auditados e armazenados com segurança " +
                    "na base de dados digital da Risum Odonto.", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY));
            notaRodape.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(notaRodape);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "prontuario_" + id + ".pdf");
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}