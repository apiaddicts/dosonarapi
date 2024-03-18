package apiquality.sonar.openapi.checks.examples;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR094UseExamplesCheck.KEY)
public class OAR094UseExamplesCheck extends BaseCheck {

    public static final String KEY = "OAR094";
    private static final String MESSAGE = "OAR094.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.ROOT, OpenApi3Grammar.ROOT);
    }

    @Override
    public void visitNode(JsonNode node) {
        deepSearchForExample(node);
    }

    private void deepSearchForExample(JsonNode node) {
        if (node.propertyMap().containsKey("example")) {
            addIssue(KEY, translate(MESSAGE), node.propertyMap().get("example").key());
            return; 
        }
        
        // Recurse into children
        for (JsonNode child : node.propertyMap().values()) {
            deepSearchForExample(child);
        }
    }
}