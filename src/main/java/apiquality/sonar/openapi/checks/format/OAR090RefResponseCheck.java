package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR090RefResponseCheck.KEY)
public class OAR090RefResponseCheck extends BaseCheck {

    public static final String KEY = "OAR090";
    private static final String MESSAGE = "OAR090.error";
    private static final String DEFAULT_SUFFIX = "Response";

    @RuleProperty(
            key = "default-suffix",
            description = "Suffix for the $ref in response or response schema",
            defaultValue = DEFAULT_SUFFIX
    )
    private String responseSuffix = DEFAULT_SUFFIX;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        JsonNode responsesNode = node.get("responses");
        if (responsesNode == null || responsesNode.isMissing()) return;

        for (JsonNode responseNode : responsesNode.propertyMap().values()) {
            JsonNode refNode = responseNode.get("$ref");
            checkRef(refNode);

            JsonNode schemaNode = responseNode.get("schema");
            if (schemaNode != null && !schemaNode.isMissing()) {
                JsonNode refNodeSwagger = schemaNode.get("$ref");
                checkRef(refNodeSwagger);
            }
        }
    }

    private void checkRef(JsonNode refNode) {
        if (refNode != null && !refNode.isMissing()) {
            String refValue = refNode.getTokenValue();
            if (!refValue.endsWith(responseSuffix)) {
                addIssue(KEY, translate(MESSAGE, responseSuffix), refNode.key());
            }
        }
    }
}