package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.service.Interface.AuditoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaServiceImp implements AuditoriaService {

    /**
     * LLamada al logger de logBack
     */
    private static final Logger AUDITORIA_LOGGER = LoggerFactory.getLogger("AuditLogger");

    /**
     * Registro de auditoria en un log
     * @param usuario
     * @param accion
     * @param entidad
     * @param detalles
     */
    @Override
    public void registerAudit(String usuario, String accion, String entidad, String detalles) {
        String registro = String.format(
                "Usuario: %s | Acción: %s | Entidad: %s | Detalles: %s",
                usuario, accion, entidad, detalles
        );
        AUDITORIA_LOGGER.info(registro);
    }
}
