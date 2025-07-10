package com.microservicio.privilegio.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicio.privilegio.client.UsuarioClient;
import com.microservicio.privilegio.dto.PrivilegioDTO;
import com.microservicio.privilegio.dto.PrivilegioResumenDTO;
import com.microservicio.privilegio.model.Modulo;
import com.microservicio.privilegio.model.Privilegio;
import com.microservicio.privilegio.model.Usuario;
import com.microservicio.privilegio.repository.ModuloRepository;
import com.microservicio.privilegio.repository.PrivilegioRepository;

@Service
public class PrivilegioService {
    private final PrivilegioRepository privilegioRepository;
    private final ModuloRepository moduloRepository;
    private final UsuarioClient usuarioClient;

    public PrivilegioService(PrivilegioRepository privilegioRepository,
                             ModuloRepository moduloRepository,
                             UsuarioClient usuarioClient) {
        this.privilegioRepository = privilegioRepository;
        this.moduloRepository = moduloRepository;
        this.usuarioClient = usuarioClient;
    }

    private boolean esAdmin(Usuario usuario) {
        return usuario != null && usuario.getRol() != null &&
                "ADMIN".equalsIgnoreCase(usuario.getRol().getNombre());
    }

    public ResponseEntity<?> crearPrivilegio(PrivilegioDTO dto) {
        Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(dto.getNickname()).block();

        if (!esAdmin(usuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo ADMIN puede crear privilegios.");
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(dto.getModuloId());
        if (moduloOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Módulo no encontrado.");
        }

        Privilegio privilegio = new Privilegio();
        privilegio.setRol(dto.getRol());
        privilegio.setDescripcion(dto.getDescripcion());
        privilegio.setModulo(moduloOpt.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(privilegioRepository.save(privilegio));
    }

    public List<PrivilegioResumenDTO> listarPrivilegios() {
        return privilegioRepository.findAll().stream()
                .map(p -> new PrivilegioResumenDTO(p.getId(), p.getRol(), p.getDescripcion()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> actualizarPrivilegio(Long id, PrivilegioDTO dto) {
        Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(dto.getNickname()).block();

        if (!esAdmin(usuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo ADMIN puede actualizar privilegios.");
        }

        Optional<Privilegio> privilegioOpt = privilegioRepository.findById(id);
        if (privilegioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Privilegio no encontrado.");
        }

        Optional<Modulo> moduloOpt = moduloRepository.findById(dto.getModuloId());
        if (moduloOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Módulo no encontrado.");
        }

        Privilegio privilegio = privilegioOpt.get();
        privilegio.setRol(dto.getRol());
        privilegio.setDescripcion(dto.getDescripcion());
        privilegio.setModulo(moduloOpt.get());

        return ResponseEntity.ok(privilegioRepository.save(privilegio));
    }

    public ResponseEntity<?> eliminarPrivilegio(Long id, String nickname) {
        Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();

        if (!esAdmin(usuario)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo ADMIN puede eliminar privilegios.");
        }

        Optional<Privilegio> privilegioOpt = privilegioRepository.findById(id);
        if (privilegioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Privilegio no encontrado.");
        }

        privilegioRepository.deleteById(id);
        return ResponseEntity.ok("Privilegio eliminado con éxito.");
    }
  
}
