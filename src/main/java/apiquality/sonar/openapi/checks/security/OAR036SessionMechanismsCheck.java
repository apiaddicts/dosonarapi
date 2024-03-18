package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR036SessionMechanismsCheck.KEY)
public class OAR036SessionMechanismsCheck extends BaseCheck {

    public static final String KEY = "OAR036";
    private static final String MESSAGE = "OAR036.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PARAMETER, OpenApi2Grammar.HEADER, OpenApi3Grammar.PARAMETER, OpenApi3Grammar.HEADER);
    }

    @Override
    public void visitNode(JsonNode node) {
        if ( ( OpenApi2Grammar.PARAMETER.equals(node.getType()) || OpenApi3Grammar.PARAMETER.equals(node.getType()) )
        && "header".equals(node.get("in").getTokenValue()) && "Cookie".equals(node.get("name").getTokenValue())) {
            addIssue(KEY, translate(MESSAGE), node.get("name").value());
        }
        if ( ( OpenApi2Grammar.HEADER.equals(node.getType()) || OpenApi3Grammar.HEADER.equals(node.getType()) )
        && "Set-Cookie".equals(node.key().getTokenValue())) {
            addIssue(KEY, translate(MESSAGE), node.key());
        }
    }
}
