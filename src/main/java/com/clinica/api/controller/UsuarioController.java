package com.clinica.api.controller;

import com.clinica.api.model.Usuario;
import com.clinica.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Page<Usuario>> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(repository.findAll(paginacao));
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuario.setAtivo(true);
        return ResponseEntity.ok(repository.save(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario existente = repository.findById(id).orElseThrow();

        existente.setNome(usuario.getNome());
        existente.setEmail(usuario.getEmail());
        existente.setPerfil(usuario.getPerfil());

        if (usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty()) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return ResponseEntity.ok(repository.save(existente));
    }
}