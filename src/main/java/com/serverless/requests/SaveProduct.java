package com.serverless.requests;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.model.Product;

public class SaveProduct implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String DYNAMO_TABLE_NAME = System.getenv("TABLE_NAME");
    private static LambdaLogger LOGGER;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        LOGGER = context.getLogger();

        ObjectMapper mapper = new ObjectMapper();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        try {
            String requestBody = requestEvent.getBody();
            LOGGER.log(String.format("====================== received request SaveProduct:\n %s\n", requestBody));

            if(requestBody != null){

                Product product = mapper.readValue(requestBody, Product.class);

                AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.defaultClient();
                DynamoDB dynamoDB = new DynamoDB(dbClient);

                Item productItem = new Item().withNumber("id", product.getId())
                                                .with("name", product.getName())
                                                .withDouble("value", product.getValue())
                                                .with("description", product.getDescription());

                dynamoDB.getTable(DYNAMO_TABLE_NAME).putItem(new PutItemSpec().withItem(productItem));

                responseEvent.setStatusCode(200);
                responseEvent.setBody(mapper.writeValueAsString(product));
            }
        }
        catch (Exception e){
            responseEvent.setStatusCode(500);
            responseEvent.setBody(e.getMessage());
        }
        return responseEvent;
    }
}
