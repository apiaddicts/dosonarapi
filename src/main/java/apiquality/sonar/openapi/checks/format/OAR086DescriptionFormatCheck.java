package apiquality.sonar.openapi.checks.format;

import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import com.google.common.collect.ImmutableSet;

@Rule(key = OAR086DescriptionFormatCheck.KEY)
public class OAR086DescriptionFormatCheck extends BaseCheck {

    public static final String KEY = "OAR086";
    private static final String MESSAGE = "OAR086.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.ROOT, OpenApi3Grammar.ROOT, OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS, OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.ROOT.equals(node.getType()) || OpenApi3Grammar.ROOT.equals(node.getType())) {
            checkInfoDescription(node);
            checkDefinitionsDescription(node);
        }
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitPathsNode(node);
        }
    }

    private void checkInfoDescription(JsonNode rootNode) {
        JsonNode infoNode = rootNode.get("info");
        if (infoNode != null) {
            checkDescriptionFormat(infoNode.get("description"));
        }
    }

    private void checkDefinitionsDescription(JsonNode rootNode) {
        JsonNode definitionsNode = rootNode.get("definitions");
        if (definitionsNode != null) {
            for (JsonNode definition : definitionsNode.propertyMap().values()) {
                checkDescriptionFormat(definition.get("description"));
            }
        }
        
        // This is for OpenAPI 3.0's 'components/schemas'
        JsonNode componentsNode = rootNode.get("components");
        if (componentsNode != null) {
            JsonNode schemasNode = componentsNode.get("schemas");
            if (schemasNode != null) {
                for (JsonNode schema : schemasNode.propertyMap().values()) {
                    checkDescriptionFormat(schema.get("description"));
                }
            }
        }
    }

    protected void visitPathsNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) {
            for (JsonNode operationNode : pathNode.propertyMap().values()) {
                checkDescriptionFormat(operationNode.get("description"));

                // Check descriptions in responses
                JsonNode responsesNode = operationNode.get("responses");
                if (responsesNode != null) {
                    for (JsonNode responseNode : responsesNode.propertyMap().values()) {
                        checkDescriptionFormat(responseNode.get("description"));
                    }
                }
            }
        }
    }

    private void checkDescriptionFormat(JsonNode descriptionNode) {
        // Verifica primero si el nodo de descripción es nulo o si el valor está ausente.
        if (descriptionNode == null || descriptionNode.isMissing()) {
            // No hacer nada si el nodo está ausente, ya que eso está permitido según tu criterio.
            return;
        }
    
        String description = descriptionNode.getTokenValue();
        // Asegúrate de manejar el caso en que getTokenValue() devuelva null.
        description = description == null ? "" : description.trim();
    
        // Ahora verifica si la descripción está vacía después de eliminar espacios en blanco.
        if (description.isEmpty()) {
            addIssue(KEY, translate(MESSAGE), descriptionNode);
            return;
        }
    
        // Verifica que la descripción comience con mayúscula y termine con punto
        if (!Character.isUpperCase(description.charAt(0)) || !description.endsWith(".")) {
            addIssue(KEY, translate(MESSAGE), descriptionNode);
        }
    }
}