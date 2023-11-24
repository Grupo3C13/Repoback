package com.example.demo.controller;

import com.example.demo.entity.Policy;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    @Autowired
    PolicyService policyService;

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies(){
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> findPolicy(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Policy> politicaBuscada = policyService.findPolicyById(id);
        return politicaBuscada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity <Policy> agregarPolitica(@RequestBody Policy policy){
        return ResponseEntity.ok(policyService.addPolicy(policy));
    }
    @PutMapping
    public ResponseEntity<Policy> editarPolitica(@RequestBody Policy policy) throws BadRequestException {
        Policy politicaeditada=policyService.updatePolicy(policy);
        return ResponseEntity.ok(politicaeditada);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id ) throws ResourceNotFoundException {
        policyService.deletePolicyById(id);
        return ResponseEntity.ok("Policy with id "+ id+" was successfully eliminated. ");
    }
}
