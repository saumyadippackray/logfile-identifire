package com.cs.logfile.time.identifier.logfileidentifier.repository;

import com.cs.logfile.time.identifier.logfileidentifier.entiry.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventStatusRepository extends JpaRepository<EventStatus,String> {
    List<EventStatus> findAll();
}
