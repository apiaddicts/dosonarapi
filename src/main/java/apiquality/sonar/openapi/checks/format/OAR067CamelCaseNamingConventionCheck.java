package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

@Rule(key = OAR067CamelCaseNamingConventionCheck.KEY)
public class OAR067CamelCaseNamingConventionCheck extends AbstractNamingConventionCheck {

    public static final String KEY = "OAR067";
    private static final String MESSAGE = "OAR067.error";
    private static final String NAMING_CONVENTION = CAMEL_CASE;

    public OAR067CamelCaseNamingConventionCheck() {
        super(KEY, MESSAGE, NAMING_CONVENTION);
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitPathsNode(node);
        }
    }

    private void visitPathsNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) {
            for (JsonNode operationNode : pathNode.propertyMap().values()) {
                JsonNode parametersNode = operationNode.get("parameters");
                if (!parametersNode.isMissing()) {
                    for (JsonNode parameterNode : parametersNode.elements()) {
                        if ("body".equals(parameterNode.get("in").getTokenValue())) {
                            visitParameterNode(parameterNode);
                        }
                    }
                }
    
                JsonNode requestBodyNode = operationNode.get("requestBody");
                if (!requestBodyNode.isMissing()) {
                    visitRequestBodyNode(requestBodyNode);
                }
    
                JsonNode responsesNode = operationNode.get("responses");
                if (!responsesNode.isMissing()) {
                    visitResponsesNode(responsesNode);
                }
            }
        }
    }

    private void visitParameterNode(JsonNode parameterNode) {
        JsonNode inNode = parameterNode.get("in");
        if (!inNode.isMissing() && "body".equals(inNode.getTokenValue())) {
            JsonNode schemaNode = parameterNode.get("schema");
            if (!schemaNode.isMissing()) {
                visitSchemaNode(schemaNode);
            }
        } else {
            JsonNode nameNode = parameterNode.get("name");
            if (!nameNode.isMissing()) {
                String name = nameNode.getTokenValue();
                validateNamingConvention(name, nameNode);
            }
        }
    }
    

    private void visitRequestBodyNode(JsonNode requestBodyNode) {
        JsonNode contentNode = requestBodyNode.get("content");
        if (!contentNode.isMissing()) {
            for (JsonNode mediaTypeNode : contentNode.propertyMap().values()) {
                JsonNode schemaNode = mediaTypeNode.get("schema");
                if (!schemaNode.isMissing()) {
                    visitSchemaNode(schemaNode);
                }
            }
        }
    }

    private void visitResponsesNode(JsonNode responsesNode) {
        for (JsonNode responseNode : responsesNode.propertyMap().values()) {
            JsonNode contentNode = responseNode.get("content");
            if (!contentNode.isMissing()) {
                for (JsonNode mediaTypeNode : contentNode.propertyMap().values()) {
                    JsonNode schemaNode = mediaTypeNode.get("schema");
                    if (!schemaNode.isMissing()) {
                        visitSchemaNode(schemaNode);
                    }
                }
            } else {
                JsonNode schemaNode = responseNode.get("schema");
                if (!schemaNode.isMissing()) {
                    visitSchemaNode(schemaNode);
                }
            }
        }
    }
    
        private void visitSchemaNode(JsonNode schemaNode) {
            Map<String, JsonNode> properties = schemaNode.propertyMap();
            if (properties.containsKey("properties")) {
                Map<String, JsonNode> schemaProperties = schemaNode.get("properties").propertyMap();
                for (JsonNode property : schemaProperties.values()) {
                    JsonNode nameNode = property.key();
                    String name = nameNode.getTokenValue();
                    validateNamingConvention(name, nameNode);
                }
            }
        }
    }