package com.clinica.api.controller;

import com.clinica.api.dto.DadosAutenticacao;
import com.clinica.api.dto.DadosTokenJWT;
import com.clinica.api.model.Usuario;
import com.clinica.api.repository.DentistaRepository;
import com.clinica.api.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DentistaRepository dentistaRepository;

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        // Sucesso = pega o usuário logado
        var usuario = (Usuario) authentication.getPrincipal();

        //  Verifica se o perfil é DENTISTA e se ele está ativo na clínica
        if ("DENTISTA".equals(usuario.getPerfil())) {
            var dentista = dentistaRepository.findByEmail(usuario.getEmail()).orElse(null);
            if (dentista != null && !dentista.isAtivo()) {
                return ResponseEntity.status(403).body("Acesso bloqueado. Seu usuário está desativado. É necessário entrar em contato com o administrador do sistema.");
            }
        }


        var tokenJWT = tokenService.gerarToken(usuario);

        // Retorna Token, Perfil e Nome
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, usuario.getPerfil(), usuario.getNome()));
    }
}