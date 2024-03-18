package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

public abstract class AbstractFormatCheck extends BaseCheck {

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.SCHEMA, OpenApi2Grammar.PARAMETER, OpenApi3Grammar.SCHEMA);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        JsonNode typeNode = node.get("type");
        String type = typeNode.getTokenValue();
        JsonNode formatNode = node.get("format");
        String format = formatNode.isMissing() ? null : formatNode.getTokenValue();
        validate(type, format, typeNode);
    }

    public abstract void validate(String type, String format, JsonNode typeNode);
}