package com.example.blog_backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication

public class BlogBackendApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(BlogBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("🔴🔴🔴 CHECKING BEANS:");

		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			if (beanName.toLowerCase().contains("blog")) {
				System.out.println("🔴 Found bean: " + beanName + " -> " +
						applicationContext.getBean(beanName).getClass().getName());
			}
		}

		System.out.println("🔴🔴🔴 CHECKING REQUEST MAPPINGS:");
		RequestMappingHandlerMapping mapping = applicationContext
				.getBean(RequestMappingHandlerMapping.class);

		mapping.getHandlerMethods().forEach((key, value) -> {
			if (key.toString().contains("blog")) {
				System.out.println("🔴 Mapping: " + key + " -> " + value);
			}
		});
	}
}
