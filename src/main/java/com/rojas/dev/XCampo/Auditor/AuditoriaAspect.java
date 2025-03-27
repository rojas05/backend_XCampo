package com.rojas.dev.XCampo.Auditor;

import com.rojas.dev.XCampo.service.Interface.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaService auditoriaArchivoService;


    /**
     * Audita INSERT en la base de datos
     * @param joinPoint punto de union
     * @param result resultado del INSERT
     */
    @AfterReturning(value = "execution(* com.rojas.dev.XCampo.service.ServiceImp.*.insert*(..))", returning = "result")
    public void auditarInsert(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("El resultado de la operación es null, no se puede auditar.");
            return;
        }
        String usuario = getUserNameAuthenticate();
        String entidad = result.getClass().getSimpleName();

        auditoriaArchivoService.registerAudit(
                usuario,
                "CREADO",
                entidad,
                result.toString()
        );
    }

    /**
     * Audita INSERT en la base de datos
     * @param joinPoint punto de union
     * @param result resultado del INSERT
     */
    @AfterReturning(value = "execution(* com.rojas.dev.XCampo.service.ServiceImp.*.create*(..))", returning = "result")
    public void auditarCreate(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("El resultado de la operación es null, no se puede auditar.");
            return;
        }
        String usuario = getUserNameAuthenticate();
        String entidad = result.getClass().getSimpleName();

        auditoriaArchivoService.registerAudit(
                usuario,
                "CREADO",
                entidad,
                result.toString()
        );
    }

    /**
     * Audita INSERT en la base de datos
     * @param joinPoint punto de union
     * @param result resultado del INSERT
     */
    @AfterReturning(value = "execution(* com.rojas.dev.XCampo.service.ServiceImp.*.add*(..))", returning = "result")
    public void auditarAdd(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("El resultado de la operación es null, no se puede auditar.");
            return;
        }
        String usuario = getUserNameAuthenticate();
        String entidad = result.getClass().getSimpleName();

        auditoriaArchivoService.registerAudit(
                usuario,
                "CREADO",
                entidad,
                result.toString()
        );
    }

    /**
     * audita UPDATE en la base de datos
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "execution(* com.rojas.dev.XCampo.service.ServiceImp.*.update*(..))", returning = "result")
    public void auditarUpdate(JoinPoint joinPoint, Object result) {
        if (result == null) {
            log.error("El resultado de la operación es null, no se puede auditar.");
            return;
        }
        String usuario = getUserNameAuthenticate();
        String entidad = result.getClass().getSimpleName();
        Long id = getIdEntity(result);

        auditoriaArchivoService.registerAudit(
                usuario,
                "MODIFICADO",
                entidad,
                result.toString()
        );
    }

    /**
     * audita DELETE en la base de datos
     * @param joinPoint
     */
    @AfterReturning(value = "execution(* com.rojas.dev.XCampo.service.ServiceImp.*.delete*(..))", returning = "resul")
    public void auditarDelete(JoinPoint joinPoint) {
        String usuario = getUserNameAuthenticate();
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            String entidad = joinPoint.getSignature().getDeclaringTypeName();

            auditoriaArchivoService.registerAudit(
                    usuario,
                    "ELIMINADO",
                    entidad,
                    "Recurso eliminado"
            );
        }
    }

    /**
     * accede al context holder de spring security
     * @return userName autenticado en spring boot
     */
    private String getUserNameAuthenticate() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    private Long getIdEntity(Object entidad) {
        try {
            return (Long) entidad.getClass().getMethod("getId").invoke(entidad);
        } catch (Exception e) {
            return null;
        }
    }
}

