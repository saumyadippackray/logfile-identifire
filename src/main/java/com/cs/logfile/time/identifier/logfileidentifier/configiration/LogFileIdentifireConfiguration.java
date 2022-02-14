package com.cs.logfile.time.identifier.logfileidentifier.configiration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogFileIdentifireConfiguration {
    @Value("${limitTime}")
    public Integer limitTime;
}
