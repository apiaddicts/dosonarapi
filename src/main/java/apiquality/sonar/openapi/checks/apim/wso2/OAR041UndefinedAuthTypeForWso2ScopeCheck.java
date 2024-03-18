package apiquality.sonar.openapi.checks.apim.wso2;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR041UndefinedAuthTypeForWso2ScopeCheck.KEY)
public class OAR041UndefinedAuthTypeForWso2ScopeCheck extends BaseCheck {

    public static final String KEY = "OAR041";
    private static final String MESSAGE = "OAR041.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        JsonNode scopeNode = node.get("x-scope");
        if (scopeNode.isMissing()) return;
        JsonNode authTypeNode = node.get("x-auth-type");
        if (authTypeNode.isMissing()) {
            addIssue(KEY, translate(MESSAGE), scopeNode.key());
        }
    }
}
