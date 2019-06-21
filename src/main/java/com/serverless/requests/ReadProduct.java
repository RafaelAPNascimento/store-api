package com.serverless.requests;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Product;

import java.util.Collections;

public class ReadProduct implements RequestHandler<Product, ApiGatewayResponse> {

    private static LambdaLogger LOGGER;

    @Override
    public ApiGatewayResponse handleRequest(Product input, Context context) {

        LOGGER = context.getLogger();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDb = new DynamoDB(client);

        if(input != null && input.getId() != null){


            return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(input.getId()).build();

        }


        return ApiGatewayResponse.builder()
                .setStatusCode(404)
                .setObjectBody(new Response("Product not found", Collections.singletonMap("product", input)))
                .setHeaders(Collections.singletonMap("X-Powered-By", "UOL"))
                .build();
    }
}
