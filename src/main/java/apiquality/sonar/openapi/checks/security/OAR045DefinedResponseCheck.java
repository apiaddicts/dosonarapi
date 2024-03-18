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
package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

@Rule(key = OAR045DefinedResponseCheck.CHECK_KEY)
public class OAR045DefinedResponseCheck extends BaseCheck {
  protected static final String CHECK_KEY = "OAR045";
  private static final String MESSAGE_NO_RESPONSE = "OAR045.error-no-responses";
  private static final String MESSAGE_NO_MODEL = "OAR045.error-no-model";

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return Sets.newHashSet(
      OpenApi2Grammar.RESPONSES);
  }

  @Override
  protected void visitNode(JsonNode node) {
    Map<String, JsonNode> properties = node.propertyMap();
    if (properties.isEmpty()) {
      addIssue(CHECK_KEY, translate(MESSAGE_NO_RESPONSE), node.key());
    }
    visitV2Responses(properties);
  }

  private void visitV2Responses(Map<String, JsonNode> responses) {
    for (Map.Entry<String, JsonNode> entry : responses.entrySet()) {
      visitResponseV2OrMediaType(entry.getKey(), entry.getValue());
    }
  }

  private boolean visitResponseV2OrMediaType(String code, JsonNode node) {
    JsonNode actual = node.resolve();
    Map<String, JsonNode> properties = actual.propertyMap();
    if (!code.equals("204") && !properties.containsKey("schema")) {
      addIssue(CHECK_KEY, translate(MESSAGE_NO_MODEL), node.key());
      return false;
    }
    return true;
  }
}
