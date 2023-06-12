package com.synch.user.basicinfo;

import com.synch.user.basicinfo.dto.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class BasicInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicInfoApplication.class, args);
	}

}
