UPI Dropin for Android
======================
This is an open-source Android SDK that implements NPCI's [UPI Linking Specifications Version 1.1 (Draft)](http://www.npci.org.in/documents/UPI-Linking-Specs-ver-1.1_draft.pdf).

The purpose of this SDK is to have as many eye-balls as possible to audit and improve the codebase.

Usage
-----
1. In your `AndroidManifest.xml`, add an intent filter to the `Activity` in which you want to handle UPI intents.
````xml
<activity android:name=".UpiPaymentActivity">

    <!-- Intent Filter for UPI URIs -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="upi" android:pathPattern=".*" />
    </intent-filter>
</activity>
````

2. Implement `UpiCallback` and handle incoming intents in your `Activity`.
````java
public class UpiPaymentActivity extends AppCompatActivity implements UpiCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // More code…

        if (UpiDelegate.isUpiIntent(getIntent())) {
            new UpiDelegate().handle(getIntent(), this);
        }
    }

    @Override
    public void onSuccess(UpiPayload payload, Map<String, String> extras, Uri upiUri) {
        // Success callback…
    }

    @Override
    public void onFailure(@UpiError int errorCode, Uri upiUri) {
        // Failure callback…
    }
}
````

License
-------

    Copyright 2016 Ragunath Jawahar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
