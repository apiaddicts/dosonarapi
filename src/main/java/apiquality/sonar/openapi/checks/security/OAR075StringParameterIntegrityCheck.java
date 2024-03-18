package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR075StringParameterIntegrityCheck.KEY)
public class OAR075StringParameterIntegrityCheck extends BaseCheck {

    public static final String KEY = "OAR075";
    private static final String MESSAGE = "OAR075.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PARAMETER, OpenApi3Grammar.PARAMETER);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (node.getType() instanceof OpenApi2Grammar) {
            visitSwaggerParameterNode(node);
        } else {
            visitParameterNode(node);
        }
    }

    public void visitParameterNode(JsonNode node) {
        JsonNode schemaNode = node.get("schema");

        if (schemaNode != null) {
            JsonNode typeNode = schemaNode.get("type");

            boolean isStringType = typeNode != null && "string".equals(typeNode.getTokenValue());

            if (isStringType) {
                JsonNode minLengthNode = schemaNode.get("minLength");
                JsonNode maxLengthNode = schemaNode.get("maxLength");
                JsonNode patternNode = schemaNode.get("pattern");
                JsonNode enumNode = schemaNode.get("enum");
                JsonNode formatNode = schemaNode.get("format");

                boolean lacksLengthRestriction = minLengthNode.isMissing() != maxLengthNode.isMissing();  
                boolean lacksRestriction = (lacksLengthRestriction || (patternNode.isMissing() && enumNode.isMissing() && formatNode.isMissing())); 
                if (lacksRestriction) {
                    addIssue(KEY, translate(MESSAGE), typeNode);
                }
            }
        }
    }

    public void visitSwaggerParameterNode(JsonNode node) {
        JsonNode typeNode = node.get("type");

        boolean isStringType = typeNode != null && "string".equals(typeNode.getTokenValue());

        if (isStringType) {
            JsonNode minLengthNode = node.get("minLength");
            JsonNode maxLengthNode = node.get("maxLength");
            JsonNode patternNode = node.get("pattern");
            JsonNode enumNode = node.get("enum");
            JsonNode formatNode = node.get("format");

            boolean lacksLengthRestriction = minLengthNode.isMissing() != maxLengthNode.isMissing();  
            boolean lacksRestriction = (lacksLengthRestriction || (patternNode.isMissing() && enumNode.isMissing() && formatNode.isMissing())); 
            if (lacksRestriction) {
                addIssue(KEY, translate(MESSAGE), typeNode);
            }
        }
    }
}