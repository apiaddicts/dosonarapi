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
package org.sonar.samples.openapi.checks.operations;

import com.sonar.sslr.api.RecognitionException;

import apiquality.sonar.openapi.checks.operations.OAR043ParsingErrorCheck;

import org.junit.Test;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiFile;
import org.apiaddicts.apitools.dosonarapi.api.OpenApiVisitorContext;
import org.apiaddicts.apitools.dosonarapi.api.PreciseIssue;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class OAR043ParsingErrorCheckTest {

  @Test
  public void reports_parsing_errors() {
    OpenApiVisitorContext context = new OpenApiVisitorContext(new TestFile(), new RecognitionException(3, "Parsing exception message"));
    OAR043ParsingErrorCheck check = new OAR043ParsingErrorCheck();

    List<PreciseIssue> issues = check.scanFileForIssues(context);

    assertThat(issues)
        .extracting(i -> i.primaryLocation().startLine(), i -> i.primaryLocation().message())
        .contains(tuple(0, "OAR043: Error parsing line 0 column null"));
  }

  private static class TestFile implements OpenApiFile {

    @Override
    public String content() {
      return null;
    }

    @Override
    public String fileName() {
      return null;
    }
  }
}

