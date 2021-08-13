package com.techprimers.serverless;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.techprimers.serverless.domain.Term;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Handler for requests to Lambda function.
 * Created 13/08/2021
 * Author: Vitalii Morozov
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        String termId = input.getPathParameters().get("id");
        Term term = getTermById(termId);
        String output = String.format("{ \"termId\": \"%s\", \"term\": \"%s\", \"price\": \"%s\" }",
                term.getId(), term.getName(), term.getPrice());
        return response
                .withStatusCode(200)
                .withBody(output);
    }

    private Term getTermById(String id) {
        return getMapper().load(Term.class, id);
    }

    private DynamoDBMapper getMapper() {
        return new DynamoDBMapper(getAmazonDynamoDBClient());
    }

    private AmazonDynamoDB getAmazonDynamoDBClient() {
        String accessKey = getEnvVariable("ACCESS_KEY");
        String secretKey = getEnvVariable("SECRET_KEY");
        String dbUrl = getEnvVariable("DB_URL");
        String region = getEnvVariable("REGION");
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                dbUrl,
                                region
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        accessKey,
                                        secretKey
                                )
                        )
                )
                .build();
    }

    private String getEnvVariable(String key) {
        return System.getenv().entrySet()
                .stream()
                .filter(var -> var.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Environment variable not found!"))
                .getValue();
    }
}
