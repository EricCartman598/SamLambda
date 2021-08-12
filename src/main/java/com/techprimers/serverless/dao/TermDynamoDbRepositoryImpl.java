package com.techprimers.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.techprimers.serverless.domain.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;


@Repository
public class TermDynamoDbRepositoryImpl implements TermRepository {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public Term addTerm(Term term) {
        dynamoDBMapper.save(term);
        return term;
    }

    @Override
    public Term getTermById(String id) {
        System.out.println("getting from DB");
        return dynamoDBMapper.load(Term.class, id);
    }
}
