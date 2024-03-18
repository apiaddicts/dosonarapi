package apiquality.sonar.openapi.checks.schemas;

import com.sonar.sslr.api.AstNode;

import org.json.JSONArray;
import org.json.JSONObject;
import apiquality.sonar.openapi.checks.BaseCheck;
import apiquality.sonar.openapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

public abstract class AbstractSchemaCheck extends BaseCheck {

    protected String key;

    public AbstractSchemaCheck(String key) {
        this.key = key;
    }

    protected Map<String, JsonNode> getAllProperties(JsonNode schemaNode) {
        final Map<String, JsonNode> properties = new HashMap<>();

        JsonNode propertiesNode = getProperties(schemaNode);
        if (!propertiesNode.isMissing()) {
            Map<String, JsonNode> baseProperties = getProperties(schemaNode).propertyMap();
            properties.putAll(baseProperties);
        }

        JsonNode allOfNode = schemaNode.get("allOf");
        if (!allOfNode.isMissing()) {
            allOfNode.elements().stream()
                    .map(JsonNodeUtils::resolve)
                    .forEach(node -> properties.putAll(getAllProperties(node)));
        }

        return properties;
    }

    protected Optional<JsonNode> validateProperty(Map<String, JsonNode> properties, String propertyName, String propertyType, JsonNode parentNode) {
        if (!properties.containsKey(propertyName)) {
            addIssue(key, translate("generic.property-missing", propertyName), parentNode);
            return Optional.empty();
        }
        JsonNode prop = properties.get(propertyName);
        return validateProperty(prop, propertyName, propertyType, parentNode);
    }

    protected Optional<JsonNode> validateProperty(JsonNode properties, String propertyName, String propertyType) {
        properties = resolve(properties);

        if (properties.propertyNames().isEmpty()) {
            addIssue(key, translate("generic.property-missing", propertyName), properties);
            return Optional.empty();
        }

        JsonNode prop = properties.get(propertyName);
        return validateProperty(prop, propertyName, propertyType, properties.key());
    }

    protected Optional<JsonNode> validateProperty(JsonNode prop, String propertyName, String propertyType, JsonNode parentNode) {
        if (prop.isMissing()) {
            addIssue(key, translate("generic.property-missing", propertyName), parentNode);
            return Optional.empty();
        } else {
            prop = resolve(prop);
            JsonNode type = getType(prop);
            if (!isType(type, propertyType)) {
                JsonNode errorNode = (type.isMissing() ? prop : type.key());
                addIssue(key, translate("generic.property-wrong-type", propertyName, propertyType), errorNode);
                return Optional.empty();
            }
        }
        return Optional.of(prop);
    }

    protected void validateRequiredProperties(JsonNode schema, Set<String> requiredValues, String requiredStr) {
        JsonNode required = getRequired(schema);
        Set<String> requiredProperties = getRequiredValues(required);
        if (!requiredProperties.containsAll(requiredValues)) {
            JsonNode errorNode = (required.isMissing() ? schema : required.key());
            addIssue(key, translate("generic.required-properties", requiredStr), errorNode);
        }
    }

    protected Optional<JsonNode> validateItems(JsonNode prop, String iType) {
        prop = resolve(prop);
        JsonNode type = getType(prop);
        if (isType(type, TYPE_ARRAY)) {
            JsonNode itemsSchema = prop.get("items");
            itemsSchema = resolve(itemsSchema);
            String propertyName = prop.key().getTokenValue();
            if (itemsSchema.isMissing()) {
                addIssue(key, translate("generic.property-items-missing", propertyName, iType), prop.key());
            } else {
                JsonNode itemsType = getType(itemsSchema);
                if (!isType(itemsType, iType)) {
                    JsonNode errorNode = (itemsType.isMissing() ? itemsSchema : itemsType.key());
                    addIssue(key, translate("generic.property-items-wrong-type", propertyName, iType), errorNode);
                    return Optional.empty();
                } else {
                    return Optional.of(itemsSchema);
                }
            }
        }
        return Optional.empty();
    }

    protected void validateEnumValues(JsonNode property, Set<String> expected) {
        Set<String> found = getEnumValues(property);
        if (!expected.equals(found)) {
            String propertyName = property.key().getTokenValue();
            String values = expected.stream().sorted().collect(Collectors.joining(", "));
            addIssue(key, translate("generic.enum-values", propertyName, values), property.key());
        }
    }

    private Set<String> getEnumValues(JsonNode schema) {
        return schema.get("enum").elements()
                .stream().map(AstNode::getTokenValue).collect(Collectors.toSet());
    }

    protected void validateProperties(String propertyName, JSONObject propertySchema, JsonNode propertiesNode) {
        Map<String, JsonNode> propertyMap = getAllProperties(propertiesNode);
        String schemaType = (propertySchema != null && propertySchema.has("type")) ? propertySchema.getString("type") : TYPE_ANY;
        if (schemaType == null || schemaType.trim().isEmpty() || schemaType.equals("any")) schemaType = TYPE_ANY;
        if (schemaType.equals(TYPE_ANY) && propertySchema.has("properties")) schemaType = TYPE_OBJECT;

        if (schemaType != null && !schemaType.trim().isEmpty()) {
            if (schemaType.equals(TYPE_OBJECT)) {
                validateProperty(propertyMap, propertyName, TYPE_OBJECT, propertiesNode.key()).ifPresent(propertyNode -> {                    
                    validateObject(propertySchema, propertyNode);
                });
            } else if (schemaType.equals(TYPE_ARRAY)) {
                validateProperty(propertyMap, propertyName, TYPE_ARRAY, propertiesNode.key()).ifPresent(propertyNode -> {
                    validateArray(propertySchema, propertyNode);
                });
            } else {
                validateProperty(propertyMap, propertyName, schemaType, propertiesNode.key());
            }
        }
    }

    private void validateObject(JSONObject propertySchema, JsonNode propertyNode) {
        JSONArray schemaRequired = (propertySchema != null && propertySchema.has("required")) ? propertySchema.getJSONArray("required") : null;
        JSONObject schemaProperties = (propertySchema != null && propertySchema.has("properties")) ? propertySchema.getJSONObject("properties") : null;

        if (schemaProperties != null && !schemaProperties.keySet().isEmpty()) {
            List<String> sortedSchemaProperties = new ArrayList<>(schemaProperties.keySet());
            Collections.sort(sortedSchemaProperties);
            sortedSchemaProperties.forEach(childProperty -> {
                JSONObject childPropertySchema = (schemaProperties != null && schemaProperties.has(childProperty)) ? schemaProperties.getJSONObject(childProperty) : null;
                validateProperties(childProperty, childPropertySchema, propertyNode);
            });
        }
        
        if (schemaRequired != null && schemaRequired.length() > 0 ) {
            Set<String> requiredProperties = schemaRequired.toList().stream().map(element -> (String) element).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
            validateRequiredProperties(propertyNode, requiredProperties, String.join(", ", requiredProperties));
        }
    }

    private void validateArray(JSONObject propertySchema, JsonNode propertyNode) {
        JSONObject schemaItems = (propertySchema != null && propertySchema.has("items")) ? propertySchema.getJSONObject("items") : null;
        String itemsType = (schemaItems != null && schemaItems.has("type")) ? schemaItems.getString("type") : TYPE_ANY;
        if (itemsType == null || itemsType.trim().isEmpty() || itemsType.equals("any")) itemsType = TYPE_ANY;
        if (itemsType.equals(TYPE_ANY) && schemaItems.has("properties")) itemsType = TYPE_OBJECT;
        
        if (itemsType.equals(TYPE_OBJECT)) {
            validateItems(propertyNode, itemsType).ifPresent(itemNode -> {
                validateObject(schemaItems, itemNode);
            });
        } else if (itemsType.equals(TYPE_ARRAY)) {
            validateItems(propertyNode, itemsType).ifPresent(itemNode -> {
                validateArray(schemaItems, itemNode);
            });
        } else {
            validateItems(propertyNode, itemsType);
        }
    }
}
