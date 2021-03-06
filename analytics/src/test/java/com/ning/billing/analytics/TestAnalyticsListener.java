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

package com.ning.billing.analytics;

import com.ning.billing.catalog.api.Currency;
import com.ning.billing.catalog.api.IPlan;
import com.ning.billing.catalog.api.IPlanPhase;
import com.ning.billing.catalog.api.IProduct;
import com.ning.billing.catalog.api.PhaseType;
import com.ning.billing.catalog.api.ProductCategory;
import com.ning.billing.entitlement.api.user.ISubscription;
import com.ning.billing.entitlement.api.user.SubscriptionTransition;
import com.ning.billing.entitlement.events.IEvent;
import com.ning.billing.entitlement.events.user.ApiEventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class TestAnalyticsListener
{
    private static final String KEY = "1234";
    private static final String ACCOUNT_KEY = "pierre-1234";
    private final Currency CURRENCY = Currency.BRL;

    private final MockBusinessSubscriptionTransitionDao dao = new MockBusinessSubscriptionTransitionDao();
    private final UUID subscriptionId = UUID.randomUUID();
    private final UUID bundleUUID = UUID.randomUUID();
    private final IProduct product = new MockProduct("platinium", "subscription", ProductCategory.BASE);
    private final IPlan plan = new MockPlan("platinum-monthly", product);
    private final IPlanPhase phase = new MockPhase(PhaseType.EVERGREEN, plan, MockDuration.UNLIMITED(), 25.95);
    private final String priceList = null;

    private AnalyticsListener listener;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception
    {
        final BusinessSubscriptionTransitionRecorder recorder = new BusinessSubscriptionTransitionRecorder(dao, new MockIEntitlementUserApi(bundleUUID, KEY), new MockIAccountUserApi(ACCOUNT_KEY, CURRENCY));
        listener = new AnalyticsListener(recorder, null);
    }

    @Test(groups = "fast")
    public void testSubscriptionLifecycle() throws Exception
    {
        // Create a subscription
        final DateTime effectiveTransitionTime = new DateTime(DateTimeZone.UTC);
        final DateTime requestedTransitionTime = new DateTime(DateTimeZone.UTC);
        final SubscriptionTransition firstTransition = createFirstSubscriptionTransition(requestedTransitionTime, effectiveTransitionTime);
        final BusinessSubscriptionTransition firstBST = createExpectedFirstBST(requestedTransitionTime, effectiveTransitionTime);
        listener.handleSubscriptionTransitionChange(firstTransition);
        Assert.assertEquals(dao.getTransitions(KEY).size(), 1);
        Assert.assertEquals(dao.getTransitions(KEY).get(0), firstBST);

        // Pause it
        final DateTime effectivePauseTransitionTime = new DateTime(DateTimeZone.UTC);
        final DateTime requestedPauseTransitionTime = new DateTime(DateTimeZone.UTC);
        final SubscriptionTransition pausedSubscriptionTransition = createPauseSubscriptionTransition(effectivePauseTransitionTime, requestedPauseTransitionTime, firstTransition.getNextState());
        final BusinessSubscriptionTransition pausedBST = createExpectedPausedBST(requestedPauseTransitionTime, effectivePauseTransitionTime, firstBST.getNextSubscription());
        listener.handleSubscriptionTransitionChange(pausedSubscriptionTransition);
        Assert.assertEquals(dao.getTransitions(KEY).size(), 2);
        Assert.assertEquals(dao.getTransitions(KEY).get(1), pausedBST);

        // Un-Pause it
        final DateTime effectiveResumeTransitionTime = new DateTime(DateTimeZone.UTC);
        final DateTime requestedResumeTransitionTime = new DateTime(DateTimeZone.UTC);
        final SubscriptionTransition resumedSubscriptionTransition = createResumeSubscriptionTransition(requestedResumeTransitionTime, effectiveResumeTransitionTime, pausedSubscriptionTransition.getNextState());
        final BusinessSubscriptionTransition resumedBST = createExpectedResumedBST(requestedResumeTransitionTime, effectiveResumeTransitionTime, pausedBST.getNextSubscription());
        listener.handleSubscriptionTransitionChange(resumedSubscriptionTransition);
        Assert.assertEquals(dao.getTransitions(KEY).size(), 3);
        Assert.assertEquals(dao.getTransitions(KEY).get(2), resumedBST);

        // Cancel it
        final DateTime effectiveCancelTransitionTime = new DateTime(DateTimeZone.UTC);
        final DateTime requestedCancelTransitionTime = new DateTime(DateTimeZone.UTC);
        listener.handleSubscriptionTransitionChange(createCancelSubscriptionTransition(requestedCancelTransitionTime, effectiveCancelTransitionTime, resumedSubscriptionTransition.getNextState()));
        final BusinessSubscriptionTransition cancelledBST = createExpectedCancelledBST(requestedCancelTransitionTime, effectiveCancelTransitionTime, resumedBST.getNextSubscription());
        Assert.assertEquals(dao.getTransitions(KEY).size(), 4);
        Assert.assertEquals(dao.getTransitions(KEY).get(3), cancelledBST);
    }

    private BusinessSubscriptionTransition createExpectedFirstBST(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime)
    {
        final BusinessSubscriptionEvent event = BusinessSubscriptionEvent.subscriptionCreated(plan);
        final ISubscription.SubscriptionState subscriptionState = ISubscription.SubscriptionState.ACTIVE;
        return createExpectedBST(event, requestedTransitionTime, effectiveTransitionTime, null, subscriptionState);
    }

    private BusinessSubscriptionTransition createExpectedPausedBST(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final BusinessSubscription lastSubscription)
    {
        final BusinessSubscriptionEvent event = BusinessSubscriptionEvent.subscriptionPaused(plan);
        final ISubscription.SubscriptionState subscriptionState = ISubscription.SubscriptionState.PAUSED;
        return createExpectedBST(event, requestedTransitionTime, effectiveTransitionTime, lastSubscription, subscriptionState);
    }

    private BusinessSubscriptionTransition createExpectedResumedBST(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final BusinessSubscription lastSubscription)
    {
        final BusinessSubscriptionEvent event = BusinessSubscriptionEvent.subscriptionResumed(plan);
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.ACTIVE;
        return createExpectedBST(event, requestedTransitionTime, effectiveTransitionTime, lastSubscription, nextState);
    }

    private BusinessSubscriptionTransition createExpectedCancelledBST(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final BusinessSubscription lastSubscription)
    {
        final BusinessSubscriptionEvent event = BusinessSubscriptionEvent.subscriptionCancelled(plan);
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.CANCELLED;
        return createExpectedBST(event, requestedTransitionTime, effectiveTransitionTime, lastSubscription, nextState);
    }

    private BusinessSubscriptionTransition createExpectedBST(
        final BusinessSubscriptionEvent eventType,
        final DateTime requestedTransitionTime,
        final DateTime effectiveTransitionTime,
        final BusinessSubscription previousSubscription,
        final ISubscription.SubscriptionState nextState
    )
    {
        return new BusinessSubscriptionTransition(
            KEY,
            ACCOUNT_KEY,
            requestedTransitionTime,
            eventType,
            previousSubscription,
            new BusinessSubscription(
                null,
                plan,
                phase,
                CURRENCY,
                effectiveTransitionTime,
                nextState,
                subscriptionId,
                bundleUUID
            )
        );
    }

    private SubscriptionTransition createFirstSubscriptionTransition(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime)
    {
        final ApiEventType eventType = ApiEventType.CREATE;
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.ACTIVE;
        return new SubscriptionTransition(
            subscriptionId,
            bundleUUID,
            IEvent.EventType.API_USER,
            eventType,
            requestedTransitionTime,
            effectiveTransitionTime,
            null,
            null,
            null,
            null,
            nextState,
            plan,
            phase,
            priceList
        );
    }

    private SubscriptionTransition createPauseSubscriptionTransition(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final ISubscription.SubscriptionState previousState)
    {
        final ApiEventType eventType = ApiEventType.PAUSE;
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.PAUSED;
        return createSubscriptionTransition(eventType, requestedTransitionTime, effectiveTransitionTime, previousState, nextState);
    }

    private SubscriptionTransition createResumeSubscriptionTransition(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final ISubscription.SubscriptionState previousState)
    {
        final ApiEventType eventType = ApiEventType.RESUME;
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.ACTIVE;
        return createSubscriptionTransition(eventType, requestedTransitionTime, effectiveTransitionTime, previousState, nextState);
    }

    private SubscriptionTransition createCancelSubscriptionTransition(final DateTime requestedTransitionTime, final DateTime effectiveTransitionTime, final ISubscription.SubscriptionState previousState)
    {
        final ApiEventType eventType = ApiEventType.CANCEL;
        final ISubscription.SubscriptionState nextState = ISubscription.SubscriptionState.CANCELLED;
        return createSubscriptionTransition(eventType, requestedTransitionTime, effectiveTransitionTime, previousState, nextState);
    }

    private SubscriptionTransition createSubscriptionTransition(
        final ApiEventType eventType,
        final DateTime requestedTransitionTime,
        final DateTime effectiveTransitionTime,
        final ISubscription.SubscriptionState previousState,
        final ISubscription.SubscriptionState nextState
    )
    {
        return new SubscriptionTransition(
            subscriptionId,
            bundleUUID,
            IEvent.EventType.API_USER,
            eventType,
            requestedTransitionTime,
            effectiveTransitionTime,
            previousState,
            plan,
            phase,
            priceList,
            nextState,
            plan,
            phase,
            priceList
        );
    }
}