package com.clinica.api.config;

import com.clinica.api.model.Dentista;
import com.clinica.api.model.Especialidade;
import com.clinica.api.model.Paciente;
import com.clinica.api.model.Usuario;
import com.clinica.api.repository.DentistaRepository;
import com.clinica.api.repository.EspecialidadeRepository;
import com.clinica.api.repository.PacienteRepository;
import com.clinica.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

 @Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        // ==========================================
        // 1. POPULAR ESPECIALIDADES
        // ==========================================
        List<String> nomesEspecialidades = Arrays.asList(
                "Clínica Geral", "Dentística", "Ortodontia", "Implantodontia",
                "Endodontia", "Periodontia", "Cirurgia e Traumatologia",
                "Odontopediatria", "Prótese Dentária", "Harmonização Orofacial", "Radiologia"
        );

        for (String nome : nomesEspecialidades) {
            if (!especialidadeRepository.existsByNomeIgnoreCase(nome)) {
                Especialidade esp = new Especialidade();
                esp.setNome(nome);
                especialidadeRepository.save(esp);
            }
        }

        // ==========================================
        // 2. POPULAR OS 10 DENTISTAS OFICIAIS
        // ==========================================
        if (dentistaRepository.count() == 0) {
            Especialidade ortodontia = especialidadeRepository.findByNome("Ortodontia").orElse(null);
            Especialidade endodontia = especialidadeRepository.findByNome("Endodontia").orElse(null);
            Especialidade implantodontia = especialidadeRepository.findByNome("Implantodontia").orElse(null);

            // 1. Dr. Rodrigo Mendes
            Dentista d1 = new Dentista();
            d1.setNome("Dr. Rodrigo Mendes");
            d1.setCro("CRO-PR 12345");
            d1.setCpf("111.111.111-11");
            d1.setEmail("rodrigo@risum.com");
            if (ortodontia != null && implantodontia != null) d1.setEspecialidades(Set.of(ortodontia, implantodontia));
            dentistaRepository.save(d1);

            // 2. Dra. Amanda Silva
            Dentista d2 = new Dentista();
            d2.setNome("Dra. Amanda Silva");
            d2.setCro("CRO-PR 67890");
            d2.setCpf("222.222.222-22");
            d2.setEmail("amanda@risum.com");
            if (endodontia != null) d2.setEspecialidades(Set.of(endodontia));
            dentistaRepository.save(d2);

            // 3. Dr. Bruno Costa
            Dentista d3 = new Dentista();
            d3.setNome("Dr. Bruno Costa");
            d3.setCro("CRO-PR 54321");
            d3.setCpf("333.333.333-33");
            d3.setEmail("bruno@risum.com");
            if (implantodontia != null) d3.setEspecialidades(Set.of(implantodontia));
            dentistaRepository.save(d3);

            // 4. Dra. Camila Oliveira
            Dentista d4 = new Dentista();
            d4.setNome("Dra. Camila Oliveira");
            d4.setCro("CRO-PR 11223");
            d4.setCpf("444.444.444-44");
            d4.setEmail("camila@risum.com");
            if (ortodontia != null) d4.setEspecialidades(Set.of(ortodontia));
            dentistaRepository.save(d4);

            // 5. Dr. Diego Santos
            Dentista d5 = new Dentista();
            d5.setNome("Dr. Diego Santos");
            d5.setCro("CRO-PR 44556");
            d5.setCpf("555.555.555-55");
            d5.setEmail("diego@risum.com");
            if (endodontia != null) d5.setEspecialidades(Set.of(endodontia));
            dentistaRepository.save(d5);

            // 6. Dra. Elena Ribeiro
            Dentista d6 = new Dentista();
            d6.setNome("Dra. Elena Ribeiro");
            d6.setCro("CRO-PR 77889");
            d6.setCpf("666.666.666-66");
            d6.setEmail("elena@risum.com");
            if (ortodontia != null && endodontia != null) d6.setEspecialidades(Set.of(ortodontia, endodontia));
            dentistaRepository.save(d6);

            // 7. Dr. Fábio Lima
            Dentista d7 = new Dentista();
            d7.setNome("Dr. Fábio Lima");
            d7.setCro("CRO-PR 99001");
            d7.setCpf("777.777.777-77");
            d7.setEmail("fabio@risum.com");
            if (implantodontia != null) d7.setEspecialidades(Set.of(implantodontia));
            dentistaRepository.save(d7);

            // 8. Dra. Gisele Almeida
            Dentista d8 = new Dentista();
            d8.setNome("Dra. Gisele Almeida");
            d8.setCro("CRO-PR 22334");
            d8.setCpf("888.888.888-88");
            d8.setEmail("gisele@risum.com");
            if (endodontia != null) d8.setEspecialidades(Set.of(endodontia));
            dentistaRepository.save(d8);

            // 9. Dr. Heitor Souza
            Dentista d9 = new Dentista();
            d9.setNome("Dr. Heitor Souza");
            d9.setCro("CRO-PR 55667");
            d9.setCpf("999.999.999-91");
            d9.setEmail("heitor@risum.com");
            if (ortodontia != null) d9.setEspecialidades(Set.of(ortodontia));
            dentistaRepository.save(d9);

            // 10. Dra. Isabela Rocha
            Dentista d10 = new Dentista();
            d10.setNome("Dra. Isabela Rocha");
            d10.setCro("CRO-PR 88990");
            d10.setCpf("123.123.123-12");
            d10.setEmail("isabela@risum.com");
            if (implantodontia != null && ortodontia != null) d10.setEspecialidades(Set.of(implantodontia, ortodontia));
            dentistaRepository.save(d10);
        }

        // ==========================================
        // 3. POPULAR PACIENTES
        // ==========================================
        if (pacienteRepository.count() == 0) {
            String[][] dadosPacientes = {
                    {"Ana Beatriz Rocha", "001.002.003-04", "(11) 98888-1111", "ana.rocha@gmail.com"},
                    {"Bruno Henrique Alves", "010.020.030-40", "(11) 98888-2222", "bruno.alves@gmail.com"},
                    {"Camila Fernandes Lima", "100.200.300-40", "(11) 98888-3333", "camila.lima@gmail.com"},
                    {"Daniel Borges", "111.222.333-44", "(11) 98888-4444", "daniel.borges@yahoo.com"},
                    {"Eduardo Correia", "222.333.444-55", "(11) 98888-5555", "eduardo.correia@hotmail.com"},
                    {"Flávia Martins", "333.444.555-66", "(11) 98888-6666", "flavia.martins@gmail.com"},
                    {"Gabriel Monteiro", "444.555.666-77", "(11) 98888-7777", "gabriel.monteiro@gmail.com"},
                    {"Helena Teixeira", "555.666.777-88", "(11) 98888-8888", "helena.teixeira@outlook.com"},
                    {"Igor Ribeiro", "666.777.888-99", "(11) 98888-9999", "igor.ribeiro@gmail.com"},
                    {"Júlia Carvalho", "777.888.999-00", "(11) 98888-0000", "julia.carvalho@gmail.com"}
            };

            for (String[] pData : dadosPacientes) {
                Paciente p = new Paciente();
                p.setNome(pData[0]);
                p.setCpf(pData[1]);
                p.setTelefone(pData[2]);
                p.setEmail(pData[3]);
                p.setAtivo(true);
                pacienteRepository.save(p);
            }
        }

        // ==========================================
        // 4. POPULAR USUÁRIOS COM SENHA CRIPTOGRAFADA
        // ==========================================
        if (usuarioRepository.count() == 0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String senhaPadrao = encoder.encode("admin123"); // Senha padronizada com o SQL

            // 4.1 Usuário Master (Admin)
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@risum.com");
            admin.setSenha(senhaPadrao);
            admin.setPerfil("ADMIN");
            admin.setAtivo(true);
            admin.setCpf("000.000.000-00");
            usuarioRepository.save(admin);

            // 4.2 Usuário da Recepção
            Usuario recepcao = new Usuario();
            recepcao.setNome("Recepção Principal");
            recepcao.setEmail("recepcao@risum.com");
            recepcao.setSenha(senhaPadrao);
            recepcao.setPerfil("RECEPCAO");
            recepcao.setAtivo(true);
            recepcao.setCpf("111.222.333-00");
            usuarioRepository.save(recepcao);

            // 4.3 Criação automática dos Usuários de Login para os 10 Dentistas Oficiais
            String[] emailsDentistas = {
                    "rodrigo@risum.com", "amanda@risum.com", "bruno@risum.com",
                    "camila@risum.com", "diego@risum.com", "elena@risum.com",
                    "fabio@risum.com", "gisele@risum.com", "heitor@risum.com",
                    "isabela@risum.com"
            };

            String[] cpfsDentistas = {
                    "111.111.111-11", "222.222.222-22", "333.333.333-33",
                    "444.444.444-44", "555.555.555-55", "666.666.666-66",
                    "777.777.777-77", "888.888.888-88", "999.999.999-99",
                    "123.123.123-12"
            };

            for (int i = 0; i < emailsDentistas.length; i++) {
                String email = emailsDentistas[i];
                String cpf = cpfsDentistas[i];

                Usuario dentistaUser = new Usuario();
                String nomeFormatado = email.split("@")[0];
                nomeFormatado = nomeFormatado.substring(0, 1).toUpperCase() + nomeFormatado.substring(1);

                dentistaUser.setNome("Dr(a). " + nomeFormatado);
                dentistaUser.setEmail(email);
                dentistaUser.setSenha(senhaPadrao);
                dentistaUser.setPerfil("DENTISTA");
                dentistaUser.setAtivo(true);
                dentistaUser.setCpf(cpf);
                usuarioRepository.save(dentistaUser);
            }
        }
    }
}