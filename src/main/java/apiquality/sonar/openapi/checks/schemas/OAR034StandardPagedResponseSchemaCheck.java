package apiquality.sonar.openapi.checks.schemas;

import org.json.JSONException;
import org.json.JSONObject;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

@Rule(key = OAR034StandardPagedResponseSchemaCheck.KEY)
public class OAR034StandardPagedResponseSchemaCheck extends AbstractExplicitResponseCheck {

    public static final String KEY = "OAR034";
    private static final String PAGING_SCHEMA = "{\"type\":\"object\",\"properties\":{\"numPages\":{\"type\":\"integer\"},\"total\":{\"type\":\"integer\"},\"start\":{\"type\":\"integer\"},\"limit\":{\"type\":\"integer\"},\"links\":{\"type\":\"object\",\"properties\":{\"next\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"previous\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"last\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"self\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"first\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}}},\"required\":[\"self\",\"previous\",\"next\"]}},\"required\":[\"start\",\"limit\",\"links\"],\"pagingPropertyName\":\"paging\"}";

    @RuleProperty(
            key = "paging-schema",
            description = "Response Schema as String",
            defaultValue = PAGING_SCHEMA
    )
    private String pagingSchemaStr = PAGING_SCHEMA;
    private JSONObject pagingSchema;

    private String pagingPropertyName = null;

    public OAR034StandardPagedResponseSchemaCheck() {
        super(KEY, "206");
    }

    @Override
    protected void visitFile(JsonNode root) {
        try {
            pagingSchema = new JSONObject(pagingSchemaStr);
            pagingPropertyName = (pagingSchema.has("pagingPropertyName")) ? pagingSchema.getString("pagingPropertyName") : null;
        } catch (JSONException err) {
			addIssue(KEY, "Error parsing Standard Response Schemas", root.key());
        }
    }

    @Override
    protected void visitV2ExplicitNode(JsonNode node) {
        JsonNode schemaNode = node.get("schema");
        if (schemaNode.isMissing()) return;
        schemaNode = resolve(schemaNode);
        validateProperties(pagingPropertyName, pagingSchema, schemaNode);
    }
}
