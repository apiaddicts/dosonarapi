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

@Rule(key = OAR110LicenseInformationCheck.KEY)
public class OAR110LicenseInformationCheck extends BaseCheck {

    public static final String KEY = "OAR110";
    private static final String MESSAGE = "OAR110.error";

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

        // Esta es una aproximación basada en tu implementación de getJsonChildren.
        List<JsonNode> infoChildren = infoNode.getJsonChildren();

        for (JsonNode child : infoChildren) {
            String propertyName = child.key().getTokenValue();
            if ("license".equals(propertyName)) {
                validateLicenseNode(child);
                break;
            }
        }

    }

    private void validateLicenseNode(JsonNode licenseNode) {
        if (licenseNode == null || licenseNode.isMissing()) {
            return;
        }
    
        List<JsonNode> children = licenseNode.getJsonChildren();
    
        boolean isEmptyLicense = true;
        for (JsonNode child : children) {
            if (child.toString().contains("BLOCK_MAPPING") || child.toString().contains("FLOW_MAPPING")) {
                isEmptyLicense = false;
                break;
            }
        }
    
        if (isEmptyLicense) {
            addIssue(KEY, translate(MESSAGE), licenseNode.key());
        } 
    }
    
}
