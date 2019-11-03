package com.example.netbooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.netbooks.services.EmailSender;

@SpringBootApplication
public class NetbooksApplication{ //implements CommandLineRunner {
	
	@Autowired
	EmailSender emailSender;
	
	public static void main(String[] args) {
		SpringApplication.run(NetbooksApplication.class, args);
	}
	/*
	@Override
    public void run(String... args) {
		/*
		 import org.apache.commons.validator.EmailValidator;
		 
		 EmailValidator validator = EmailValidator.getInstance();
		 if (validator.isValid(email)...
		 ///
		emailSender.sendMessage("reniko@ukr.net", "ret", "bet");
        System.out.println("gg");

    }
	*/
}
