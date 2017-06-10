package org.openmrs.module.referenceapplication.reporting.reports;

import org.openmrs.Cohort;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests the NumberOfPatientRegistrations report
 */
public class NumberOfPatientRegistrationsTest extends ReportManagerTest {

	@Autowired
	NumberOfPatientRegistrations numberOfPatientRegistrations;

	@Override
	public ReportManager getReportManager() {
		return numberOfPatientRegistrations;
	}

	@Override
	public EvaluationContext getEvaluationContext() {
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.getDateTime(2017,6,1));
		context.addParameterValue("endDate", DateUtil.getDateTime(2017,6,16));
		context.setBaseCohort(new Cohort("50, 51"));
		return context;
	}
}
