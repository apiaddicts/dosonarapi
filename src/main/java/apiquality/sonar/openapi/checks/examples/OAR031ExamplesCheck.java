package apiquality.sonar.openapi.checks.examples;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

@Rule(key = OAR031ExamplesCheck.KEY)
public class OAR031ExamplesCheck extends BaseCheck {

    public static final String KEY = "OAR031";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.SCHEMA, OpenApi2Grammar.RESPONSES, OpenApi3Grammar.SCHEMA, OpenApi3Grammar.RESPONSES, OpenApi3Grammar.REQUEST_BODY);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        AstNodeType type = node.getType();
        if ( type.equals(OpenApi2Grammar.RESPONSES) || type.equals(OpenApi3Grammar.RESPONSES)) {
            List<JsonNode> responseCodes = node.properties().stream().collect(Collectors.toList());
            for (JsonNode jsonNode : responseCodes) {
                if (!jsonNode.key().getTokenValue().equals("204")) {
                    JsonNode responseNode = jsonNode.resolve();
                    if ( responseNode.getType().equals(OpenApi3Grammar.RESPONSE) ) {
                        visitRequestBodyOrResponseV3Node(responseNode);
                    } else {
                        visitResponseV2Node(responseNode);
                    }
                }
            }
        } else if ( type.equals(OpenApi2Grammar.SCHEMA) || type.equals(OpenApi3Grammar.SCHEMA) ) {
            visitSchemaNode(node);
        }  else if ( type.equals(OpenApi3Grammar.REQUEST_BODY) ) {
            visitRequestBodyOrResponseV3Node(node);
        }
    }

    private void visitResponseV2Node(JsonNode node) {
        if (node.get("examples").isMissing()) {
            addIssue(KEY, translate("OAR031.error-response"), node.key());
        }
    }

    private void visitSchemaNode(JsonNode node) {
        JsonNode parentNode = (JsonNode) node.getParent().getParent();

        if ( parentNode.getType().equals(OpenApi3Grammar.PARAMETER) ) {
            if (node.get("example").isMissing() && parentNode.get("example").isMissing() && parentNode.get("examples").isMissing()) {
                addIssue(KEY, translate("OAR031.error-parameter"), parentNode);
            }
        } else if ( parentNode.getType().equals(OpenApi3Grammar.SCHEMA_PROPERTIES) || 
            parentNode.getType().toString().equals("BLOCK_MAPPING") || parentNode.getType().toString().equals("FLOW_MAPPING") ) {
            JsonNode type = getType(node);
            if (!isObjectType(type) && !type.isMissing() && !isArrayType(type)) {
                if (node.get("example").isMissing()) {
                    addIssue(KEY, translate("OAR031.error-property"), node.key());
                }
            }
        }
    }

    private void visitRequestBodyOrResponseV3Node(JsonNode node) {
        JsonNode content = node.at("/content");
        if (content.isMissing()) return;
        for (JsonNode mediaTypeNode : content.propertyMap().values()) {
            AstNodeType type = node.getType();
            JsonNode schemaNode = mediaTypeNode.get("schema");
            boolean hasSchemaExample = false;
            if (schemaNode.getType().equals(OpenApi3Grammar.SCHEMA) && !schemaNode.get("example").isMissing()) {
                hasSchemaExample = true;
            }
            if (!hasSchemaExample && mediaTypeNode.get("examples").isMissing() && mediaTypeNode.get("example").isMissing()) {
                if ( type.equals(OpenApi3Grammar.REQUEST_BODY) ) {
                    addIssue(KEY, translate("OAR031.error-request"), node.key());
                } else {
                    addIssue(KEY, translate("OAR031.error-response"), node.key());
                }
            }
            
        }
    }
}
