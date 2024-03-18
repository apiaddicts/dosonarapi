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

import com.sonar.sslr.api.RecognitionException;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitorContext;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationException;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.ValidationIssue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = OAR043ParsingErrorCheck.CHECK_KEY)
public class OAR043ParsingErrorCheck extends BaseCheck {

  public static final String CHECK_KEY = "OAR043";

  private final Pattern PATTERN = Pattern.compile("\\d+");

  @Override
  public void scanFile(OpenApiVisitorContext context) {
    super.scanFile(context);
    RecognitionException parsingException = context.parsingException();
    if (parsingException instanceof ValidationException) {
      for (ValidationException issue : ((ValidationException) parsingException).getCauses()) {
        addIssue(CHECK_KEY, issue.formatMessage(), issue.getNode());
      }
    } else if (parsingException != null) {
      int diff = 1;
      String msg = parsingException.getMessage();
      if (parsingException.getCause() != null) {
        msg = parsingException.getCause().getMessage();
        diff = 2;
        if (parsingException.getCause().getCause() != null) {
          msg = parsingException.getCause().getCause().getMessage();
          diff = 3;
        }
      }
      Matcher m = PATTERN.matcher(msg);
      String line = null;
      String column = null;
      if (m.find()) line = m.group();
      if (m.find()) column = m.group();
      int l = line == null || line.trim().isEmpty() ? 0 : Integer.parseInt(line);
      l -= diff;
      if (l < 0) l = 0;
      addLineIssue(CHECK_KEY, translate("OAR043.error-parser", l, column), l);
    } else {
      List<ValidationIssue> issues = context.getIssues();
      for (ValidationIssue issue : issues) {
        addIssue(CHECK_KEY, issue.formatMessage(), issue.getNode());
      }
    }
  }
}
