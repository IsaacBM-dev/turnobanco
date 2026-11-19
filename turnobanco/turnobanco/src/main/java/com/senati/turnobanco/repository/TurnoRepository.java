package com.senati.turnobanco.repository;

import com.senati.turnobanco.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    // Verificar o buscar por código de turno exacto (para actualizar/eliminar por otro campo)
    Optional<Turno> findByCodigoTurnoIgnoreCase(String codigoTurno);

    boolean existsByCodigoTurnoIgnoreCase(String codigoTurno);

    // Contar turnos en espera por trámite para el Cálculo 2 (Tiempo de espera en cola)
    long countByTramite_IdTramiteAndEstadoIgnoreCase(Integer idTramite, String estado);

    // Requerimiento PDF: Listar todos los registros ordenados por algún campo distinto a Id
    List<Turno> findAllByOrderByApellidosAscNombresAsc();

    List<Turno> findAllByOrderByCodigoTurnoAsc();

    // Requerimiento PDF: Buscar por un campo y listar por coincidencia (Código de Turno)
    List<Turno> findByCodigoTurnoContainingIgnoreCaseOrderByIdTurnoDesc(String codigoTurno);

    // Requerimiento PDF + Usuario: Buscar por 2 o más campos y listar por coincidencia
    // Filtra simultáneamente por nombres, apellidos y código de turno (si algún parámetro viene vacío, ignora ese filtro)
    @Query("SELECT t FROM Turno t WHERE " +
            "(:nombres IS NULL OR :nombres = '' OR LOWER(t.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
            "(:apellidos IS NULL OR :apellidos = '' OR LOWER(t.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
            "(:codigoTurno IS NULL OR :codigoTurno = '' OR LOWER(t.codigoTurno) LIKE LOWER(CONCAT('%', :codigoTurno, '%'))) " +
            "ORDER BY t.idTurno DESC")
    List<Turno> buscarPorFiltroMulticampo(
            @Param("nombres") String nombres,
            @Param("apellidos") String apellidos,
            @Param("codigoTurno") String codigoTurno
    );
}

