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

import com.ning.billing.analytics.utils.Rounder;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class BusinessAccount
{
    // Populated by the database
    private DateTime createdDt = null;
    private DateTime updatedDt = null;

    private final String key;
    private BigDecimal balance;
    private List<String> tags;
    private DateTime lastInvoiceDate;
    private BigDecimal totalInvoiceBalance;
    private String lastPaymentStatus;
    private String paymentMethod;
    private String creditCardType;
    private String billingAddressCountry;

    public BusinessAccount(final String key, final BigDecimal balance, final List<String> tags, final DateTime lastInvoiceDate, final BigDecimal totalInvoiceBalance, final String lastPaymentStatus, final String paymentMethod, final String creditCardType, final String billingAddressCountry)
    {
        this.key = key;
        this.balance = balance;
        this.billingAddressCountry = billingAddressCountry;
        this.creditCardType = creditCardType;
        this.lastInvoiceDate = lastInvoiceDate;
        this.lastPaymentStatus = lastPaymentStatus;
        this.paymentMethod = paymentMethod;
        this.tags = tags;
        this.totalInvoiceBalance = totalInvoiceBalance;
    }

    public String getKey()
    {
        return key;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public Double getRoundedBalance()
    {
        return Rounder.round(balance);
    }

    public void setBalance(final BigDecimal balance)
    {
        this.balance = balance;
    }

    public String getBillingAddressCountry()
    {
        return billingAddressCountry;
    }

    public void setBillingAddressCountry(final String billingAddressCountry)
    {
        this.billingAddressCountry = billingAddressCountry;
    }

    public DateTime getCreatedDt()
    {
        return createdDt;
    }

    public void setCreatedDt(final DateTime createdDt)
    {
        this.createdDt = createdDt;
    }

    public String getCreditCardType()
    {
        return creditCardType;
    }

    public void setCreditCardType(final String creditCardType)
    {
        this.creditCardType = creditCardType;
    }

    public DateTime getLastInvoiceDate()
    {
        return lastInvoiceDate;
    }

    public void setLastInvoiceDate(final DateTime lastInvoiceDate)
    {
        this.lastInvoiceDate = lastInvoiceDate;
    }

    public String getLastPaymentStatus()
    {
        return lastPaymentStatus;
    }

    public void setLastPaymentStatus(final String lastPaymentStatus)
    {
        this.lastPaymentStatus = lastPaymentStatus;
    }

    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(final String paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(final List<String> tags)
    {
        this.tags = tags;
    }

    public BigDecimal getTotalInvoiceBalance()
    {
        return totalInvoiceBalance;
    }

    public Double getRoundedTotalInvoiceBalance()
    {
        return Rounder.round(totalInvoiceBalance);
    }

    public void setTotalInvoiceBalance(final BigDecimal totalInvoiceBalance)
    {
        this.totalInvoiceBalance = totalInvoiceBalance;
    }

    public DateTime getUpdatedDt()
    {
        return updatedDt;
    }

    public void setUpdatedDt(final DateTime updatedDt)
    {
        this.updatedDt = updatedDt;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessAccount");
        sb.append("{balance=").append(balance);
        sb.append(", createdDt=").append(createdDt);
        sb.append(", updatedDt=").append(updatedDt);
        sb.append(", key='").append(key).append('\'');
        sb.append(", tags=").append(tags);
        sb.append(", lastInvoiceDate=").append(lastInvoiceDate);
        sb.append(", totalInvoiceBalance=").append(totalInvoiceBalance);
        sb.append(", lastPaymentStatus='").append(lastPaymentStatus).append('\'');
        sb.append(", paymentMethod='").append(paymentMethod).append('\'');
        sb.append(", creditCardType='").append(creditCardType).append('\'');
        sb.append(", billingAddressCountry='").append(billingAddressCountry).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BusinessAccount that = (BusinessAccount) o;

        if (balance != null ? !(Rounder.round(balance) == Rounder.round(that.balance)) : that.balance != null) {
            return false;
        }
        if (billingAddressCountry != null ? !billingAddressCountry.equals(that.billingAddressCountry) : that.billingAddressCountry != null) {
            return false;
        }
        if (createdDt != null ? !createdDt.equals(that.createdDt) : that.createdDt != null) {
            return false;
        }
        if (creditCardType != null ? !creditCardType.equals(that.creditCardType) : that.creditCardType != null) {
            return false;
        }
        if (key != null ? !key.equals(that.key) : that.key != null) {
            return false;
        }
        if (lastInvoiceDate != null ? !lastInvoiceDate.equals(that.lastInvoiceDate) : that.lastInvoiceDate != null) {
            return false;
        }
        if (lastPaymentStatus != null ? !lastPaymentStatus.equals(that.lastPaymentStatus) : that.lastPaymentStatus != null) {
            return false;
        }
        if (paymentMethod != null ? !paymentMethod.equals(that.paymentMethod) : that.paymentMethod != null) {
            return false;
        }
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) {
            return false;
        }
        if (totalInvoiceBalance != null ? !(Rounder.round(totalInvoiceBalance) == Rounder.round(that.totalInvoiceBalance)) : that.totalInvoiceBalance != null) {
            return false;
        }
        if (updatedDt != null ? !updatedDt.equals(that.updatedDt) : that.updatedDt != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = createdDt != null ? createdDt.hashCode() : 0;
        result = 31 * result + (updatedDt != null ? updatedDt.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (lastInvoiceDate != null ? lastInvoiceDate.hashCode() : 0);
        result = 31 * result + (totalInvoiceBalance != null ? totalInvoiceBalance.hashCode() : 0);
        result = 31 * result + (lastPaymentStatus != null ? lastPaymentStatus.hashCode() : 0);
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        result = 31 * result + (creditCardType != null ? creditCardType.hashCode() : 0);
        result = 31 * result + (billingAddressCountry != null ? billingAddressCountry.hashCode() : 0);
        return result;
    }
}
