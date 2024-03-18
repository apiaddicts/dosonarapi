package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = OAR032AmbiguousElementsPathCheck.KEY)
public class OAR032AmbiguousElementsPathCheck extends BaseCheck {

    public static final String KEY = "OAR032";
    private static final String PARAM_REGEX = "\\{[^}{]*}";
    private static final String MESSAGE = "OAR032.error";

    private static final String AMBIGUOUS_NAMES = "elementos,instancias,recursos,valores,terminos,objetos,articulos," +
            "elements,instances,resources,values,terms,objects,items";

    @RuleProperty(
            key = "ambiguous-names",
            description = "List of forbidden names. Comma separated",
            defaultValue = AMBIGUOUS_NAMES
    )
    private String forbiddenValuesStr = AMBIGUOUS_NAMES;

    private Set<String> forbiddenValues = new HashSet<>();

    @Override
    protected void visitFile(JsonNode root) {
        forbiddenValues.addAll(Stream.of(forbiddenValuesStr.split(",")).map(String::trim).collect(Collectors.toSet()));
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitPathNode(node);
    }

    private void visitPathNode(JsonNode node) {
        String path = node.key().getTokenValue();
        String pathWithoutParams = path.replaceAll(PARAM_REGEX, "");
        Set<String> pathParts = Stream.of(pathWithoutParams.split("/")).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        String forbidden = pathParts.stream().filter(forbiddenValues::contains).collect(Collectors.joining(", "));
        if (!forbidden.isEmpty()) {
            addIssue(KEY, translate(MESSAGE, forbidden), node.key());
        }
    }
}