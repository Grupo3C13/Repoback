package com.example.demo.service;

import com.example.demo.entity.Policy;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    @Autowired
    PolicyRepository policyRepository;

    public Policy addPolicy(Policy policy){
        return policyRepository.save(policy);
    }

    public List<Policy> getAllPolicies(){
        return  policyRepository.findAll();
    }

    public Optional<Policy> findPolicyById(Long id){
        return policyRepository.findById(id);
    }
    public Policy updatePolicy(Policy policy)throws BadRequestException {
        Optional<Policy> politicaBuscada = findPolicyById(policy.getId());
        if (politicaBuscada.isPresent()){
            return policyRepository.save(policy);
        }
        else {
            throw new BadRequestException("Could not update policy with id : "+policy.getId());
        }

    }
    public void deletePolicyById(Long id) throws ResourceNotFoundException {
        Optional<Policy> politicaBuscada = findPolicyById(id);
        if (politicaBuscada.isPresent()){
            policyRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("There is no policy with id : "+id+" for that reason it cannot be deleted");
        }

    }

}
