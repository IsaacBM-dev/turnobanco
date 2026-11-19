package com.senati.turnobanco.service.impl;

import com.senati.turnobanco.dto.TurnoRequestDTO;
import com.senati.turnobanco.exception.BusinessValidationException;
import com.senati.turnobanco.exception.ResourceNotFoundException;
import com.senati.turnobanco.model.Tramite;
import com.senati.turnobanco.model.Turno;
import com.senati.turnobanco.repository.TramiteRepository;
import com.senati.turnobanco.repository.TurnoRepository;
import com.senati.turnobanco.service.TurnoService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final TramiteRepository tramiteRepository;

    public TurnoServiceImpl(TurnoRepository turnoRepository, TramiteRepository tramiteRepository) {
        this.turnoRepository = turnoRepository;
        this.tramiteRepository = tramiteRepository;
    }

    /**
     * =========================================================================
     * OPERACIÓN DE CÁLCULO #1:
     * Calcula el tiempo estimado de atención individual (en minutos)
     * Fórmula: TiempoBaseTramite * NumeroOperaciones * FactorPrioridadTramite
     * =========================================================================
     */
    private BigDecimal calcularTiempoEstimadoAtencion(Tramite tramite, Integer numeroOperaciones) {
        BigDecimal tiempoBase = BigDecimal.valueOf(tramite.getTiempoBaseMin());
        BigDecimal operaciones = BigDecimal.valueOf(numeroOperaciones);
        BigDecimal factor = tramite.getFactorPrioridad();

        return tiempoBase.multiply(operaciones)
                .multiply(factor)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * =========================================================================
     * OPERACIÓN DE CÁLCULO #2:
     * Calcula el tiempo estimado de espera en la cola (en minutos)
     * Si el turno está "EN ESPERA", cuenta cuántos turnos están esperando en el
     * mismo trámite y lo multiplica por el tiempo base del trámite.
     * Si ya está "EN ATENCION" o "ATENDIDO", el tiempo de espera restante es 0.00.
     * =========================================================================
     */
    private BigDecimal calcularTiempoEsperaCola(Tramite tramite, String estado) {
        if (!"EN ESPERA".equalsIgnoreCase(estado)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        long turnosEnEspera = turnoRepository.countByTramite_IdTramiteAndEstadoIgnoreCase(
                tramite.getIdTramite(), "EN ESPERA"
        );
        BigDecimal cantidadEnFila = BigDecimal.valueOf(turnosEnEspera);
        BigDecimal tiempoBase = BigDecimal.valueOf(tramite.getTiempoBaseMin());

        return cantidadEnFila.multiply(tiempoBase)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public Turno registrarTurno(TurnoRequestDTO request) {
        if (turnoRepository.existsByCodigoTurnoIgnoreCase(request.getCodigoTurno())) {
            throw new BusinessValidationException("codigoTurno",
                    "Ya existe un turno registrado con el código: " + request.getCodigoTurno());
        }

        Tramite tramite = tramiteRepository.findById(request.getIdTramite())
                .orElseThrow(() -> new BusinessValidationException("idTramite",
                        "No existe el trámite bancario con ID: " + request.getIdTramite()));

        // Ejecutar las 2 operaciones de cálculo
        BigDecimal tiempoAtencion = calcularTiempoEstimadoAtencion(tramite, request.getNumeroOperaciones());
        BigDecimal tiempoEspera = calcularTiempoEsperaCola(tramite, request.getEstado());

        Turno nuevoTurno = Turno.builder()
                .codigoTurno(request.getCodigoTurno().toUpperCase())
                .dni(request.getDni())
                .nombres(request.getNombres().trim())
                .apellidos(request.getApellidos().trim())
                .numeroOperaciones(request.getNumeroOperaciones())
                .estado(request.getEstado().toUpperCase())
                .tramite(tramite)
                .tiempoEstimadoAtencionMin(tiempoAtencion)
                .tiempoEsperaColaMin(tiempoEspera)
                .fechaRegistro(LocalDateTime.now())
                .build();

        return turnoRepository.save(nuevoTurno);
    }

    @Override
    @Transactional
    public Turno actualizarPorId(Integer idTurno, TurnoRequestDTO request) {
        Turno turnoExistente = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("idTurno",
                        "No se encontró el turno con ID: " + idTurno));

        // Validar si cambió el código de turno y ya existe en otro registro
        if (!turnoExistente.getCodigoTurno().equalsIgnoreCase(request.getCodigoTurno())
                && turnoRepository.existsByCodigoTurnoIgnoreCase(request.getCodigoTurno())) {
            throw new BusinessValidationException("codigoTurno",
                    "El código de turno " + request.getCodigoTurno() + " ya está en uso por otro cliente.");
        }

        Tramite tramite = tramiteRepository.findById(request.getIdTramite())
                .orElseThrow(() -> new BusinessValidationException("idTramite",
                        "No existe el trámite bancario con ID: " + request.getIdTramite()));

        // Recalcular tiempos automáticamente al actualizar
        BigDecimal tiempoAtencion = calcularTiempoEstimadoAtencion(tramite, request.getNumeroOperaciones());
        BigDecimal tiempoEspera = calcularTiempoEsperaCola(tramite, request.getEstado());

        turnoExistente.setCodigoTurno(request.getCodigoTurno().toUpperCase());
        turnoExistente.setDni(request.getDni());
        turnoExistente.setNombres(request.getNombres().trim());
        turnoExistente.setApellidos(request.getApellidos().trim());
        turnoExistente.setNumeroOperaciones(request.getNumeroOperaciones());
        turnoExistente.setEstado(request.getEstado().toUpperCase());
        turnoExistente.setTramite(tramite);
        turnoExistente.setTiempoEstimadoAtencionMin(tiempoAtencion);
        turnoExistente.setTiempoEsperaColaMin(tiempoEspera);

        return turnoRepository.save(turnoExistente);
    }

    @Override
    @Transactional
    public Turno actualizarPorCodigoTurno(String codigoTurno, TurnoRequestDTO request) {
        Turno turnoExistente = turnoRepository.findByCodigoTurnoIgnoreCase(codigoTurno)
                .orElseThrow(() -> new ResourceNotFoundException("codigoTurno",
                        "No se encontró ningún turno con el código: " + codigoTurno));

        return actualizarPorId(turnoExistente.getIdTurno(), request);
    }

    @Override
    @Transactional
    public Turno eliminarPorId(Integer idTurno) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new ResourceNotFoundException("idTurno",
                        "No existe el turno con ID: " + idTurno + " para eliminar."));
        turnoRepository.delete(turno);
        return turno;
    }

    @Override
    @Transactional
    public Turno eliminarPorCodigoTurno(String codigoTurno) {
        Turno turno = turnoRepository.findByCodigoTurnoIgnoreCase(codigoTurno)
                .orElseThrow(() -> new ResourceNotFoundException("codigoTurno",
                        "No existe el turno con código: " + codigoTurno + " para eliminar."));
        turnoRepository.delete(turno);
        return turno;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> listarTodos() {
        return turnoRepository.findAll(Sort.by(Sort.Direction.DESC, "idTurno"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> listarOrdenadosPorCampo(String campoOrden) {
        if ("codigoTurno".equalsIgnoreCase(campoOrden)) {
            return turnoRepository.findAllByOrderByCodigoTurnoAsc();
        } else if ("tiempoEspera".equalsIgnoreCase(campoOrden)) {
            return turnoRepository.findAll(Sort.by(Sort.Direction.DESC, "tiempoEsperaColaMin"));
        }
        // Por defecto ordena alfabéticamente por Apellidos y Nombres (distinto a Id)
        return turnoRepository.findAllByOrderByApellidosAscNombresAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> buscarPorCodigoCoincidencia(String codigoTurno) {
        return turnoRepository.findByCodigoTurnoContainingIgnoreCaseOrderByIdTurnoDesc(codigoTurno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> buscarPorNombresApellidosCodigo(String nombres, String apellidos, String codigoTurno) {
        return turnoRepository.buscarPorFiltroMulticampo(nombres, apellidos, codigoTurno);
    }
}

