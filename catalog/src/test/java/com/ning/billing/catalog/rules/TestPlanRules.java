/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.catalog.rules;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.ning.billing.catalog.MockCatalog;
import com.ning.billing.catalog.PriceList;
import com.ning.billing.catalog.Product;
import com.ning.billing.catalog.api.ActionPolicy;
import com.ning.billing.catalog.api.BillingPeriod;
import com.ning.billing.catalog.api.IPriceListSet;
import com.ning.billing.catalog.api.IllegalPlanChange;
import com.ning.billing.catalog.api.PhaseType;
import com.ning.billing.catalog.api.PlanAlignmentChange;
import com.ning.billing.catalog.api.PlanChangeResult;
import com.ning.billing.catalog.api.PlanPhaseSpecifier;
import com.ning.billing.catalog.api.PlanSpecifier;

public class TestPlanRules {
	Logger log = LoggerFactory.getLogger(TestPlanRules.class);

	private MockCatalog cat = null;

	@BeforeTest
	public void setup() {
		cat = new MockCatalog();

		PriceList priceList2 = cat.getPriceLists().getChildPriceLists()[0];

		CaseChangePlanPolicy casePolicy = new CaseChangePlanPolicy().setPolicy(ActionPolicy.END_OF_TERM);
		CaseChangePlanAlignment caseAlignment = new CaseChangePlanAlignment().setAlignment(PlanAlignmentChange.START_OF_SUBSCRIPTION);
		CasePriceList casePriceList = new CasePriceList().setToPriceList(priceList2);

		cat.getPlanRules().
		setChangeCase(new CaseChangePlanPolicy[]{casePolicy}).
		setChangeAlignmentCase(new CaseChangePlanAlignment[]{caseAlignment}).
		setPriceListCase(new CasePriceList[]{casePriceList});
	}

	@Test
	public void testCannotChangeToSamePlan() {
		Product product1 = cat.getProducts()[0];
		PriceList priceList1 = cat.getPriceListFromName(IPriceListSet.DEFAULT_PRICELIST_NAME);
		
		PlanPhaseSpecifier from = new PlanPhaseSpecifier(product1.getName(), product1.getCategory(), BillingPeriod.MONTHLY, priceList1.getName(), PhaseType.EVERGREEN);
		PlanSpecifier to = new PlanSpecifier(product1.getName(), product1.getCategory(), BillingPeriod.MONTHLY, priceList1.getName());

		try {
			cat.getPlanRules().planChange(from, to, cat);
			Assert.fail("We did not see an exception when  trying to change plan to the same plan");
		} catch (IllegalPlanChange e) {
			log.info("Correct - cannot change to the same plan:", e);
		}

	}
	
	@Test
	public void testExistingPriceListIsKept() {
		Product product1 = cat.getProducts()[0];
		PriceList priceList1 = cat.getPriceListFromName(IPriceListSet.DEFAULT_PRICELIST_NAME);
		
		PlanPhaseSpecifier from = new PlanPhaseSpecifier(product1.getName(), product1.getCategory(), BillingPeriod.MONTHLY, priceList1.getName(), PhaseType.EVERGREEN);
		PlanSpecifier to = new PlanSpecifier(product1.getName(), product1.getCategory(), BillingPeriod.ANNUAL, priceList1.getName());

		PlanChangeResult result = null;
		try {
			result = cat.getPlanRules().planChange(from, to, cat);		
		} catch (IllegalPlanChange e) {
			log.info("Correct - cannot change to the same plan:", e);
			Assert.fail("We should not have triggered this error");
		}
		
		Assert.assertEquals(result.getPolicy(), ActionPolicy.END_OF_TERM);
		Assert.assertEquals(result.getAlignment(), PlanAlignmentChange.START_OF_SUBSCRIPTION);
		Assert.assertEquals(result.getNewPriceList(), priceList1);

	}
	
	
	@Test
	public void testBaseCase() {
		Product product1 = cat.getProducts()[0];
		Product product2 = cat.getProducts()[1];
		PriceList priceList1 = cat.getPriceListFromName(IPriceListSet.DEFAULT_PRICELIST_NAME);
		PriceList priceList2 = cat.getPriceLists().getChildPriceLists()[0];
		
		PlanPhaseSpecifier from = new PlanPhaseSpecifier(product1.getName(), product1.getCategory(), BillingPeriod.MONTHLY, priceList1.getName(), PhaseType.EVERGREEN);
		PlanSpecifier to = new PlanSpecifier(product2.getName(), product2.getCategory(), BillingPeriod.MONTHLY, null);

		PlanChangeResult result = null;
		try {
			result = cat.getPlanRules().planChange(from, to, cat);		
		} catch (IllegalPlanChange e) {
			log.info("Correct - cannot change to the same plan:", e);
			Assert.fail("We should not have triggered this error");
		}
		
		Assert.assertEquals(result.getPolicy(), ActionPolicy.END_OF_TERM);
		Assert.assertEquals(result.getAlignment(), PlanAlignmentChange.START_OF_SUBSCRIPTION);
		Assert.assertEquals(result.getNewPriceList(), priceList2);

	}
	
	
	
}
