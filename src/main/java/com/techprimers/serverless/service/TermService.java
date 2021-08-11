package com.techprimers.serverless.service;


import com.techprimers.serverless.domain.Term;

public interface TermService {
    Term getTermById(String id);

    String addTerm(Term term);
}
