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

import android.net.Uri;

import com.mobsandgeeks.upi.UpiDelegate.UpiError;

import java.util.Map;

/**
 * A callback interface for posting results after parsing an UPI URI.
 *
 * @author Ragunath Jawahar
 */
public interface UpiCallback {

    /**
     * Called when the UPI URI was parsed successfully.
     *
     * @param payload Contains information parsed from the UPI URI.
     * @param extras Extra information contained in the URI that are not a part of the UPI spec.
     * @param upiUri The original URI.
     */
    void onSuccess(UpiPayload payload, Map<String, String> extras, Uri upiUri);

    /**
     * Called when there was a failure in parsing the UPI URI.
     *
     * @param errorCode An error code stating the reason why there was an error parsing the URI.
     * @param upiUri The original URI.
     */
    void onFailure(@UpiError int errorCode, Uri upiUri);

}
