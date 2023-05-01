package com.FoodOrdering.OrderUp;

import com.FoodOrdering.OrderUp.ScheduleTask.ScheduleTask;
import com.FoodOrdering.OrderUp.Service.DailyReportService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication (exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class OrderUp {

	public static void main(String[] args) {
		ApplicationContext context =
		SpringApplication.run(OrderUp.class, args);

//		ScheduleTask scheduleTask= context.getBean(ScheduleTask.class);
//		scheduleTask.dailyCalculateRating();
//		DailyReportService dailyReportService = context.getBean(DailyReportService.class);
//		try {
//			dailyReportService.transactionByDay(new ObjectId("644b3ffd9630996703271914"), "dodat263@gmail.com");
//		} catch (InvalidFormatException e) {
//			e.printStackTrace();
//		}

	}

}
