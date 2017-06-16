/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.referenceapplication.definition.constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MetadataConstants {

	private Log log = LogFactory.getLog(this.getClass());

    // Identifier Types
    public static final String OPENMRS_ID = "05a29f94-c0ed-11e2-94be-8c13b969e334";

    // Encounter Types
    public static final String HIV_INITIAL_ENCOUNTER = "8d5b27bc-c2cc-11de-8d13-0010c6dffd0f";
    public static final String HIV_FOLLOWUP_ENCOUNTER = "8d5b2be0-c2cc-11de-8d13-0010c6dffd0f";

    // Forms
    public static final String HIV_ENROLLMENT_FORM = "14ce95aa-142c-41fc-b92f-d75964314e5f";
    public static final String HIV_FOLLOWUP_FORM = "fe6ddde7-9c81-4570-8795-8252ae1197f3";

    // Concepts
    public static final String TRUE = "be4abdb9-1691-11df-97a5-7038c432aabf";
    public static final String FALSE = "be4abefe-1691-11df-97a5-7038c432aabf";
    public static final String WEIGHT = "be4f6a72-1691-11df-97a5-7038c432aabf";
    public static final String HEIGHT = "be4f6bfb-1691-11df-97a5-7038c432aabf";
    public static final String CD4_COUNT = "be53d1d4-1691-11df-97a5-7038c432aabf";
    public static final String WHO_STAGE = "be52377e-1691-11df-97a5-7038c432aabf";
    public static final String WHO_STAGE_1 = "be4cc33e-1691-11df-97a5-7038c432aabf";
    public static final String WHO_STAGE_2 = "be4cc487-1691-11df-97a5-7038c432aabf";
    public static final String WHO_STAGE_3 = "be4cc5d5-1691-11df-97a5-7038c432aabf";
    public static final String WHO_STAGE_4 = "be4cc71e-1691-11df-97a5-7038c432aabf";
    public static final String ON_ARVS = "be4cb396-1691-11df-97a5-7038c432aabf";
    public static final String RETURN_VISIT_DATE = "be4f73c9-1691-11df-97a5-7038c432aabf";

    // Programs
    public static final String HIV_PROGRAM = "ee12645c-aafd-11df-a781-001e378eb67e";
    public static final String TREATMENT_STATUS = "f0a8a602-aaff-11df-a781-001e378eb67e";
    public static final String FOLLOWING = "151652ae-15f5-102d-96e4-000c29c2a5d7";
    public static final String DIED = "15165d8a-15f5-102d-96e4-000c29c2a5d7";
    public static final String DEFAULTED = "15165f24-15f5-102d-96e4-000c29c2a5d7";
    public static final String TRANSFERRED = "15165e5c-15f5-102d-96e4-000c29c2a5d7";
    public static final String STOPPED = "1516588a-15f5-102d-96e4-000c29c2a5d7";
}
