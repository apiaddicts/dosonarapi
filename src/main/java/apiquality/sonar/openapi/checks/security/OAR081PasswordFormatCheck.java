package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import java.util.Set;

@Rule(key = OAR081PasswordFormatCheck.KEY)
public class OAR081PasswordFormatCheck extends BaseCheck {

    
    public static final String KEY = "OAR081";
    private static final String MESSAGE = "OAR081.error";
    
    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi2Grammar.DEFINITIONS, OpenApi3Grammar.PATHS, OpenApi3Grammar.COMPONENTS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitPathsNode(node);
        } else if (OpenApi2Grammar.DEFINITIONS.equals(node.getType())) {
            visitDefinitionsNode(node);
        } else if (OpenApi3Grammar.COMPONENTS.equals(node.getType())) {
            JsonNode schemasNode = node.get("schemas");
            if (!schemasNode.isMissing()) {
                visitDefinitionsNode(schemasNode);
            }
        }
    }

    private void visitDefinitionsNode(JsonNode definitionsNode) {
        for (JsonNode schemaNode : definitionsNode.propertyMap().values()) {
            JsonNode propertiesNode = schemaNode.get("properties");
            if (!propertiesNode.isMissing()) {
                validatePasswordFormat(propertiesNode);
            }
        }
    }

    private void visitPathsNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) {
            for (JsonNode operationNode : pathNode.propertyMap().values()) {
                validateOperationNode(operationNode);
            }
        }
    }

    private void validateOperationNode(JsonNode operationNode) {
    JsonNode parametersNode = operationNode.get("parameters");
    if (!parametersNode.isMissing()) {
        for (JsonNode parameterNode : parametersNode.elements()) {
            if ("body".equals(parameterNode.get("in").getTokenValue())) {
                validateRequestBody(parameterNode);
            }
        }
    }
    else {
        validateRequestBody(operationNode.get("requestBody"));
    }
    validateResponses(operationNode.get("responses"));
}

private void validateRequestBody(JsonNode requestBodyNode) {
    if (!requestBodyNode.isMissing()) {
        JsonNode schemaNode = requestBodyNode.get("schema");
        if (!schemaNode.isMissing()) {
            validatePasswordFormat(schemaNode.get("properties"));
        }
        else {
            JsonNode contentNode = requestBodyNode.get("content");
            if (!contentNode.isMissing()) {
                for (JsonNode mediaTypeNode : contentNode.propertyMap().values()) {
                    schemaNode = mediaTypeNode.get("schema");
                    if (!schemaNode.isMissing()) {
                        validatePasswordFormat(schemaNode.get("properties"));
                    }
                }
            }
        }
    }
}

private void validateResponses(JsonNode responsesNode) {
    if (!responsesNode.isMissing()) {
        for (JsonNode responseNode : responsesNode.propertyMap().values()) {
            JsonNode schemaNode = responseNode.get("schema");
            if (!schemaNode.isMissing()) {
                validatePasswordFormat(schemaNode.get("properties"));
            }
            else {
                JsonNode contentNode = responseNode.get("content");
                if (!contentNode.isMissing()) {
                    for (JsonNode mediaTypeNode : contentNode.propertyMap().values()) {
                        schemaNode = mediaTypeNode.get("schema");
                        if (!schemaNode.isMissing()) {
                            validatePasswordFormat(schemaNode.get("properties"));
                        }
                    }
                }
            }
        }
    }
}

    private void validatePasswordFormat(JsonNode propertiesNode) {
        if (!propertiesNode.isMissing()) {
            JsonNode passwordNode = propertiesNode.get("password");
            if (!passwordNode.isMissing() && !"password".equals(passwordNode.get("format").getTokenValue())) {
                addIssue(KEY, translate(MESSAGE), passwordNode.key());
            }
        }
    }
}