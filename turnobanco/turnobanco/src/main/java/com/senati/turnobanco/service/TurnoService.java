package com.senati.turnobanco.service;

import com.senati.turnobanco.dto.TurnoRequestDTO;
import com.senati.turnobanco.model.Turno;

import java.util.List;

public interface TurnoService {
    // 1. Registrar
    Turno registrarTurno(TurnoRequestDTO request);

    // 2. Actualizar por Id
    Turno actualizarPorId(Integer idTurno, TurnoRequestDTO request);

    // 3. Actualizar por otro campo (codigoTurno)
    Turno actualizarPorCodigoTurno(String codigoTurno, TurnoRequestDTO request);

    // 4. Eliminar por Id
    Turno eliminarPorId(Integer idTurno);

    // 5. Eliminar por otro campo (codigoTurno)
    Turno eliminarPorCodigoTurno(String codigoTurno);

    // 6. Listar todos los registros
    List<Turno> listarTodos();

    // 7. Listar todos los registros ordenados por campo distinto a Id
    List<Turno> listarOrdenadosPorCampo(String campoOrden);

    // 8. Buscar por un campo y listar por coincidencia
    List<Turno> buscarPorCodigoCoincidencia(String codigoTurno);

    // 9. Buscar por dos o más campos y listar por coincidencia (nombres, apellidos, codigoTurno)
    List<Turno> buscarPorNombresApellidosCodigo(String nombres, String apellidos, String codigoTurno);
}

