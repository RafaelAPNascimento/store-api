package com.serverless.requests;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.model.Product;

import java.util.List;

public class ListProducts implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static LambdaLogger LOGGER;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        LOGGER = context.getLogger();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

        LOGGER.log(String.format("====================== received request SaveProduct:\n %s\n", request));

        List<Product> products = dynamoDBMapper.scan(Product.class, scanExpression);
        products.size();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        if (!products.isEmpty()) {

            try {
                ObjectMapper jsonMapper = new ObjectMapper();
                response.setStatusCode(200);
                response.setBody(jsonMapper.writeValueAsString(products));
            }
            catch (JsonProcessingException e){
                e.printStackTrace();
                response.setStatusCode(500);
                response.setBody(e.getMessage());
            }
        }
        else{
            response.setStatusCode(200);
            response.setBody("Nenhum produto");
        }
        return response;
    }
}
