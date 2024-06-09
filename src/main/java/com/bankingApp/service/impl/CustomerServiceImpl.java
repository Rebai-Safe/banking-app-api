package com.bankingApp.service.impl;

import com.bankingApp.dto.CustomerDto;
import com.bankingApp.entity.Customer;
import com.bankingApp.exception.CustomerNotFoundException;
import com.bankingApp.repository.CustomerRepository;
import com.bankingApp.service.CustomerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public List<CustomerDto> listCustomer() {
        return customerRepository.findAll().stream().map(customer -> modelMapper.map(customer, CustomerDto.class)).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomer(Long id) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {

        LOGGER.info("Saving new customer");
        Customer savedCustomer = customerRepository.save(modelMapper.map(customerDto, Customer.class));
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {

        LOGGER.info("Updating customer");
        Customer savedCustomer = customerRepository.save(modelMapper.map(customerDto, Customer.class));
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword) {
        return customerRepository.findByNameContains(keyword).stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }
}
