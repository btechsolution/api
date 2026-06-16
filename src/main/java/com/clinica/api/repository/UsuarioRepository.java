package com.clinica.api.repository;

import com.clinica.api.model.Usuario;
import com.clinica.api.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    List<Usuario> findByPerfil(Perfil perfil);
}