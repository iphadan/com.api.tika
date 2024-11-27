package com.api.tika;

import com.api.tika.models.roles.Role;
import com.api.tika.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TikaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TikaApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository){
		System.out.println(" ******************************");
		System.out.println(" *    Default Role Seeding    *");
		System.out.println(" ******************************");

		return args ->	{
			if(roleRepository.findRoleByName("USER").isEmpty()){
				Role role = Role.builder().name("USER").build();
				roleRepository.save(role);
			}

		};
	}
}
