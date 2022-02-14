package com.cs.logfile.time.identifier.logfileidentifier.service;

import com.cs.logfile.time.identifier.logfileidentifier.entiry.EventStatus;
import com.cs.logfile.time.identifier.logfileidentifier.repository.EventStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbOperationService {
    @Autowired
    EventStatusRepository eventStatusRepository;

    public void saveInEventStatus(EventStatus eventStatus){
        eventStatusRepository.save(eventStatus);
    }
}
