package com.techprimers.serverless.service;

import com.techprimers.serverless.dao.TermRepository;
import com.techprimers.serverless.domain.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;

    @Autowired
    public TermServiceImpl(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Override
    public Term getTermById(String id) {
        return termRepository.getTermById(id);
    }

    @Override
    public String addTerm(Term term) {
        if(termRepository.addTerm(term) != null) {
            return "New product has been added to database";
        }
        else {
            return "Error while adding a new product to database!";
        }
    }
}
