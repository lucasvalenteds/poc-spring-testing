package com.example.backend.customer;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Valid Customer customer) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(repository.save(customer));
    }

    @GetMapping
    public ResponseEntity<Customers> findAllCustomer(Pageable pageable) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Customers(repository.findAll(pageable).getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findOneCustomer(@PathVariable("id") long id) {
        return repository.findById(id)
            .map(customer ->
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(customer)
            )
            .orElseGet(() ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null)
            );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
        repository.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(null);
    }
}
