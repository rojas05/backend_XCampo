package com.rojas.dev.XCampo.service.Interface;

public interface AuditoriaService {
    void registerAudit(String usuario, String accion, String entidad, String detalles);
}
