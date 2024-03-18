package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static apiquality.sonar.openapi.utils.JsonNodeUtils.*;

public abstract class AbstractUndefinedMediaTypeCheck extends BaseCheck {

	private static final String MESSAGE = "generic.section";

	private String key;
	private String section;

	private boolean globalDefinesMediaTypes = false;

	public AbstractUndefinedMediaTypeCheck(String key, String section) {
		this.key = key;
		this.section = section;
	}

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION, OpenApi3Grammar.RESPONSES);
	}

	@Override
	protected void visitFile(JsonNode root) {
		globalDefinesMediaTypes = (root.getType() instanceof OpenApi2Grammar) ? definesMimeTypesV2(root) : false;
	}

	@Override
	public void visitNode(JsonNode node) {
		if (node.getType() instanceof OpenApi2Grammar) {
			visitV2Node(node);
		} else {
			visitV3Node(node);
		}
	}

	private void visitV2Node(JsonNode node) {
		if (!globalDefinesMediaTypes && !definesMimeTypesV2(node)) {
			addIssue(key, translate(MESSAGE, section), node.key());
		}
	}

	private void visitV3Node(JsonNode node) {
		if (node.getType() == OpenApi3Grammar.OPERATION && section.equals("consumes")) {
			String operation = node.key().getTokenValue().toLowerCase();
			if (operation.equals("post") || operation.equals("put") || operation.equals("patch")) {
				visitContentNode(node);
			}
		}

		if (node.getType() == OpenApi3Grammar.RESPONSES && section.equals("produces")) {
            List<JsonNode> responseCodes = node.properties().stream().collect(Collectors.toList());
            for (JsonNode jsonNode : responseCodes) {
                if (!jsonNode.key().getTokenValue().equals("204")) {
                    JsonNode responseNode = jsonNode.resolve();
                    visitContentNode(responseNode);
                }
            }
		}
	}

	private void visitContentNode(JsonNode node) {
		JsonNode contentNode;
		if (section.equals("consumes")) {
			JsonNode requestBodyNode = node.at("/requestBody");
			if (requestBodyNode.isMissing() || requestBodyNode.isNull()) {
				addIssue(key, translate(MESSAGE, "requestBody"), node.key());
				return;
			}
			contentNode = resolve(requestBodyNode).at("/content");
		} else {
			contentNode = node.at("/content");
		}

		if (!globalDefinesMediaTypes && !definesMimeTypesV3(contentNode)) {
			addIssue(key, translate(MESSAGE, "content"), node.key());
		}
	}

	private boolean definesMimeTypesV2(JsonNode node) {
		JsonNode consumes = node.get(section);
		if (consumes.isMissing() || consumes.isNull()) return false;
		return !consumes.elements().isEmpty();
	}

	private boolean definesMimeTypesV3(JsonNode content) {
		Map<String, JsonNode> mimeTypesList = content.propertyMap();
		if (content.isMissing() || content.isNull()) return false;
		return !mimeTypesList.isEmpty();
	}
}
