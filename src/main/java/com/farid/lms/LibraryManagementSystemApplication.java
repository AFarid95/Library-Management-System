package com.farid.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import com.farid.lms.logging.OperationLogger;

@SpringBootApplication
@EnableCaching
public class LibraryManagementSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

    @Bean
    OperationLogger operationLogger() {
        return new OperationLogger();
    }
}
