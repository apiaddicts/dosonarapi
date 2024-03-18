package apiquality.sonar.openapi.checks.security;

import java.util.Set;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;

@Rule(key = OAR078VerbsSecurityCheck.KEY)
public class OAR078VerbsSecurityCheck extends BaseCheck {

    public static final String KEY = "OAR078";
    private static final String MESSAGE = "OAR078.error";

    private static final Set<String> SECURITY_VERBS = ImmutableSet.of("get", "post", "put", "patch", "delete");
    private boolean hasGlobalSecurity = false;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH, OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    protected void visitFile(JsonNode root) {
        JsonNode globalSecurity = root.get("security");
        if (globalSecurity != null && !globalSecurity.isMissing() && !globalSecurity.isNull() && !globalSecurity.elements().isEmpty()) {
            hasGlobalSecurity = true;
        }
        super.visitFile(root);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (hasGlobalSecurity) return;

        if (node.getType() == OpenApi2Grammar.PATH || node.getType() == OpenApi3Grammar.PATH) {
            visitOperationNode(node);
        } else if (node.getType() == OpenApi2Grammar.OPERATION || node.getType() == OpenApi3Grammar.OPERATION) {
            visitOperationNode(node);
        }
    }

    private void visitOperationNode(JsonNode node) {
        String operationType = node.key().getTokenValue();
        if (!SECURITY_VERBS.contains(operationType.toLowerCase())) {
            return;
        }
        
        JsonNode security = node.get("security");
        if (security.isMissing() || security.isNull() || security.elements().isEmpty()) {
            addIssue(KEY, translate(MESSAGE), node.key());
        }
    }
}