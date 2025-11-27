package com.Encora.capstone.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.net.URI;
@Configuration
public class DynamoDbConfig {
@Value("${aws.region}")
private String region;
@Value("${aws.dynamodb.endpoint:}")
private String endpoint;
@Value("${aws.access-key-id:}")
private String accessKey;
@Value("${aws.secret-access-key:}")
private String secretKey;
@Bean
public DynamoDbClient dynamoDbClient() {
var builder = DynamoDbClient.builder()
.region(Region.of(region));
// Credentials: use static if provided, else default (for IAM roles)
AwsCredentialsProvider credentialsProvider;
if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
credentialsProvider = StaticCredentialsProvider.create(
AwsBasicCredentials.create(accessKey, secretKey)
);
} else {
credentialsProvider = DefaultCredentialsProvider.create();
}
builder.credentialsProvider(credentialsProvider);
// Optional endpoint override (e.g., for local dev)
if (!endpoint.isEmpty()) {
builder.endpointOverride(URI.create(endpoint));
System.out.println("Using custom endpoint: " + endpoint);
}
return builder.build();
}
@Bean
public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
return DynamoDbEnhancedClient.builder()
.dynamoDbClient(dynamoDbClient)
.build();
}
}