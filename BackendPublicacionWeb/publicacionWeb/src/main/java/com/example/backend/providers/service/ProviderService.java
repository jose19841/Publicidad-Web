package com.example.backend.providers.service;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import com.example.backend.providers.service.usecase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderService {

    private final CreateProviderUsecase createProviderUsecase;
    private final GetAllProviderUsecase getAllProviderUsecase;
    private final UpdateProviderUsecase updateProviderUsecase;
    private final DisableProviderUsecase disableProviderUsecase;
    private final SearchProviderUsecase searchProviderService;
    private final EnableProviderUsecase enableProviderUsecase;

    public ProviderResponseDTO create(ProviderRequestDTO request, MultipartFile image) {
        log.info("→ [ProviderService] Creando proveedor nombre={} apellido={} categoríaId={}",
                request.getName(), request.getLastName(), request.getCategoryId());
        try {
            ProviderResponseDTO response = createProviderUsecase.create(request, image);
            log.info("✓ [ProviderService] Proveedor creado id={} nombre={} apellido={}",
                    response.getId(), response.getName(), response.getLastName());
            return response;
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al crear proveedor nombre={} apellido={}",
                    request.getName(), request.getLastName(), e);
            throw e;
        }
    }

    public List<ProviderResponseDTO> getAll() {
        log.info("→ [ProviderService] Solicitando listado de todos los proveedores");
        try {
            List<ProviderResponseDTO> providers = getAllProviderUsecase.getAll();
            log.info("✓ [ProviderService] Se recuperaron {} proveedores", providers.size());
            return providers;
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al obtener el listado de proveedores", e);
            throw e;
        }
    }

    public ProviderResponseDTO update(long id, ProviderUpdateRequestDTO request, MultipartFile image, String imageAction) {
        log.info("→ [ProviderService] Actualizando proveedor id={} con imageAction={}", id, imageAction);
        try {
            ProviderResponseDTO updated = updateProviderUsecase.update(id, request, image, imageAction);
            log.info("✓ [ProviderService] Proveedor actualizado id={} nombre={} apellido={}",
                    updated.getId(), updated.getName(), updated.getLastName());
            return updated;
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al actualizar proveedor id={}", id, e);
            throw e;
        }
    }

    public void disable(Long id) {
        log.info("→ [ProviderService] Inhabilitando proveedor id={}", id);
        try {
            disableProviderUsecase.disable(id);
            log.info("✓ [ProviderService] Proveedor inhabilitado id={}", id);
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al inhabilitar proveedor id={}", id, e);
            throw e;
        }
    }

    public void enable(Long id) {
        log.info("→ [ProviderService] Habilitando proveedor id={}", id);
        try {
            enableProviderUsecase.enable(id);
            log.info("✓ [ProviderService] Proveedor habilitado id={}", id);
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al habilitar proveedor id={}", id, e);
            throw e;
        }
    }

    public ProviderResponseDTO search(Long providerId) {
        log.info("→ [ProviderService] Buscando proveedor id={}", providerId);
        try {
            ProviderResponseDTO response = searchProviderService.execute(providerId);
            log.info("✓ [ProviderService] Proveedor encontrado id={} nombre={} apellido={}",
                    response.getId(), response.getName(), response.getLastName());
            return response;
        } catch (Exception e) {
            log.error("✗ [ProviderService] Error al buscar proveedor id={}", providerId, e);
            throw e;
        }
    }
}
