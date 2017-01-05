/*
 * Copyright (C) 2016 Ragunath Jawahar
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobsandgeeks.upi;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mobsandgeeks.upi.UpiParam.ALL_PARAMETERS;
import static com.mobsandgeeks.upi.UpiParam.AMOUNT;
import static com.mobsandgeeks.upi.UpiParam.CURRENCY_CODE;
import static com.mobsandgeeks.upi.UpiParam.MERCHANT_CODE;
import static com.mobsandgeeks.upi.UpiParam.MINIMUM_AMOUNT;
import static com.mobsandgeeks.upi.UpiParam.PAYEE_ADDRESS;
import static com.mobsandgeeks.upi.UpiParam.PAYEE_NAME;
import static com.mobsandgeeks.upi.UpiParam.REFERENCE_URL;
import static com.mobsandgeeks.upi.UpiParam.TRANSACTION_ID;
import static com.mobsandgeeks.upi.UpiParam.TRANSACTION_NOTE;
import static com.mobsandgeeks.upi.UpiParam.TRANSACTION_REFERENCE_ID;

/**
 * This class is useful to detect and handle UPI URIs and intents.
 *
 * @author Ragunath Jawahar
 */
public class UpiDelegate {

    /**
     * Missing Payee Address in URI.
     */
    @SuppressWarnings("WeakerAccess")
    public static final int ERROR_MISSING_PAYEE_ADDRESS = 100;

    /**
     * Missing Payee Address in URI.
     */
    @SuppressWarnings("WeakerAccess")
    public static final int ERROR_MISSING_PAYEE_NAME = 101;

    private static final String UPI_SCHEME = "upi";

    @IntDef({ ERROR_MISSING_PAYEE_ADDRESS, ERROR_MISSING_PAYEE_NAME })
    public @interface UpiError {}

    /**
     * Checks if an Intent is a UPI intent.
     *
     * @param intent A intent whose data has to be checked.
     * @return {@code true} if the intent is an UPI intent, {@code false} otherwise.
     */
    public static boolean isUpiIntent(Intent intent) {
        if (intent == null) {
            return false;
        }

        Uri uri = intent.getData();
        return uri != null && isUpiUri(uri);
    }

    /**
     * Checks if the given URI is a UPI URI.
     *
     * @param uri The URI to check.
     * @return {@code true} if the URI is an UPI intent, {@code false} otherwise.
     */
    public static boolean isUpiUri(@NonNull Uri uri) {
        return UPI_SCHEME.equals(uri.getScheme().toLowerCase());
    }

    /**
     * Handles the UPI intent. Make sure to check if the intent is a valid UPI intent using the
     * {@link #isUpiIntent(Intent)} method before calling this method.
     *
     * @param upiIntent The UPI intent to be handled.
     * @param callback A callback to notify results.
     */
    public void handle(@NonNull Intent upiIntent, @NonNull UpiCallback callback) {
        Uri upiUri = upiIntent.getData();

        // Mandatory fields
        String payeeName = upiUri.getQueryParameter(PAYEE_NAME);
        String payeeAddress = upiUri.getQueryParameter(PAYEE_ADDRESS);

        // Check for missing mandatory fields and throw an error
        @UpiError int error = -1;
        if (payeeName == null || TextUtils.isEmpty(payeeName.trim())) {
            error = ERROR_MISSING_PAYEE_NAME;
        } else if (payeeAddress == null || TextUtils.isEmpty(payeeAddress.trim())) {
            error = ERROR_MISSING_PAYEE_ADDRESS;
        } // FIXME: 05/01/17 What if both are missing?

        // noinspection WrongConstant
        if (error != -1) {
            callback.onFailure(error, upiUri);
            return;
        }

        // Success call
        UpiPayload payload = getPayload(upiUri, UpiPayload.builder(payeeName, payeeAddress));
        Map<String, String> extras = getExtras(upiUri);
        callback.onSuccess(payload, extras, upiUri);
    }

    private UpiPayload getPayload(Uri upiUri, UpiPayload.Builder builder) {
        return builder.transactionReferenceId(upiUri.getQueryParameter(TRANSACTION_REFERENCE_ID))
                .merchantCode(upiUri.getQueryParameter(MERCHANT_CODE))
                .transactionId(upiUri.getQueryParameter(TRANSACTION_ID))
                .transactionNote(upiUri.getQueryParameter(TRANSACTION_NOTE))
                .currencyCode(upiUri.getQueryParameter(CURRENCY_CODE))
                .referenceUrl(upiUri.getQueryParameter(REFERENCE_URL))
                .payeeAmount(toBigDecimal(upiUri.getQueryParameter(AMOUNT)))
                .minimumAmount(toBigDecimal(upiUri.getQueryParameter(MINIMUM_AMOUNT)))
                .build();
    }

    @Nullable
    private Map<String, String> getExtras(Uri upiUri) {
        Set<String> queryParameterNames = new HashSet<>(upiUri.getQueryParameterNames());
        queryParameterNames.removeAll(ALL_PARAMETERS);

        Map<String, String> extras;
        if (queryParameterNames.size() > 0) {
            extras = new HashMap<>();
            for (String name : queryParameterNames) {
                extras.put(name, upiUri.getQueryParameter(name));
            }
            extras = Collections.unmodifiableMap(extras);
        } else {
            extras = Collections.emptyMap();
        }

        return extras;
    }

    @Nullable
    private BigDecimal toBigDecimal(String amount) {
        try {
            return amount != null ? new BigDecimal(amount) : null;
        } catch (Exception e) {
            return null;
        }
    }

}
