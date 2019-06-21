package com.serverless.requests;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class DeleteProduct implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String DYNAMO_TABLE_NAME = System.getenv("TABLE_NAME");
    private static LambdaLogger LOGGER;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent req, Context context) {

        LOGGER = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        String responseBody = null;

        try {
            String paramId = req.getPathParameters().get("id");

            if (paramId != null) {

                Long id = Long.parseLong(paramId);
                AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.defaultClient();
                DynamoDB dynamoDB = new DynamoDB(dbClient);
                Table table = dynamoDB.getTable(DYNAMO_TABLE_NAME);

                DeleteItemSpec del = new DeleteItemSpec()
                        .withPrimaryKey("id", id)
                        .withReturnValues(ReturnValue.ALL_OLD);

                DeleteItemOutcome outcome = table.deleteItem(del);
                DeleteItemResult deleteItem = outcome.getDeleteItemResult();

                if (deleteItem != null) {
                    response.setStatusCode(200);
                    responseBody = deleteItem.toString();
                }
            }
            else{
                LOGGER.log("ID parametro inválido'");
                responseBody = "ID parametro inválido"+". ID: "+paramId;;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(500);
            responseBody = e.getMessage();
        }

        response.setBody(responseBody);
        return response;
    }
}
