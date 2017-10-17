/*
* Copyright 2017 Gabor Varadi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.zhuinden.xkcdexample;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;

/**
 * Created by Owner on 2017. 10. 17..
 */

@Singleton
public class RealmManager {
    @Inject
    RealmManager() {
    }

    /**
     * The RealmManager allows creating a singleton Realm manager which can open thread-local instances.
     *
     * It also allows obtaining the open thread-local instance without incrementing the reference count.
     */

    private final ThreadLocal<Realm> localRealms = new ThreadLocal<>();

    /**
     * Opens a reference-counted local Realm instance.
     *
     * @return the open Realm instance
     */
    public Realm openLocalInstance() {
        checkDefaultConfiguration();
        Realm realm = Realm.getDefaultInstance();
        if(localRealms.get() == null) {
            localRealms.set(realm);
        }
        return realm;
    }

    /**
     * Returns the local Realm instance without adding to the reference count.
     *
     * @return the local Realm instance
     * @throws IllegalStateException when no Realm is open
     */
    public Realm getLocalInstance() {
        Realm realm = localRealms.get();
        if(realm == null) {
            throw new IllegalStateException("No open Realms were found on this thread.");
        }
        return realm;
    }

    /**
     * Returns if there is the instance on this thread is closed.
     *
     * @return if the Realm is closed
     */
    public boolean isClosed() {
        return localRealms.get() == null;
    }

    /**
     * Closes local Realm instance, decrementing the reference count.
     *
     * @throws IllegalStateException if there is no open Realm.
     */
    public void closeLocalInstance() {
        checkDefaultConfiguration();
        Realm realm = localRealms.get();
        if(realm == null) {
            throw new IllegalStateException("Cannot close a Realm that is not open.");
        }
        realm.close();
        // noinspection ConstantConditions
        if(Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()) <= 0) {
            localRealms.set(null);
        }
    }

    private void checkDefaultConfiguration() {
        if(Realm.getDefaultConfiguration() == null) {
            throw new IllegalStateException("No default configuration is set.");
        }
    }
}
