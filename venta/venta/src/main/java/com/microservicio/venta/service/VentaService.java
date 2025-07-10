package com.microservicio.venta.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservicio.venta.client.DireccionClient;
import com.microservicio.venta.client.ProductoClient;
import com.microservicio.venta.client.UsuarioClient;
import com.microservicio.venta.dto.ProductoDetalleDTO;
import com.microservicio.venta.model.DetalleVenta;

import com.microservicio.venta.model.Producto;
import com.microservicio.venta.model.Usuario;
import com.microservicio.venta.model.Venta;
import com.microservicio.venta.repository.DetalleVentaRepository;
import com.microservicio.venta.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoClient productoClient;
    private final UsuarioClient usuarioClient;
    private final DireccionClient direccionClient;

    public VentaService(VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            ProductoClient productoClient,
            UsuarioClient usuarioClient,
            DireccionClient direccionClient) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoClient = productoClient;
        this.usuarioClient = usuarioClient;
        this.direccionClient = direccionClient;
    }

    @Transactional
    public ResponseEntity<?> registrarVentaDesdePayload(Map<String, Object> payload) {
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

            if (!usuario.getRol().getNombre().equalsIgnoreCase("CLIENTE")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo CLIENTES pueden realizar ventas.");
            }

            Venta venta = new Venta();
            venta.setUsuarioId(usuario.getId());
            venta.setDireccionId(Long.valueOf(payload.get("direccionId").toString()));
            venta.setFecha(LocalDate.now());

            Venta ventaGuardada = ventaRepository.save(venta);

            List<Map<String, Object>> detalles = (List<Map<String, Object>>) payload.get("detalles");
            List<DetalleVenta> detallesGuardados = new ArrayList<>();

            for (Map<String, Object> detalleMap : detalles) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(ventaGuardada);
                Long productoId = Long.valueOf(detalleMap.get("productoId").toString());
                Integer cantidad = Integer.valueOf(detalleMap.get("cantidad").toString());

                Producto producto = productoClient.obtenerProductoPorId(productoId).block();
                if (producto == null)
                    continue;

                detalle.setProductoId(productoId);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(producto.getPrecio());

                DetalleVenta detalleGuardado = detalleVentaRepository.save(detalle);
                detallesGuardados.add(detalleGuardado);
            }

            ventaGuardada.setDetalles(detallesGuardados);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaGuardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la venta: " + e.getMessage());
        }
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public List<DetalleVenta> listarDetalles() {
        return detalleVentaRepository.findAll();
    }

    public List<Venta> listarPorUsuarioId(Long idUsuario) {
        return ventaRepository.findByUsuarioId(idUsuario);
    }

    public List<Venta> listarPorDireccionId(Long direccionId) {
        return ventaRepository.findByDireccionId(direccionId);
    }

    public List<ProductoDetalleDTO> obtenerProductosPorVentaId(Long ventaId) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);
        List<ProductoDetalleDTO> productos = new ArrayList<>();

        for (DetalleVenta detalle : detalles) {
            Producto producto = productoClient.obtenerProductoPorId(detalle.getProductoId()).block();
            if (producto != null) {
                ProductoDetalleDTO dto = new ProductoDetalleDTO();
                dto.setProductoId(producto.getId());
                dto.setNombreProducto(producto.getNombre());
                dto.setCantidad(detalle.getCantidad());
                dto.setPrecioUnitario(detalle.getPrecioUnitario());
                productos.add(dto);
            }
        }

        return productos;
    }

}
