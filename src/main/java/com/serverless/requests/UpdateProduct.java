package com.serverless.requests;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.model.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UpdateProduct implements RequestHandler<Product, ApiGatewayResponse> {

    private static LambdaLogger LOGGER;

    @Override
    public ApiGatewayResponse handleRequest(Product input, Context context) {

        LOGGER = context.getLogger();
        LOGGER.log(String.format("====================== received request UpdateProduct:\n %s\n", input));

        Map<String, Object> map = new HashMap<>();
        map.put("product", input);
        map.put("value", "kkkkkkkkkkkkkkk");
        map.put("id", input.getName());


        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(new Response("Reached", map))
                //.setObjectBody("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk")
                .setHeaders(Collections.singletonMap("X-Powered-By", "UOL"))
                .build();
    }
}
