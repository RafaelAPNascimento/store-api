package main;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.serverless.model.Product;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {

        String sh = m01(Product.class);
        System.out.println(sh);
    }

    static String m01(Class clazz) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        JsonSchema schema = mapper.generateJsonSchema(clazz);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
    }
}
