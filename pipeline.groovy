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
stage("============== compile"){

    echo $AWS_ACCESS_KEY_ID
    echo $AWS_SECRET_ACCESS_KEY
    dir('store-api') {
        pwd()
        sh 'ls -la'
        withMaven(maven: 'maven_3_5_2') {
            sh 'mvn compile'
        }
    }
}
stage("============== test"){

    withMaven(maven: 'maven_3_5_2'){
        sh 'mvn test'
    }
}
stage("============== package"){

    withMaven(maven: 'maven_3_5_2'){
        sh 'mvn clean package'
    }
}
stage("============== deploy"){
    sh 'sls deploy --profile pessoal'
}
}