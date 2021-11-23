package vn.danghung.dev.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vn.danghung.dev.service.DocService;
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Autowired
    private DocService docService;
    //indexing once on start up
    //but i comment this method because i have a index file
    //u can remove comment to get new index file then comment this method to run test faster
    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 1000)
    public void runOnceOnStartup(){
        docService.indexing();
    }
}
