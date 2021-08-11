package com.techprimers.serverless.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamoDbConfiguration {
    private static final String DYNAMO_DB_CREDENTIALS = "dynamo-db-credentials";

    @Value("${db.url}")
    private String dbUrl;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(getAmazonDynamoDBClient());
    }

    private AmazonDynamoDB getAmazonDynamoDBClient() {
        String accessKey = getSecret("accessKey");
        String secretKey = getSecret("secretKey");
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

    private String getSecret(String secretName) {
        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(DYNAMO_DB_CREDENTIALS)
                .build();

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
        String secret = valueResponse.secretString();
        Map<String, String> secretMap = new HashMap<>();
        for(String s : secret.substring(1, secret.length() - 1).split(",")) {
            String[] split = s.split(":");
            secretMap.put(split[0].substring(1, split[0].length() - 1), split[1].substring(1, split[1].length() - 1));
        }
        secretsClient.close();
        return secretMap.get(secretName);
    }
}
