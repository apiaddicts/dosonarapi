package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;

@Rule(key = OAR111ContactInformationCheck.KEY)
public class OAR111ContactInformationCheck extends BaseCheck {

    public static final String KEY = "OAR111";
    private static final String MESSAGE = "OAR111.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.INFO, OpenApi3Grammar.INFO);
    }

    @Override
    public void visitNode(JsonNode node) {
        validateInfo(node);
    }

    private void validateInfo(JsonNode infoNode) {
        if (infoNode == null || infoNode.isMissing()) {
            return;
        }

        List<JsonNode> infoChildren = infoNode.getJsonChildren();

        for (JsonNode child : infoChildren) {
            String propertyName = child.key().getTokenValue();
            if ("contact".equals(propertyName)) {
                validateContactNode(child);
                break;
            }
        }
    }

    private void validateContactNode(JsonNode contactNode) {
        if (contactNode == null || contactNode.isMissing()) {
            return;
        }
    
        List<JsonNode> children = contactNode.getJsonChildren();
    
        boolean isEmptyContact = true;
        for (JsonNode child : children) {
            if (child.toString().contains("BLOCK_MAPPING") || child.toString().contains("FLOW_MAPPING")) {
                isEmptyContact = false;
                break;
            }
        }
    
        if (isEmptyContact) {
            addIssue(KEY, translate(MESSAGE), contactNode.key());
        }
    }
    
}
