package apiquality.sonar.openapi.checks.schemas;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Rule(key = OAR108SchemaValidatorCheck.KEY)
public class OAR108SchemaValidatorCheck extends BaseCheck {

    public static final String KEY = "OAR108";
    private static final String MESSAGE = "OAR108.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (node.isObject()) {
            for (JsonNode pathNode : node.propertyMap().values()) {
                for (JsonNode operationNode : pathNode.propertyMap().values()) {
                    JsonNode responsesNode = operationNode.get("responses");
                    if (responsesNode != null && responsesNode.isObject()) {
                        for (JsonNode responseNode : responsesNode.propertyMap().values()) {
                            JsonNode contentNode = responseNode.get("content");
                            if (contentNode != null && contentNode.isObject()) {
                                for (JsonNode mediaTypeNode : contentNode.propertyMap().values()) {
                                    JsonNode schemaNodeOpenApi3 = mediaTypeNode.get("schema");
                                    JsonNode exampleNodeOpenApi3 = mediaTypeNode.get("example");
                                    if (schemaNodeOpenApi3 != null && exampleNodeOpenApi3 != null && !schemaNodeOpenApi3.isMissing() && !schemaNodeOpenApi3.isNull() && !exampleNodeOpenApi3.isMissing() && !exampleNodeOpenApi3.isNull()) {
                                            Map<String, String> schemaTypes = extractSchemaTypes(schemaNodeOpenApi3);
                                            Map<String, String> exampleTypes = extractExampleTypes(exampleNodeOpenApi3);
                                            for (Map.Entry<String, String> entry : schemaTypes.entrySet()) {
                                                String key = entry.getKey();
                                                String expectedType = entry.getValue();
                                                String actualType = exampleTypes.getOrDefault(key, "unknown");
                                                if (!expectedType.equals(actualType)) {
                                                    addIssue(KEY, translate(MESSAGE), exampleNodeOpenApi3.key());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    JsonNode schemaNodeSwagger2 = responseNode.get("schema");
                                    JsonNode examplesNodeSwagger2 = responseNode.get("examples");
                                    if (schemaNodeSwagger2 != null && examplesNodeSwagger2 != null && !schemaNodeSwagger2.isMissing() && !schemaNodeSwagger2.isNull() && !examplesNodeSwagger2.isMissing() && !examplesNodeSwagger2.isNull()) {
                                        Map<String, String> schemaTypes = extractSchemaTypes(schemaNodeSwagger2);
                                        Map<String, String> exampleTypes = extractExampleTypesSwagger2(examplesNodeSwagger2);
                                        for (Map.Entry<String, String> entry : schemaTypes.entrySet()) {
                                            String key = entry.getKey();
                                            String expectedType = entry.getValue();
                                            String actualType = exampleTypes.getOrDefault(key, "unknown");
                                            if (!expectedType.equals(actualType)) {
                                                addIssue(KEY, translate(MESSAGE), examplesNodeSwagger2.key());
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
    private Map<String, String> extractSchemaTypes(JsonNode schemaNode) {
        Map<String, String> schemaTypes = new HashMap<>();
    
        JsonNode propertiesNode = schemaNode.get("properties");
        if (propertiesNode != null && propertiesNode.isObject()) {
            for (Map.Entry<String, JsonNode> entry : propertiesNode.propertyMap().entrySet()) {
                String propertyName = entry.getKey();
                JsonNode propertyTypeNode = entry.getValue().get("type");
                String propertyType = propertyTypeNode != null ? propertyTypeNode.stringValue() : null;
                schemaTypes.put(propertyName, propertyType);
            }
        }
    
        return schemaTypes;
    }

    private Map<String, String> extractExampleTypes(JsonNode exampleNode) {
        Map<String, String> exampleTypes = new HashMap<>();
    
        if (exampleNode != null && exampleNode.isObject()) {
            for (Map.Entry<String, JsonNode> entry : exampleNode.propertyMap().entrySet()) {
                String propertyName = entry.getKey();
                JsonNode propertyValueNode = entry.getValue();
                String propertyType = determineExampleType(propertyValueNode);
                exampleTypes.put(propertyName, propertyType);
            }
        }
    
        return exampleTypes;
    }
    private Map<String, String> extractExampleTypesSwagger2(JsonNode examplesNode) {
        Map<String, String> exampleTypes = new HashMap<>();
        
        for (JsonNode exampleMediaTypeNode : examplesNode.propertyMap().values()) {
            if (exampleMediaTypeNode.isObject()) {
                for (Map.Entry<String, JsonNode> entry : exampleMediaTypeNode.propertyMap().entrySet()) {
                    String propertyName = entry.getKey();
                    JsonNode propertyValueNode = entry.getValue();
                    String propertyType = determineExampleType(propertyValueNode);
                    exampleTypes.put(propertyName, propertyType);
                }
            }
        }
    
        return exampleTypes;
    }
    
    private String determineExampleType(JsonNode node) {
        String value = node.stringValue().trim();
    
        // Verifica si el valor es numérico.
        if (value.matches("-?\\d+\\.\\d+")) {
            // Si el valor tiene un punto, se clasifica como 'number'.
            return "number";
        } else if (value.matches("-?\\d+")) {
            // Si el valor es numérico pero no tiene un punto, se clasifica como 'integer'.
            return "integer";
        }
    
        // Verifica si es un valor booleano.
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return "boolean";
        }
    
        // Verifica si el valor es un objeto o un array.
        if (node.isObject()) {
            return "object";
        } else if (node.isArray()) {
            return "array";
        }
    
        // Si no es ninguno de los anteriores, asume que es una cadena.
        return "string";
    }
}

    


