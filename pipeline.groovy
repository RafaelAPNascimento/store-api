// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
@Grapes(
        @Grab(group='com.amazonaws', module='aws-java-sdk', version='1.11.572')
)

import groovy.json.JsonSlurper
import groovy.json.JsonSlurper
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.Deployment;
import com.amazonaws.services.apigateway.model.GetDeploymentsRequest;
import com.amazonaws.services.apigateway.model.GetDeploymentsResult;

node('master'){
    stage("============== deploy API"){

        deployAPI(api:'rjer823v32', stage:'dev')
    }
}