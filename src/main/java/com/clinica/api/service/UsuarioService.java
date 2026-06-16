package com.clinica.api.service;

import com.clinica.api.model.Usuario;
import com.clinica.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario salvar(Usuario u) {
        u.setSenha(encoder.encode(u.getSenha()));
        u.setAtivo(true);
        return repository.save(u);
    }

    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario u = repository.findById(id).orElseThrow();
        u.setNome(dadosNovos.getNome());
        u.setEmail(dadosNovos.getEmail());
        u.setPerfil(dadosNovos.getPerfil());
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isEmpty()) {
            u.setSenha(encoder.encode(dadosNovos.getSenha()));
        }
        return repository.save(u);
    }
}