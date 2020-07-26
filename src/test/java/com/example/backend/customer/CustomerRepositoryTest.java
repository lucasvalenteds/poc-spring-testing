package com.example.backend.customer;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testNameShouldNotBeEmpty() {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(new Customer(1L, ""));
            entityManager.flush();
        });
    }

    @Test
    void testNameShouldNotBeBlank() {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(new Customer(1L, " "));
            entityManager.flush();
        });
    }

    @Test
    void testCustomerShouldHaveName() {
        Customer customer = repository.save(new Customer(1L, "João da Silva"));

        entityManager.flush();

        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("João da Silva", customer.getName());
    }

    @Test
    void testIdCanBeGenerated() {
        Customer customer = new Customer();
        customer.setName("John Smith");

        Customer john = repository.save(customer);
        entityManager.flush();

        assertEquals(1L, john.getId());
    }
}