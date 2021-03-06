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

package com.ning.billing.catalog.api;

import java.util.Date;
import java.util.List;

public interface ICatalog {

	public abstract IProduct[] getProducts();
	
	public abstract IPlan getPlan(String productName, BillingPeriod term, String priceList);

	public abstract Currency[] getSupportedCurrencies();

	public abstract IPlan[] getPlans();

	public abstract ActionPolicy getPlanChangePolicy(PlanPhaseSpecifier from,
			PlanSpecifier to);

	public abstract PlanChangeResult planChange(PlanPhaseSpecifier from,
			PlanSpecifier to) throws IllegalPlanChange;
	
    public abstract IPlan getPlanFromName(String name);

    public abstract IPlanPhase getPhaseFromName(String name);

    public abstract Date getEffectiveDate();

    public abstract IPlanPhase getPhaseFor(String name, Date date);

    public abstract IProduct getProductFromName(String name);

    public abstract ActionPolicy getPlanCancelPolicy(PlanPhaseSpecifier planPhase);

    public abstract void configureEffectiveDate(Date date);

    public abstract String getCalalogName();

    public abstract PlanAlignmentCreate getPlanCreateAlignment(PlanSpecifier specifier);

    public abstract BillingAlignment getBillingAlignment(PlanPhaseSpecifier planPhase);

    public abstract PlanAlignmentChange getPlanChangeAlignment(PlanPhaseSpecifier from,
			PlanSpecifier to);

	
}