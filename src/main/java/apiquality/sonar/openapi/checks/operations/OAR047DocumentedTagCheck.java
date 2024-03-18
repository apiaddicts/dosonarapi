/*
 * SonarQube OpenAPI Plugin
 * Copyright (C) 2018-2019 Societe Generale
 * vincent.girard-reydet AT socgen DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.impl.MissingNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Rule(key = OAR047DocumentedTagCheck.CHECK_KEY)
public class OAR047DocumentedTagCheck extends BaseCheck {
  public static final String CHECK_KEY = "OAR047";
  private final Map<String, JsonNode> tagNames = new HashMap<>();

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(OpenApi2Grammar.TAG, OpenApi2Grammar.OPERATION, OpenApi3Grammar.TAG, OpenApi3Grammar.OPERATION);
  }

  @Override
  public void visitFile(JsonNode root) {
    tagNames.clear();
    JsonNode tagsArray = root.at("/tags").value();
    if (tagsArray != null) {
      for (JsonNode element : tagsArray.elements()) {
        JsonNode previous = tagNames.put(element.at("/name").value().getTokenValue(), element);
        if (previous != null) {
          PreciseIssue issue = addIssue(CHECK_KEY, translate("OAR047.error-duplicated"), element);
          issue.secondary(previous, null);
        }
      }
    }
  }


  @Override
  protected void visitNode(JsonNode node) {
    AstNodeType nodeType = node.getType();
    if (nodeType == OpenApi2Grammar.TAG || nodeType == OpenApi3Grammar.TAG) {
      visitTag(node);
    } else {
      visitOperation(node);
    }
  }

  private void visitTag(JsonNode node) {
    JsonNode descriptionNode = node.at("/description").value();
    if (descriptionNode == MissingNode.MISSING) {
      addIssue(CHECK_KEY, translate("OAR047.error-no-description"), node);
    }
  }

  private void visitOperation(JsonNode node) {
    JsonNode tagsArray = node.at("/tags").value();
    if (tagsArray != MissingNode.MISSING) {
      for (JsonNode element : tagsArray.elements()) {
        if (!tagNames.containsKey(element.getTokenValue())) {
          addIssue(CHECK_KEY, translate("OAR047.error-no-declared"), element);
        }
      }
    }
  }

}
