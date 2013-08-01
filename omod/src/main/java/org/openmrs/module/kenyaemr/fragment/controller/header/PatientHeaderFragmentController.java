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

package org.openmrs.module.kenyaemr.fragment.controller.header;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.AppDescriptor;
import org.openmrs.module.kenyacore.CoreContext;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.ui.framework.WebConstants;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

/**
 * Banner showing which patient this page is in the context of
 */
public class PatientHeaderFragmentController {

	public void controller(PageModel sharedPageModel,
						   FragmentConfiguration config,
						   FragmentModel model,
						   PageRequest pageRequest,
						   @SpringBean KenyaUiUtils kenyaUi,
						   @SpringBean CoreContext emr) {

		Patient patient = (Patient) config.get("patient");
		if (patient == null) {
			patient = (Patient) sharedPageModel.getAttribute("patient");
		}

		model.addAttribute("patient", patient);

		List<Visit> activeVisits = Context.getVisitService().getActiveVisitsByPatient(patient);
		if (activeVisits.size() > 0) {
			Visit activeVisit = activeVisits.get(0);
			model.addAttribute("activeVisit", activeVisit);
			model.addAttribute("activeVisitStartedToday", EmrUtils.isToday(activeVisit.getStartDatetime()));
		}
		else {
			model.addAttribute("activeVisit", null);
		}
		
		AppDescriptor currentApp = kenyaUi.getCurrentApp(pageRequest);

		if (currentApp != null) {
			model.addAttribute("appHomepageUrl", "/" + WebConstants.CONTEXT_PATH + "/" + currentApp.getHomepageUrl());
		} else {
			model.addAttribute("appHomepageUrl", null);
		}

		model.addAttribute("idsToShow", emr.getIdentifierManager().getPatientDisplayIdentifiers(patient));
	}
}