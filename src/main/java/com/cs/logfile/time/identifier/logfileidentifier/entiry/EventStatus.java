package com.cs.logfile.time.identifier.logfileidentifier.entiry;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="event_status")
public class EventStatus {
    @Id
    private String eventId;

    private int eventDuration;

    private String eventType;

    private String eventHost;

    private Boolean eventAlert;
}
