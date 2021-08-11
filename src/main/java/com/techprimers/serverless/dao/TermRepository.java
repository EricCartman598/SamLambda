package com.techprimers.serverless.dao;

import com.techprimers.serverless.domain.Term;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository {
    Term addTerm(Term term);
    Term getTermById(String id);
}