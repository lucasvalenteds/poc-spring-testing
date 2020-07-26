package com.example.backend.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.codearte.jfairy.Fairy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CustomerController.class)
@AutoConfigureDataJpa
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatingValidCustomer() throws Exception {
        Customer customerWithName = createCustomerWithName();
        Customer customerWithId = createCustomerWithId();

        Mockito.when(repository.save(customerWithName))
            .thenReturn(customerWithId);

        String responseBody = mvc
            .perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(customerWithName))
            )
            .andExpect(status().isCreated())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(mapper.readValue(responseBody, Customer.class), customerWithId);
        Mockito.verify(repository).save(customerWithName);
    }

    @Test
    void testFindingValidCustomerById() throws Exception {
        Customer customerWithId = createCustomerWithId();

        Mockito.when(repository.findById(customerWithId.getId()))
            .thenReturn(Optional.of(customerWithId));

        String responseBody = mvc
            .perform(
                get("/customers/" + customerWithId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertEquals(mapper.readValue(responseBody, Customer.class), customerWithId);
        Mockito.verify(repository).findById(customerWithId.getId());
    }

    @Test
    void testCustomerNotFoundById() throws Exception {
        Customer customerWithId = createCustomerWithId();

        Mockito.when(repository.findById(customerWithId.getId()))
            .thenReturn(Optional.empty());

        mvc
            .perform(
                get("/customers/" + customerWithId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());

        Mockito.verify(repository).findById(customerWithId.getId());
    }

    @Test
    void testFindingAllCustomers() throws Exception {
        Customer customerWithId = createCustomerWithId();
        PageRequest pageable = PageRequest.of(0, 1);
        PageImpl<Customer> page = new PageImpl<>(List.of(customerWithId));

        Mockito.when(repository.findAll(pageable)).thenReturn(page);

        String responseBody = mvc
            .perform(
                get("/customers")
                    .queryParam("page", "" + pageable.getPageNumber())
                    .queryParam("size", "" + pageable.getPageSize())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.customers").isArray())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Customers customers = mapper.readValue(responseBody, Customers.class);

        assertIterableEquals(customers.getCustomers(), page.getContent());
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    void testRemovingValidCustomer() throws Exception {
        Customer customerWithId = createCustomerWithId();

        mvc
            .perform(
                delete("/customers/" + customerWithId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(customerWithId))
            )
            .andExpect(status().isNoContent())
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(repository).deleteById(customerWithId.getId());
    }

    private final Fairy fairy = Fairy.create();

    private Customer createCustomerWithName() {
        Customer customer = new Customer();
        customer.setName(fairy.person().getFullName());
        return customer;
    }

    private Customer createCustomerWithId() {
        Customer customer = createCustomerWithName();
        customer.setId(1L);
        return customer;
    }
}
