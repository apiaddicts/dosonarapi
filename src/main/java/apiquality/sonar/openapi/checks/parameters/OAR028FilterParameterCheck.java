package apiquality.sonar.openapi.checks.parameters;

import java.util.Set;
import java.util.stream.Collectors;

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

@Rule(key = OAR028FilterParameterCheck.KEY)
public class OAR028FilterParameterCheck extends BaseCheck {

    public static final String KEY = "OAR028";
    private static final String MESSAGE = "OAR028.error";
    private static final String DEFAULT_PATH = "/examples";
    private static final String PATH_STRATEGY = "/include";
    private static final String PARAM_NAME = "$filter";

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

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PARAMETER, OpenApi3Grammar.PARAMETER);
    }

    @Override
    protected void visitFile(JsonNode root) {
        paths = parsePaths(pathsStr);
        super.visitFile(root);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitParameterNode(node);
    }

    public void visitParameterNode(JsonNode node) {
        JsonNode inNode = node.get("in");
        JsonNode nameNode = node.get("name");

        if (inNode != null && nameNode != null) {
            String path = getPath(node);
            if (shouldExcludePath(path) && !parameterName.equals(nameNode.getTokenValue())) {
                addIssue(KEY, translate(MESSAGE, parameterName), nameNode);
            }
        }
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

    private boolean shouldExcludePath(String path) {
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
}