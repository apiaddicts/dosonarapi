package apiquality.sonar.openapi.checks.schemas;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;

import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

public abstract class AbstractExplicitResponseCheck extends AbstractSchemaCheck {

    protected final String responseCode;

    public AbstractExplicitResponseCheck(String key, String responseCode) {
        super(key);
        this.responseCode = responseCode;
    }
    
    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.RESPONSES, OpenApi3Grammar.RESPONSES);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitResponsesNode(node);
    }

    protected void visitResponsesNode(JsonNode node) {
        List<JsonNode> allResponses = node.properties().stream()
                .map(JsonNode::value)
                .collect(Collectors.toList());

        for (JsonNode responseNode : allResponses) {
            if (!responseCode.equals(responseNode.key().getTokenValue())) continue;
            responseNode = resolve(responseNode);
            if (responseNode.getType().equals(OpenApi2Grammar.RESPONSE)) {
                visitV2ExplicitNode(responseNode);
            } else if (responseNode.getType().equals(OpenApi3Grammar.RESPONSE)) {
                JsonNode content = responseNode.at("/content");
                if (content.isMissing()) {
                    if (key.equals("OAR038")) {
                        visitV2ExplicitNode(responseNode);
                    }
                    continue;
                }
                for (JsonNode mediaTypeNode : content.propertyMap().values()) {
                    visitV2ExplicitNode(mediaTypeNode);
                }
            }
        }
    }

    protected abstract void visitV2ExplicitNode(JsonNode node);
}
