package com.github.sooogle.jpademo;

import com.github.sooogle.jpademo.entity.Owner;
import com.github.sooogle.jpademo.entitysub.PetEager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = {Owner.class, PetEager.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
