/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.services.verifier.core.checks;

import org.drools.workbench.services.verifier.api.client.configuration.AnalyzerConfiguration;
import org.drools.workbench.services.verifier.api.client.index.ObjectField;
import org.drools.workbench.services.verifier.api.client.maps.InspectorMultiMap;
import org.drools.workbench.services.verifier.api.client.maps.util.RedundancyResult;
import org.drools.workbench.services.verifier.api.client.reporting.CheckType;
import org.drools.workbench.services.verifier.core.cache.inspectors.PatternInspector;
import org.drools.workbench.services.verifier.core.cache.inspectors.RuleInspector;
import org.drools.workbench.services.verifier.core.cache.inspectors.action.ActionInspector;
import org.drools.workbench.services.verifier.core.checks.base.SingleCheck;

public abstract class DetectRedundantActionBase
        extends SingleCheck {

    protected PatternInspector patternInspector;

    protected RedundancyResult<ObjectField, ActionInspector> result;

    DetectRedundantActionBase( final RuleInspector ruleInspector,
                               final AnalyzerConfiguration configuration,
                               final CheckType checkType ) {
        super( ruleInspector,
               configuration,
               checkType );
    }

    @Override
    public boolean check() {
        result = ruleInspector.getPatternsInspector().stream()
                              .map( PatternInspector::getActionsInspector )
                              .map( InspectorMultiMap::hasRedundancy )
                              .filter( RedundancyResult::isTrue )
                              .findFirst().orElse( null );

        return hasIssues = result != null;
    }

}
