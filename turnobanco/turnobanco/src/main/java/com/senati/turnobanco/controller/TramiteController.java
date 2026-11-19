package com.senati.turnobanco.controller;

import com.senati.turnobanco.dto.ListResponseDTO;
import com.senati.turnobanco.model.Tramite;
import com.senati.turnobanco.repository.TramiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramites")
@CrossOrigin(origins = "*")
public class TramiteController {

    private final TramiteRepository tramiteRepository;

    public TramiteController(TramiteRepository tramiteRepository) {
        this.tramiteRepository = tramiteRepository;
    }

    @GetMapping
    public ResponseEntity<ListResponseDTO<Tramite>> listarTramites() {
        List<Tramite> tramites = tramiteRepository.findAll();
        return ResponseEntity.ok(ListResponseDTO.success(tramites));
    }
}

