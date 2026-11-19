package com.senati.turnobanco.controller;

import com.senati.turnobanco.dto.ListResponseDTO;
import com.senati.turnobanco.dto.SingleResponseDTO;
import com.senati.turnobanco.dto.TurnoRequestDTO;
import com.senati.turnobanco.model.Turno;
import com.senati.turnobanco.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "*")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // 1. REGISTRAR TURNO (Retorna Caso 01 - 200 OK o Caso 03 - 400 Bad Request)
    @PostMapping
    public ResponseEntity<SingleResponseDTO<Turno>> registrar(@Valid @RequestBody TurnoRequestDTO request) {
        Turno creado = turnoService.registrarTurno(request);
        return ResponseEntity.ok(SingleResponseDTO.success(creado));
    }

    // 2. ACTUALIZAR POR ID (Retorna Caso 01 - 200 OK)
    @PutMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<Turno>> actualizarPorId(
            @PathVariable("id") Integer id,
            @Valid @RequestBody TurnoRequestDTO request) {
        Turno actualizado = turnoService.actualizarPorId(id, request);
        return ResponseEntity.ok(SingleResponseDTO.success(actualizado));
    }

    // 3. ACTUALIZAR POR OTRO CAMPO: CODIGO DE TURNO (Retorna Caso 01 - 200 OK)
    @PutMapping("/codigo/{codigoTurno}")
    public ResponseEntity<SingleResponseDTO<Turno>> actualizarPorCodigo(
            @PathVariable("codigoTurno") String codigoTurno,
            @Valid @RequestBody TurnoRequestDTO request) {
        Turno actualizado = turnoService.actualizarPorCodigoTurno(codigoTurno, request);
        return ResponseEntity.ok(SingleResponseDTO.success(actualizado));
    }

    // 4. ELIMINAR POR ID (Retorna Caso 01 - 200 OK con el turno eliminado)
    @DeleteMapping("/{id}")
    public ResponseEntity<SingleResponseDTO<Turno>> eliminarPorId(@PathVariable("id") Integer id) {
        Turno eliminado = turnoService.eliminarPorId(id);
        return ResponseEntity.ok(SingleResponseDTO.success(eliminado));
    }

    // 5. ELIMINAR POR OTRO CAMPO: CODIGO DE TURNO (Retorna Caso 01 - 200 OK)
    @DeleteMapping("/codigo/{codigoTurno}")
    public ResponseEntity<SingleResponseDTO<Turno>> eliminarPorCodigo(
            @PathVariable("codigoTurno") String codigoTurno) {
        Turno eliminado = turnoService.eliminarPorCodigoTurno(codigoTurno);
        return ResponseEntity.ok(SingleResponseDTO.success(eliminado));
    }

    // 6. LISTAR TODOS LOS REGISTROS (Retorna Caso 02 - 200 OK con meta.total_registros)
    @GetMapping
    public ResponseEntity<ListResponseDTO<Turno>> listarTodos() {
        List<Turno> lista = turnoService.listarTodos();
        return ResponseEntity.ok(ListResponseDTO.success(lista));
    }

    // 7. LISTAR TODOS LOS REGISTROS ORDENADOS POR CAMPO DISTINTO A ID (apellidos, codigoTurno, tiempoEspera)
    @GetMapping("/ordenados")
    public ResponseEntity<ListResponseDTO<Turno>> listarOrdenados(
            @RequestParam(name = "campo", defaultValue = "apellidos") String campo) {
        List<Turno> lista = turnoService.listarOrdenadosPorCampo(campo);
        return ResponseEntity.ok(ListResponseDTO.success(lista));
    }

    // 8. BUSCAR POR UN CAMPO Y LISTAR POR COINCIDENCIA (Por codigoTurno)
    @GetMapping("/buscar-codigo")
    public ResponseEntity<ListResponseDTO<Turno>> buscarPorCodigo(
            @RequestParam(name = "codigo", defaultValue = "") String codigo) {
        List<Turno> lista = turnoService.buscarPorCodigoCoincidencia(codigo);
        return ResponseEntity.ok(ListResponseDTO.success(lista));
    }

    // 9. BUSCAR POR 2 O MÁS CAMPOS Y LISTAR POR COINCIDENCIA (Nombres, Apellidos y Código de Turno)
    @GetMapping("/filtrar")
    public ResponseEntity<ListResponseDTO<Turno>> filtrarTurnos(
            @RequestParam(name = "nombres", required = false, defaultValue = "") String nombres,
            @RequestParam(name = "apellidos", required = false, defaultValue = "") String apellidos,
            @RequestParam(name = "codigoTurno", required = false, defaultValue = "") String codigoTurno) {
        List<Turno> lista = turnoService.buscarPorNombresApellidosCodigo(nombres, apellidos, codigoTurno);
        return ResponseEntity.ok(ListResponseDTO.success(lista));
    }
}

