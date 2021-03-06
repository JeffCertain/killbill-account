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

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collections;

public class TestBusinessAccount
{
    private BusinessAccount account;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception
    {
        account = new BusinessAccount("pierre", BigDecimal.ONE, Collections.singletonList("batch15"), new DateTime(), BigDecimal.TEN, "ERROR_NOT_ENOUGH_FUNDS", "CreditCard", "Visa", "");
    }

    @Test(groups = "fast")
    public void testEquals() throws Exception
    {
        Assert.assertSame(account, account);
        Assert.assertEquals(account, account);
        Assert.assertTrue(account.equals(account));

        final BusinessAccount otherAccount = new BusinessAccount("pierre", BigDecimal.ONE, Collections.singletonList("batch15"), new DateTime(), BigDecimal.TEN, "ERROR_NOT_ENOUGH_FUNDS", "CreditCard", "Visa", "");
        Assert.assertFalse(account.equals(otherAccount));
    }
}
