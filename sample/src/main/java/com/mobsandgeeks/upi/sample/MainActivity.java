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

package com.mobsandgeeks.upi.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mobsandgeeks.upi.UpiCallback;
import com.mobsandgeeks.upi.UpiDelegate;
import com.mobsandgeeks.upi.UpiDelegate.UpiError;
import com.mobsandgeeks.upi.UpiPayload;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements UpiCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UpiDelegate.isUpiIntent(getIntent())) {
            new UpiDelegate().handle(getIntent(), this);
        }
    }

    @Override
    public void onSuccess(UpiPayload payload, Map<String, String> extras, Uri upiUri) {
        Toast.makeText(this, payload.getPayeeName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(@UpiError int errorCode, Uri upiUri) {
        Toast.makeText(this, "Bummer!", Toast.LENGTH_SHORT).show();
    }

}
