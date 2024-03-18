package apiquality.sonar.openapi.checks.schemas;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import apiquality.sonar.openapi.checks.BaseCheck;

@Rule(key = OAR080SecuritySchemasCheck.KEY)
public class OAR080SecuritySchemasCheck extends BaseCheck {

    public static final String KEY = "OAR080";
    private static final String MESSAGE = "OAR080.error";

    private static final Set<String> SECURITY_VERBS = ImmutableSet.of("get", "post", "put", "patch", "delete");

    private static final String SECURITY_SCHEMES = "oauth2, apiKey";

    @RuleProperty(
        key = "expected-security-scheme",
        description = "Expected security schemes",
        defaultValue = SECURITY_SCHEMES
    )
    private String expectedSecurityScheme = SECURITY_SCHEMES;

    private Set<String> expectedSecuritySchemes;
    private boolean hasGlobalSecurity = false;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(
            OpenApi2Grammar.PATH,
            OpenApi3Grammar.PATH,
            OpenApi2Grammar.OPERATION,
            OpenApi3Grammar.OPERATION
        );
    }

    @Override
    protected void visitFile(JsonNode root) {
        JsonNode security = root.get("security");
        hasGlobalSecurity = !(security.isMissing() || security.isNull() || security.elements().isEmpty());

        expectedSecuritySchemes = Arrays.stream(expectedSecurityScheme.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        super.visitFile(root);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (hasGlobalSecurity) return;  

        if (node.is(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH)) {
            visitOperationNode(node);
        } else if (node.is(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION)) {
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
            return;
        }

        Set<String> actualSecuritySchemes = security.elements().stream()
                .map(JsonNode::propertyMap)
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        if (!expectedSecuritySchemes.containsAll(actualSecuritySchemes) || !actualSecuritySchemes.containsAll(expectedSecuritySchemes)) {
            addIssue(KEY, translate(MESSAGE), node.key());
        }
    }
}