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

package com.ning.billing.account.dao;

import com.google.inject.Inject;
import com.ning.billing.account.api.Account;
import com.ning.billing.account.api.IAccount;
import com.ning.billing.account.api.IAccountData;
import org.skife.jdbi.v2.IDBI;

import java.util.List;
import java.util.UUID;

public class AccountDao implements IAccountDao {

    private final IAccountDaoSql dao;

    @Inject
    public AccountDao(IDBI dbi) {
        this.dao = dbi.onDemand(IAccountDaoSql.class);
    }

    @Override
    public IAccount createAccount(IAccountData input) {
        IAccount result = new Account().withKey(input.getKey());
        dao.insertAccount(result);
        return result;
    }

    @Override
    public IAccount getAccountByKey(String key) {
        return dao.getAccountByKey(key);
    }

    @Override
    public IAccount getAccountById(UUID uid) {
        return dao.getAccountFromId(uid.toString());
    }

    @Override
    public List<IAccount> getAccounts() {
        return dao.getAccounts();
    }

    @Override
    public void test() {
        dao.test();
    }

    @Override
    public void save(IAccount account) {
        dao.insertAccount(account);
    }
}
