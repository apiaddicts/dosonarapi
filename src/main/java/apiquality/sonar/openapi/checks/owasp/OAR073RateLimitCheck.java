package apiquality.sonar.openapi.checks.owasp;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(key = OAR073RateLimitCheck.KEY)
public class OAR073RateLimitCheck extends BaseCheck {

    public static final String KEY = "OAR073";
    private static final String MESSAGE = "OAR073.error";
    private static final String DEFAULT_PATH = "/status, /health-check";
    private static final String PATH_STRATEGY = "/exclude";

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

    private Set<String> exclusion;
    private String currentPath;

    @Override
    protected void visitFile(JsonNode root) {
        exclusion = parsePaths(pathsStr);
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
            if (shouldSkipNode(currentPath)) {
                return;
            }
            visitOperationNode(node);
        }
    }

    private boolean shouldSkipNode(String currentPath) {
        if (pathCheckStrategy.equals("/exclude")) {
            return exclusion.contains(currentPath);
        } else if (pathCheckStrategy.equals("/include")) {
            return !exclusion.contains(currentPath);
        }
        return false;
    }

    private void visitOperationNode(JsonNode node) {
        JsonNode responsesNode = node.get("responses");
        if (responsesNode != null) {
            JsonNode node429 = responsesNode.get("429");
            if (node429.isMissing()) {
                addIssue(KEY, translate(MESSAGE), responsesNode.key());
            }
        }
    }

    private Set<String> parsePaths(String pathsStr) {
        if (!pathsStr.trim().isEmpty()) {
            return Arrays.stream(pathsStr.split(",")).map(String::trim).collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}