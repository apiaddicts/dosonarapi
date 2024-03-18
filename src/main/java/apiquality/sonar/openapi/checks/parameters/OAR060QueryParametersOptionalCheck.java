package apiquality.sonar.openapi.checks.parameters;


import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import com.google.common.collect.ImmutableSet;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import java.util.Set;


@Rule(key = OAR060QueryParametersOptionalCheck.KEY)
public class OAR060QueryParametersOptionalCheck extends BaseCheck {

    public static final String KEY = "OAR060";
    private static final String MESSAGE = "OAR060.error";

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
        if (inNode != null && "query".equals(inNode.getTokenValue())) {
            JsonNode requiredNode = node.get("required");
            if (requiredNode != null && "true".equals(requiredNode.getTokenValue())) {
                addIssue(KEY, translate(MESSAGE), requiredNode);
            }
        }
    }
}