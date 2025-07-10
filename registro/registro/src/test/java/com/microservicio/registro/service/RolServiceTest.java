package com.microservicio.registro.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservicio.registro.model.Rol;
import com.microservicio.registro.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {
     @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void listarRoles_deberiaRetornarTodosLosRoles() {
        List<Rol> rolesSimulados = Arrays.asList(
                new Rol(1L, "CLIENTE"),
                new Rol(2L, "LOGISTICA"),
                new Rol(3L, "GERENTE"),
                new Rol(4L, "ADMIN")
        );

        when(rolRepository.findAll()).thenReturn(rolesSimulados);

        List<Rol> resultado = rolService.listarRoles();

        assertThat(resultado).isEqualTo(rolesSimulados);
    }

    @Test
    void obtenerPorNombre_deberiaRetornarRolSiExiste() {
        Rol admin = new Rol(4L, "ADMIN");
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(admin));

        Optional<Rol> resultado = rolService.obtenerPorNombre("ADMIN");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("ADMIN");
    }

    @Test
    void obtenerPorId_deberiaRetornarRolSiExiste() {
        Rol logistica = new Rol(2L, "LOGISTICA");
        when(rolRepository.findById(2L)).thenReturn(Optional.of(logistica));

        Optional<Rol> resultado = rolService.obtenerPorId(2L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("LOGISTICA");
    }

    @Test
    void guardarRol_deberiaGuardarYRetornarRol() {
        Rol nuevoRol = new Rol(null, "GERENTE");
        Rol guardado = new Rol(3L, "GERENTE");

        when(rolRepository.save(nuevoRol)).thenReturn(guardado);

        Rol resultado = rolService.guardarRol(nuevoRol);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("GERENTE");
    }
     

}
