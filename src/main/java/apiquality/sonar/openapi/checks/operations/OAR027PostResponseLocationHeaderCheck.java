package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR027PostResponseLocationHeaderCheck.KEY)
public class OAR027PostResponseLocationHeaderCheck extends BaseCheck {

    public static final String KEY = "OAR027";
    private static final String MESSAGE = "OAR027.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitOperationNode(node);
    }

    private void visitOperationNode(JsonNode node) {
        if (!"post".equals(node.key().getTokenValue())) return;
        JsonNode node201 = node.get("responses").get("201");
        if (node201.isMissing()) return;
        JsonNode headers = node201.resolve().get("headers");
        if (headers.isMissing()) {
            addIssue(KEY, translate(MESSAGE), node201.key());
        } else if (headers.get("Location").isMissing() && headers.get("location").isMissing()) {
            addIssue(KEY, translate(MESSAGE), headers.key());
        }
    }

}
