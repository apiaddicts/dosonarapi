package apiquality.sonar.openapi.checks.operations;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import apiquality.sonar.openapi.checks.schemas.AbstractExplicitResponseCheck;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

@Rule(key = OAR038StandardCreateResponseCheck.KEY)
public class OAR038StandardCreateResponseCheck extends AbstractExplicitResponseCheck {

    public static final String KEY = "OAR038";

    private static final String DATA_PROPERTY = "data";

    @RuleProperty(
            key = "data-property",
            description = "Data property in standard response.",
            defaultValue = DATA_PROPERTY
    )
    private String dataNode = DATA_PROPERTY;

    public OAR038StandardCreateResponseCheck() {
        super(KEY, "201");
    }

    @Override
    protected void visitV2ExplicitNode(JsonNode node) {
        JsonNode schemaNode = node.get("schema");
        if (schemaNode.isMissing()) {
            addIssue(KEY, translate("OAR038.error-required-schema"), node.key());
        } else {
            schemaNode = resolve(schemaNode);

            Map<String, JsonNode> properties = getAllProperties(schemaNode);

            validateProperty(properties, dataNode, TYPE_ANY, schemaNode.key())
                    .ifPresent(this::validateData);
        }
    }

    private void validateData(JsonNode data) {
        Map<String, JsonNode> properties = getAllProperties(data);
        if (properties.isEmpty()) {
            addIssue(KEY, translate("OAR038.error-required-one-property"), data.key());
        }
    }
}
