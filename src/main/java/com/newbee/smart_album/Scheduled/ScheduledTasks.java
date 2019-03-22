package com.newbee.smart_album.Scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanRecycleBin()
    {

    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanTempFile()
    {

    }
}
