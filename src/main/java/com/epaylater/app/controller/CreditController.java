package com.epaylater.app.controller;

import com.epaylater.app.dto.CreditDto;
import com.epaylater.app.entities.Credit;
import com.epaylater.app.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;

    @PostMapping("/add")
    public ResponseEntity<String> addCredit(@RequestBody CreditDto credit) throws Exception {
        String message = "Credit info with " + creditService.addCredit(credit) + " added.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/autopay/customer/{id}")
    public ResponseEntity<String> clearDues(@PathVariable("id") Long id){
        String message = "The credit with: " + creditService.clearAllDues(id) + " created.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Credit>> getCustomerCredits(@PathVariable("id") Long id){
        return new ResponseEntity<>(creditService.getCustomerCredit(id), HttpStatus.OK);
    }

}
