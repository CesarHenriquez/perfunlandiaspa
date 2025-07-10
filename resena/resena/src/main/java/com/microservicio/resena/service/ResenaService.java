package com.microservicio.resena.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicio.resena.client.ProductoClient;
import com.microservicio.resena.client.UsuarioClient;
import com.microservicio.resena.model.Producto;
import com.microservicio.resena.model.Resena;
import com.microservicio.resena.model.Usuario;
import com.microservicio.resena.repository.ResenaRepository;

@Service
public class ResenaService {
    private final ResenaRepository resenaRepository;
    private final ProductoClient productoClient;
    private final UsuarioClient usuarioClient;

    public ResenaService(
            ResenaRepository resenaRepository,
            ProductoClient productoClient,
            UsuarioClient usuarioClient) {
        this.resenaRepository = resenaRepository;
        this.productoClient = productoClient;
        this.usuarioClient = usuarioClient;
    }

    public ResponseEntity<?> guardarResenaDesdePayload(Map<String, Object> payload) {
        try {
            String nickname = (String) payload.get("nickname");
            String clave = (String) payload.get("clave");

            Usuario usuario = usuarioClient.obtenerUsuarioPorNickname(nickname).block();
            if (usuario == null || usuario.getRol() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario inválido o sin rol.");
            }

            if (!usuarioClient.validarClave(clave, usuario.getClave())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clave incorrecta.");
            }

            String rol = usuario.getRol().getNombre().toUpperCase();
            if (!rol.equals("CLIENTE") && !rol.equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo CLIENTE o ADMIN pueden publicar reseñas.");
            }

            Long productoId = Long.valueOf(payload.get("productoId").toString());
            Producto producto = productoClient.obtenerProductoPorId(productoId).block();
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El producto con ID " + productoId + " no existe.");
            }

            Resena resena = new Resena();
            resena.setProductoId(productoId);
            resena.setUsuarioId(usuario.getId());
            resena.setComentario((String) payload.get("comentario"));
            resena.setCalificacion(Integer.valueOf(payload.get("puntuacion").toString())); 

            Resena guardada = resenaRepository.save(resena);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar reseña: " + e.getMessage());
        }
    }

    public List<Resena> listarResenas() {
        return resenaRepository.findAll();
    }

    public List<Resena> listarPorProductoId(Long productoId) {
        return resenaRepository.findByProductoId(productoId);
    }

    public List<Resena> listarPorUsuarioId(Long usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId);
    }

}
