package apiquality.sonar.openapi.checks.parameters;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import apiquality.sonar.openapi.checks.BaseCheck;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(key = OAR019SelectParameterCheck.KEY)
public class OAR019SelectParameterCheck extends BaseCheck {

    public static final String KEY = "OAR019";
    private static final String MESSAGE = "OAR019.error";
    private static final String DEFAULT_PATH = "/examples";
    private static final String PATH_STRATEGY = "/include";
    private static final String PARAM_NAME = "$select";

    @RuleProperty(
        key = "paths",
        description = "List of explicit paths to include/exclude from this rule separated by comma",
        defaultValue = DEFAULT_PATH
    )
    private String pathsStr = DEFAULT_PATH;

    @RuleProperty(
        key = "pathValidationStrategy",
        description = "Path validation strategy (include/exclude)",
        defaultValue = PATH_STRATEGY
    )
    private String pathCheckStrategy = PATH_STRATEGY;

    @RuleProperty(
        key = "parameterName",
        description = "Name of the parameter to be checked",
        defaultValue = PARAM_NAME
    )
    private String parameterName = PARAM_NAME;

    private Set<String> paths;
    private JsonNode rootNode;  // Variable de instancia para almacenar el nodo raíz

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    protected void visitFile(JsonNode root) {
        this.rootNode = root;  // Almacenamos el nodo raíz
        paths = parsePaths(pathsStr);
        System.out.println("Visiting file...");
        System.out.println("Parsing paths...");
        super.visitFile(root);
    }

    @Override
    public void visitNode(JsonNode node) {
        System.out.println("Visiting node: " + node.key().getTokenValue());
        if ("get".equals(node.key().getTokenValue())) {
            System.out.println("It's a GET operation");

            String path = getPath(node);
            System.out.println("Extracting path...\nPath found: " + path);

            boolean hasParameter = hasParameterInNode(node);

            System.out.println("Checking if should include path: " + path);
            if (shouldIncludePath(path) && !hasParameter) {
                System.out.println("Issue added for path: " + path);
                System.out.println("Checking conditions for adding issue...");
                System.out.println("Should include path: " + shouldIncludePath(path));
                System.out.println("Has parameter: " + hasParameter);
                addIssue(KEY, translate(MESSAGE, PARAM_NAME), node.key());
            }
        }
    }

    private boolean hasParameterInNode(JsonNode node) {
        JsonNode parametersNode = node.get("parameters");
        if (parametersNode != null) {
            System.out.println("Parameters found");

            for (JsonNode parameterNode : parametersNode.elements()) {
                if (isRefParameter(parameterNode) && hasNamedRefParameter(parameterNode)) {
                    return true;
                } else if (hasDirectParameter(parameterNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRefParameter(JsonNode parameterNode) {
        JsonNode refNode = parameterNode.get("$ref");
        if (refNode != null) {
            System.out.println("Detected a $ref parameter node");
            return true;
        }
        return false;
    }

    private boolean hasNamedRefParameter(JsonNode parameterNode) {
        String refValue = parameterNode.get("$ref").getTokenValue();
        JsonNode refParameterNode = resolveReference(refValue, rootNode);  
        if (refParameterNode != null) {
            JsonNode nameNode = refParameterNode.get("name");
            JsonNode inNode = refParameterNode.get("in"); 
            return inNode != null && "query".equals(inNode.getTokenValue()) && nameNode != null && parameterName.equals(nameNode.getTokenValue());
        }
        return false;
    }

    private boolean hasDirectParameter(JsonNode parameterNode) {
        System.out.println("Processing a direct parameter node");
        JsonNode nameNode = parameterNode.get("name");
        JsonNode inNode = parameterNode.get("in");
        return inNode != null && "query".equals(inNode.getTokenValue()) && nameNode != null && parameterName.equals(nameNode.getTokenValue());
    }

    private String getPath(JsonNode node) {
        StringBuilder pathBuilder = new StringBuilder();
        AstNode pathNode = node.getFirstAncestor(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH);
        if (pathNode != null) {
            while (pathNode.getType() != OpenApi2Grammar.PATH && pathNode.getType() != OpenApi3Grammar.PATH) {
                pathNode = pathNode.getParent();
            }
            pathBuilder.append(((JsonNode) pathNode).key().getTokenValue());
        }
        return pathBuilder.toString();
    }

    private boolean shouldIncludePath(String path) {
        if (pathCheckStrategy.equals("/exclude")) {
            return !paths.contains(path);
        } else if (pathCheckStrategy.equals("/include")) {
            return paths.contains(path);
        }
        return false;
    }

      private Set<String> parsePaths(String pathsStr) {
        if (!pathsStr.trim().isEmpty()) {
            return Arrays.stream(pathsStr.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    private JsonNode resolveReference(String refValue, JsonNode root) {
        if (refValue == null || !refValue.startsWith("#/")) { 
            return null; 
        }
    
        String pathToReference = refValue.substring(2);
        String[] pathParts = pathToReference.split("/");
    
        JsonNode currentNode = root;
        for (String part : pathParts) {
            if (currentNode == null) {
                return null; 
            }
            currentNode = currentNode.get(part);
            System.out.println("Navigating to: " + part);
        }
    
        if (currentNode == null) {
            System.out.println("Resolved reference node is null for ref: " + refValue);
        } else {
            System.out.println("Resolved reference node for ref: " + refValue + " is: " + currentNode.toString());
        }
    
        return currentNode;
    }
}