package com.FoodOrdering.OrderUp.ScheduleTask;


import com.FoodOrdering.OrderUp.Service.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    @Autowired
    ApplicationService applicationService;

    private static final Logger log = LogManager.getLogger(ScheduleTask.class);


    @Scheduled(cron = "${dailyCalculate.rating.schedule.cron}")
    public void dailyCalculateRating (){
        System.out.println("===================> daily calculate rating start");
        applicationService.dailyCalculateRating();

    }


    @Scheduled(cron = "${dailyReport.export.schedule.cron}")
    public void exportDailyReport() {

        log.info("{} | ==================== START ====================","exportDailyReport" );

    }

    @Scheduled(cron = "${dailyReport.email.send.schedule.cron}")
    public void sendEmailDailyReport() {

        log.info("{} | ==================== START ====================","sendEmailDailyReport" );


    }



}
