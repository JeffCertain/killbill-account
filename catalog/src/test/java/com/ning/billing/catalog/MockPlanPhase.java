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

package com.ning.billing.catalog;

import com.ning.billing.catalog.api.BillingPeriod;
import com.ning.billing.catalog.api.PhaseType;
import com.ning.billing.catalog.api.TimeUnit;

public class MockPlanPhase extends PlanPhase {

    public MockPlanPhase(
    		BillingPeriod billingPeriod, 
    		PhaseType type, 
    		Duration duration, 
    		InternationalPrice recurringPrice, 
    		InternationalPrice fixedPrice) {
		setBillingPeriod(billingPeriod);
		setPhaseType(type);
		setDuration(duration);
		setReccuringPrice(recurringPrice);
		setFixedPrice(fixedPrice);
	}
    
    public MockPlanPhase() {
		setBillingPeriod(BillingPeriod.MONTHLY);
		setPhaseType(PhaseType.EVERGREEN);
		setDuration(new Duration().setNumber(-1).setUnit(TimeUnit.UNLIMITED));
		setReccuringPrice(new MockInternationalPrice());
		setFixedPrice(null);
		setPlan(new MockPlan(this));
	}

	public MockPlanPhase(MockPlan mockPlan) {
		setBillingPeriod(BillingPeriod.MONTHLY);
		setPhaseType(PhaseType.EVERGREEN);
		setDuration(new Duration().setNumber(-1).setUnit(TimeUnit.UNLIMITED));
		setReccuringPrice(new MockInternationalPrice());
		setFixedPrice(null);
		setPlan(mockPlan);
	}

	
}
