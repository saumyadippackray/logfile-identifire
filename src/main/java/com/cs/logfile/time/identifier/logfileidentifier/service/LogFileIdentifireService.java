package com.cs.logfile.time.identifier.logfileidentifier.service;

import com.cs.logfile.time.identifier.logfileidentifier.configiration.LogFileIdentifireConfiguration;
import com.cs.logfile.time.identifier.logfileidentifier.dto.Event;
import com.cs.logfile.time.identifier.logfileidentifier.dto.State;
import com.cs.logfile.time.identifier.logfileidentifier.entiry.EventStatus;
import com.cs.logfile.time.identifier.logfileidentifier.repository.EventStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class LogFileIdentifireService {
    Logger logger= LogManager.getLogger(LogFileIdentifireService.class);

    @Autowired
    DbOperationService dbOperationService;

    @Autowired
    EventStatusRepository eventStatusRepository;

    @Autowired
    LogFileIdentifireConfiguration logFileIdentifireConfiguration;

    public void processFile(String filePath){
        try {
                //taking a hashMap to store active Events
                Map<String, Event> events=new HashMap<>();

                //creating file instance
                File file=new File(filePath);
                //Read the file
                FileReader fr=new FileReader(file);
                //creates a buffering character input stream
                BufferedReader br=new BufferedReader(fr);

                String line;
                while((line=br.readLine())!=null)
                {
                    Event event1;
                    //parsing event from each line of file
                    event1 = new ObjectMapper().readValue(line, Event.class);
                    //logic if the event is already present in hashmap
                    if(events.containsKey(event1.getId())){
                        //Getting event if the event is already present in hashmap
                        Event event2=events.get(event1.getId());
                        //calculate the delay time between two events
                        int delayTime=getDelayTime(event1,event2);
                        //Prepare the EventStatus Object
                        EventStatus eventStatus=getEventStatus(delayTime,event1);
                        //save the eventStatus object in table
                        dbOperationService.saveInEventStatus(eventStatus);
                        //Remove the event from the hashmap
                        events.remove(event1.getId());
                    }
                    //logic if the event is not present in the hashmap
                    else{
                        events.put(event1.getId(),event1);
                    }
                }
            }
        catch (FileNotFoundException fileNotFoundException){
            logger.info("File is not present"+fileNotFoundException);
        }
        catch (IOException ioException){
            logger.info("io exception");
        }

    }

    //Method to calculate the delay time between two events with same id
    public Integer getDelayTime(Event event1,Event event2){
        if(Objects.nonNull(event1) && Objects.nonNull(event2)) {
            //Filter the start event
            Event start = Stream.of(event1, event2).filter(e -> State.STARTED.equals(e.getState())).findFirst().orElse(null);
            //Filter the end event
            Event end = Stream.of(event1, event2).filter(e -> State.FINISHED.equals(e.getState())).findFirst().orElse(null);
            //Calculate the delay time
            Long delayTime=end.getTimestamp()-start.getTimestamp();
            return delayTime.intValue();
        }
        return null;
    }
    public EventStatus getEventStatus(int delayTime,Event event){
        Boolean eventAlert;
        if(logFileIdentifireConfiguration.limitTime>delayTime){
            eventAlert=false;
        }
        else
        {
            eventAlert=true;
        }
        EventStatus eventStatus=new EventStatus();
        eventStatus.setEventId(event.getId());
        eventStatus.setEventHost(event.getHost());
        eventStatus.setEventType(event.getType());
        eventStatus.setEventDuration(delayTime);
        eventStatus.setEventAlert(eventAlert);

        return eventStatus;
    }
    public void get(){
        for(EventStatus eventStatus:eventStatusRepository.findAll()){
            System.out.println(eventStatus);
        }

    }
}
