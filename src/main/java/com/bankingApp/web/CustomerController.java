package com.bankingApp.web;

import com.bankingApp.dto.CustomerDto;
import com.bankingApp.exception.CustomerNotFoundException;
import com.bankingApp.model.HttpResponse;
import com.bankingApp.service.CustomerService;
import com.bankingApp.utils.ResponseHandlerUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@RestController
public class CustomerController {

    private CustomerService CustomerService;


    @GetMapping("/customers")
    public ResponseEntity<HttpResponse> getCustomersList(){

        return ResponseHandlerUtil.generateResponse("Customers list returned successfully",
                HttpStatus.OK,
                CustomerService.listCustomer());
    }

    @GetMapping("/customers/search")
    public ResponseEntity<HttpResponse> searchCustomers(
            @RequestParam(name = "keyword", defaultValue = "")
            String keyword){

        return ResponseHandlerUtil.generateResponse("Customers list returned successfully",
                HttpStatus.OK,
                CustomerService.searchCustomers(keyword));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@PathVariable("id") Long customerId) throws CustomerNotFoundException {

        return ResponseHandlerUtil.generateResponse("Customer returned successfully",
                HttpStatus.OK,
                CustomerService.getCustomer(customerId));
    }

    @PostMapping("/customers")
    public ResponseEntity<HttpResponse> saveCustomer(@RequestBody CustomerDto customerDto){

        return ResponseHandlerUtil.generateResponse("Customer saved successfully",
                 HttpStatus.ACCEPTED,
                 CustomerService.saveCustomer(customerDto));
    }


    @PutMapping("/customers/{id}")
    public ResponseEntity<HttpResponse> updateCustomer(@RequestBody CustomerDto customerDto,
                                                      @PathVariable("id") Long customerId){
        customerDto.setId(customerId);

        return ResponseHandlerUtil.generateResponse("Customer updated successfully",
                HttpStatus.ACCEPTED,
                CustomerService.saveCustomer(customerDto));
    }


    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        CustomerService.deleteCustomer(id);
    }
}
