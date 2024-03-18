package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = OAR084ForbiddenFormatsInQueryCheck.KEY)
public class OAR084ForbiddenFormatsInQueryCheck extends BaseCheck {

    public static final String KEY = "OAR084";
    private static final String MESSAGE = "OAR084.error";
    private static final String FORBIDDEN_QUERY_FORMATS = "password";
    private static final String DEFAULT_PATH = "/examples";
    private static final String PATH_STRATEGY = "/include";

    @RuleProperty(
            key = "forbidden-query-formats",
            description = "List of forbidden query params separated by comma",
            defaultValue = FORBIDDEN_QUERY_FORMATS
    )
    private String forbiddenQueryFormatsStr = FORBIDDEN_QUERY_FORMATS;

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

    private Set<String> forbiddenQueryFormats = new HashSet<>();
    private Set<String> paths;
    private String currentPath;

    @Override
    protected void visitFile(JsonNode root) {
        if (!forbiddenQueryFormatsStr.trim().isEmpty()) {
            forbiddenQueryFormats.addAll(Stream.of(forbiddenQueryFormatsStr.split(",")).map(String::trim).collect(Collectors.toSet()));
        }
        if (!pathsStr.trim().isEmpty()) {
            paths = Stream.of(pathsStr.split(",")).map(String::trim).collect(Collectors.toSet());
        } else {
            paths = new HashSet<>();
        }
        super.visitFile(root);
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH, OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (node.getType() == OpenApi2Grammar.PATH || node.getType() == OpenApi3Grammar.PATH) {
            currentPath = node.key().getTokenValue();
        } else if (node.getType() == OpenApi2Grammar.OPERATION || node.getType() == OpenApi3Grammar.OPERATION) {
            if (shouldExcludePath()) {
                return;
            }

            String operationType = node.key().getTokenValue();
            if (!ImmutableSet.of("get", "post", "put", "patch", "delete").contains(operationType.toLowerCase())) {
                return;
            }

            JsonNode parametersNode = node.get("parameters");
            if (parametersNode == null || parametersNode.isNull()) {
                return;
            }
            
            List<JsonNode> forbiddenFormatNodes = new ArrayList<>();

            parametersNode.elements().forEach(parameterNode -> {
                JsonNode inNode = parameterNode.get("in");
                if (inNode != null && "query".equals(inNode.getTokenValue())) {
                    JsonNode formatNode;

                    if (node.getType() == OpenApi2Grammar.OPERATION) {
                        formatNode = parameterNode.get("format");
                    } else {
                        JsonNode schemaNode = parameterNode.get("schema");
                        if (schemaNode == null || schemaNode.isNull()) {
                            return;
                        }
                        formatNode = schemaNode.get("format");
                    }

                    if (formatNode != null && !formatNode.isNull() && forbiddenQueryFormats.contains(formatNode.getTokenValue())) {
                        forbiddenFormatNodes.add(formatNode);
                    }
                }
            });

            if (!forbiddenFormatNodes.isEmpty()) {
                String forbiddenFormatsStr = forbiddenFormatNodes.stream()
                    .map(JsonNode::getTokenValue)
                    .collect(Collectors.joining(", "));

                for (JsonNode formatNode : forbiddenFormatNodes) {
                    addIssue(KEY, translate(MESSAGE, forbiddenFormatsStr), formatNode);
                }
            }
        }
    }

    private boolean shouldExcludePath() {
        if ("/exclude".equals(pathCheckStrategy)) {
            return paths.contains(currentPath);
        } else if ("/include".equals(pathCheckStrategy)) {
            return !paths.contains(currentPath);
        }
        return false;
    }
}