package apiquality.sonar.openapi.checks.parameters;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR026TotalParameterDefaultValueCheck.KEY)
public class OAR026TotalParameterDefaultValueCheck extends BaseCheck {

    protected static final String KEY = "OAR026";
    private static final String MESSAGE = "OAR026.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PARAMETER, OpenApi3Grammar.PARAMETER);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        if (!"$total".equals(node.get("name").getTokenValue())) return;
        JsonNode defaultNode = ( node.getType() == OpenApi2Grammar.PARAMETER ) ? node.get("default") : node.at("/schema/default");
        if (defaultNode.isMissing()) {
            if (node.key().isMissing()) {
                addIssue(KEY, translate(MESSAGE), node);
            } else {
                addIssue(KEY, translate(MESSAGE), node.key());
            }
        } else if (!"false".equals(defaultNode.getTokenValue())) {
            addIssue(KEY, translate(MESSAGE), defaultNode);
        }
    }
}