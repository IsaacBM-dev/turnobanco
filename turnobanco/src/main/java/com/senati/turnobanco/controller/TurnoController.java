package com.senati.turnobanco.controller;

import com.senati.turnobanco.model.Turno;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private List<Turno> listaTurnos = new ArrayList<>();
    private Long contadorId = 1L;

    private boolean datosSonValidos(Turno turno) {
        return turno.getCodigo() != null && !turno.getCodigo().trim().isEmpty()
                && turno.getCliente() != null && !turno.getCliente().trim().isEmpty()
                && turno.getTipoOperacion() != null && !turno.getTipoOperacion().trim().isEmpty()
                && turno.getEstado() != null && !turno.getEstado().trim().isEmpty();
    }

    // registrar un nuevo turno
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Turno nuevoTurno) {
        if (!datosSonValidos(nuevoTurno)) {
            return ResponseEntity.badRequest().body("Error: todos los campos del turno son obligatorios");
        }

        nuevoTurno.setId(contadorId++);
        listaTurnos.add(nuevoTurno);
        return ResponseEntity.ok(nuevoTurno);
    }

    // Listar turnos
    @GetMapping
    public List<Turno> listarTodos() {
        return listaTurnos;
    }

    // Obtener un turno especifico
    @GetMapping("/{id}")
    public ResponseEntity<Turno> obtenerPorId(@PathVariable Long id) {
        return listaTurnos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // modificar turno existente por id
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Turno turnoActualizado) {
        if (!datosSonValidos(turnoActualizado)) {
            return ResponseEntity.badRequest().body("Error: todos los campos del turno son obligatorios");
        }

        for (Turno t : listaTurnos) {
            if (t.getId().equals(id)) {
                t.setCodigo(turnoActualizado.getCodigo());
                t.setCliente(turnoActualizado.getCliente());
                t.setTipoOperacion(turnoActualizado.getTipoOperacion());
                t.setEstado(turnoActualizado.getEstado());
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Quitar un turno por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = listaTurnos.removeIf(t -> t.getId().equals(id));
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

