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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        String payeeName = upiUri.getQueryParameter(UpiParam.PAYEE_NAME);
        String payeeAddress = upiUri.getQueryParameter(UpiParam.PAYEE_ADDRESS);

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
        return builder.transactionReferenceId(upiUri.getQueryParameter(UpiParam.TRANSACTION_REFERENCE_ID))
                .merchantCode(upiUri.getQueryParameter(UpiParam.MERCHANT_CODE))
                .transactionId(upiUri.getQueryParameter(UpiParam.TRANSACTION_ID))
                .transactionNote(upiUri.getQueryParameter(UpiParam.TRANSACTION_NOTE))
                .currencyCode(upiUri.getQueryParameter(UpiParam.CURRENCY_CODE))
                .referenceUrl(upiUri.getQueryParameter(UpiParam.REFERENCE_URL))
                .payeeAmount(toBigDecimal(upiUri.getQueryParameter(UpiParam.AMOUNT)))
                .minimumAmount(toBigDecimal(upiUri.getQueryParameter(UpiParam.MINIMUM_AMOUNT)))
                .build();
    }

    @Nullable
    private Map<String, String> getExtras(Uri upiUri) {
        final List<String> upiParams = Arrays.asList(
                UpiParam.AMOUNT, UpiParam.CURRENCY_CODE, UpiParam.MERCHANT_CODE,
                UpiParam.MINIMUM_AMOUNT, UpiParam.PAYEE_ADDRESS, UpiParam.PAYEE_NAME,
                UpiParam.REFERENCE_URL, UpiParam.TRANSACTION_ID, UpiParam.TRANSACTION_NOTE,
                UpiParam.TRANSACTION_REFERENCE_ID
        );

        Map<String, String> extras = null;
        Set<String> queryParameterNames = new HashSet<>(upiUri.getQueryParameterNames());
        for (int i = 0, n = upiParams.size(); i < n; i++) {
            queryParameterNames.remove(upiParams.get(i));
        }

        if (queryParameterNames.size() > 0) {
            extras = new HashMap<>();
            for (String name : queryParameterNames) {
                extras.put(name, upiUri.getQueryParameter(name));
            }
        }

        return extras != null ? Collections.unmodifiableMap(extras) : null;
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
