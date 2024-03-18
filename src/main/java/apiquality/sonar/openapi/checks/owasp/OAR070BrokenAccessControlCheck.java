package apiquality.sonar.openapi.checks.owasp;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR070BrokenAccessControlCheck.KEY)
public class OAR070BrokenAccessControlCheck extends BaseCheck {

    public static final String KEY = "OAR070";
    private static final String MESSAGE = "OAR070.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PARAMETER, OpenApi3Grammar.PARAMETER);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitParameterNode(node);
    }

    public void visitParameterNode(JsonNode node) {
        JsonNode inNode = node.get("in");
        if (inNode != null && "path".equals(inNode.getTokenValue())) {
            JsonNode nameNode = node.get("name");
            JsonNode typeNode = node.get("type");
            JsonNode schemaNode = node.get("schema");
    
            boolean isNumericType = typeNode != null && ("integer".equals(typeNode.getTokenValue()) ||
                "number".equals(typeNode.getTokenValue()) ||
                "float".equals(typeNode.getTokenValue()));
    
            if (!isNumericType && schemaNode != null) {
                JsonNode schemaTypeNode = schemaNode.get("type");
                isNumericType = schemaTypeNode != null && ("integer".equals(schemaTypeNode.getTokenValue()) ||
                    "number".equals(schemaTypeNode.getTokenValue()) ||
                    "float".equals(schemaTypeNode.getTokenValue()));
                typeNode = schemaTypeNode; 
            }
    
            if (nameNode != null && isNumericType) {
                addIssue(KEY, translate(MESSAGE), typeNode);
            }
        }
    }
}