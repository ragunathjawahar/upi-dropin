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

/**
 * An interface with constants that are defined in the
 * <a href="http://www.npci.org.in/documents/UPI-Linking-Specs-ver-1.1_draft.pdf">UPI Linking
 * Specifications Version 1.1 (Draft)</a>.
 */
interface UpiParam {

    // Mandatory fields
    String PAYEE_ADDRESS = "pa";
    String PAYEE_NAME = "pn";

    // Conditional
    String TRANSACTION_REFERENCE_ID = "tr";

    // Optional
    String AMOUNT = "am";
    String MINIMUM_AMOUNT = "mam";
    String MERCHANT_CODE = "mc";
    String TRANSACTION_ID = "tid";
    String TRANSACTION_NOTE = "tn";
    String CURRENCY_CODE = "cu";
    String REFERENCE_URL = "url";

}
