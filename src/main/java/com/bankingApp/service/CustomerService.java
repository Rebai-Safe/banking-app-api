package com.bankingApp.service;

import com.bankingApp.dto.CustomerDto;
import com.bankingApp.entity.Customer;
import com.bankingApp.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> listCustomer();
    CustomerDto getCustomer(Long id) throws CustomerNotFoundException;
    CustomerDto saveCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(CustomerDto customerDto);
    void deleteCustomer(Long customerId);

    List<CustomerDto> searchCustomers(String keyword);
}

