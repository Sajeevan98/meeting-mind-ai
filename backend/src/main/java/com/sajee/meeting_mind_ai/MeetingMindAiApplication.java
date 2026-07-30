package com.sajee.meeting_mind_ai;

import com.sajee.meeting_mind_ai.storage.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MeetingMindAiApplication {

	public static void main(String[] args) {

//		System.out.println("DB_HOST = " + System.getenv("DB_HOST"));
		SpringApplication.run(MeetingMindAiApplication.class, args);
	}

}
