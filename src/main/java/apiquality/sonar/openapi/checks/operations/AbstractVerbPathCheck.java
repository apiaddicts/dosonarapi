package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;

import apiquality.sonar.openapi.checks.schemas.AbstractSchemaCheck;
import apiquality.sonar.openapi.utils.VerbPathMatcher;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.isOperation;

public abstract class AbstractVerbPathCheck extends AbstractSchemaCheck {

    protected VerbPathMatcher matcher;

    public AbstractVerbPathCheck(String key) {
        super(key);
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        String path = node.key().getTokenValue();
        Collection<JsonNode> operationNodes = node.properties().stream().filter(propertyNode -> isOperation(propertyNode)).collect(Collectors.toList());
        for (JsonNode operationNode : operationNodes) {
            String verb = operationNode.key().getTokenValue();
            Optional<VerbPathMatcher.PatternGroup> pg = matcher.matchesWithValues(verb, path);
            if (pg.isPresent()) matchV2(node, operationNode, verb, path, pg.get());
            else mismatchV2(node, operationNode, verb, path);
        }
    }

    protected void matchV2(JsonNode node, JsonNode operationNode, String verb, String path, VerbPathMatcher.PatternGroup pg) {
        // Intentional blank
    }

    protected void mismatchV2(JsonNode node, JsonNode operationNode, String verb, String path) {
        // Intentional blank
    }
}
