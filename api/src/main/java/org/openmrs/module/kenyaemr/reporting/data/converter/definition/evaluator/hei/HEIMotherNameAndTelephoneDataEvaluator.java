package org.openmrs.module.kenyaemr.reporting.data.converter.definition.evaluator.hei;

import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIMotherFacilityAndCCCNumberDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIMothersNameAndTelephoneDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * HEIMotherNameAndTelephoneDataEvaluator
 */
@Handler(supports= HEIMothersNameAndTelephoneDataDefinition.class, order=50)
public class HEIMotherNameAndTelephoneDataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select  e.patient_id,\n" +
                "  CONCAT(d.given_name,\" \",\n" +
                "         d.middle_name,\" \",\n" +
                "         d.family_name,'____________', d.phone_number) as mothersNameTelephone\n" +
                "from kenyaemr_etl.etl_patient_demographics d\n" +
                "  INNER JOIN kenyaemr_etl.etl_hei_enrollment e\n" +
                "    on e.patient_id = d.patient_id\n" +
                "GROUP BY e.patient_id;";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}