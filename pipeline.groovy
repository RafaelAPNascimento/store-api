
import groovy.json.JsonSlurper
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder
import com.amazonaws.services.apigateway.model.CreateStageRequest
import com.amazonaws.services.apigateway.model.CreateStageResult;
import com.amazonaws.services.apigateway.model.Deployment;
import com.amazonaws.services.apigateway.model.GetDeploymentsRequest;
import com.amazonaws.services.apigateway.model.GetDeploymentsResult;


@Grab(group = "com.amazonaws", module = "aws-java-sdk", version = "1.11.575")
class Test{}

node('master'){
stage("============== compile"){

    //returns the AWS security credentials configured for the named profile
    AWSCredentialsProvider credentialsProvider = new EnvironmentVariableCredentialsProvider()
    echo credentialsProvider.getCredentials()
    //echo '${env.AWS_ACCESS_KEY_ID}'
    //echo '${env.AWS_SECRET_ACCESS_KEY}'
    sh 'printenv'
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