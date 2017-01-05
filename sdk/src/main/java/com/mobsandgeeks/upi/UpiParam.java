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

import java.util.Arrays;
import java.util.List;

/**
 * A class with constants that are defined in the
 * <a href="http://www.npci.org.in/documents/UPI-Linking-Specs-ver-1.1_draft.pdf">UPI Linking
 * Specifications Version 1.1 (Draft)</a>.
 */
final class UpiParam {

    // Mandatory fields
    static final String PAYEE_ADDRESS = "pa";
    static final String PAYEE_NAME = "pn";

    // Conditional
    static final String TRANSACTION_REFERENCE_ID = "tr";

    // Optional
    static final String AMOUNT = "am";
    static final String MINIMUM_AMOUNT = "mam";
    static final String MERCHANT_CODE = "mc";
    static final String TRANSACTION_ID = "tid";
    static final String TRANSACTION_NOTE = "tn";
    static final String CURRENCY_CODE = "cu";
    static final String REFERENCE_URL = "url";

    /**
     * List of all UPI parameters.
     */
    static final List<String> ALL_PARAMETERS = Arrays.asList(
            UpiParam.AMOUNT, UpiParam.CURRENCY_CODE, UpiParam.MERCHANT_CODE,
            UpiParam.MINIMUM_AMOUNT, UpiParam.PAYEE_ADDRESS, UpiParam.PAYEE_NAME,
            UpiParam.REFERENCE_URL, UpiParam.TRANSACTION_ID, UpiParam.TRANSACTION_NOTE,
            UpiParam.TRANSACTION_REFERENCE_ID
    );

    private UpiParam() { /* No instances. */ }

}
