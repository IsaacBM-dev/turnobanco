package com.senati.turnobanco.repository;

import com.senati.turnobanco.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Integer> {
}

