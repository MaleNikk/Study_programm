package spring.boot.book_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "spring.boot")
@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {
			SpringApplication.run(BookStoreApplication.class, args);
	}
}
