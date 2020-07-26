package com.example.backend.internal;

import com.example.backend.customer.Customer;
import com.example.backend.customer.CustomerRepository;
import io.codearte.jfairy.Fairy;
import java.util.stream.IntStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Fixtures {

    @Bean
    public Fairy fairy() {
        return Fairy.create();
    }

    @Bean
    public CommandLineRunner customers(CustomerRepository customerRepository, Fairy fairy) {
        return args -> IntStream.range(1, 150)
            .mapToObj(index -> new Customer(index, fairy.person().getFullName()))
            .peek(System.out::println)
            .forEach(customerRepository::save);
    }
}
