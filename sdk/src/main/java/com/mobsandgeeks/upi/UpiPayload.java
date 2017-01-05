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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

/**
 * A payload that contains all values that are described in the
 * <a href="http://www.npci.org.in/documents/UPI-Linking-Specs-ver-1.1_draft.pdf">UPI Linking
 * Specifications Version 1.1 (Draft)</a>.
 *
 * @author Ragunath Jawahar
 */
public final class UpiPayload implements Parcelable {

    @NonNull private final String payeeName;
    @NonNull private final String payeeAddress;
    private final String merchantCode;
    private final String transactionId;
    private final String transactionReferenceId;
    private final String transactionNote;
    private final BigDecimal payeeAmount;
    private final BigDecimal minimumAmount;
    private final String currencyCode;
    private final String referenceUrl;

    /**
     * Private constructor. Used by the {@link Builder} method.
     */
    private UpiPayload(@NonNull String payeeName,
               @NonNull String payeeAddress,
               String merchantCode,
               String transactionId,
               String transactionReferenceId,
               String transactionNote,
               BigDecimal payeeAmount,
               BigDecimal minimumAmount,
               String currencyCode,
               String referenceUrl) {
        this.payeeName = payeeName;
        this.payeeAddress = payeeAddress;
        this.merchantCode = merchantCode;
        this.transactionId = transactionId;
        this.transactionReferenceId = transactionReferenceId;
        this.transactionNote = transactionNote;
        this.payeeAmount = payeeAmount;
        this.minimumAmount = minimumAmount;
        this.currencyCode = currencyCode;
        this.referenceUrl = referenceUrl;
    }

    private UpiPayload(Parcel in) {
        this.payeeName = in.readString();
        this.payeeAddress = in.readString();
        this.merchantCode = in.readString();
        this.transactionId = in.readString();
        this.transactionReferenceId = in.readString();
        this.transactionNote = in.readString();
        this.payeeAmount = (BigDecimal) in.readSerializable();
        this.minimumAmount = (BigDecimal) in.readSerializable();
        this.currencyCode = in.readString();
        this.referenceUrl = in.readString();
    }

    /**
     * @return Payee name. This field is mandatory.
     */
    @NonNull
    public String getPayeeName() {
        return payeeName;
    }

    /**
     * @return Payee address. This field is mandatory.
     */
    @NonNull
    public String getPayeeAddress() {
        return payeeAddress;
    }

    /**
     * @return Payee merchant code.
     */
    @Nullable
    public String getMerchantCode() {
        return merchantCode;
    }

    /**
     * This is a PSP generated ID when present. In the case of Merchant payments, merchant may
     * acquire the transaction ID from his PSP.
     *
     * @return The PSP generated transaction ID.
     */
    @Nullable
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Transaction reference ID. This could be an order number, subscription number, bill ID,
     * booking ID, insurance renewal reference, etc., This field is mandatory only for merchant
     * transactions.
     *
     * @return The transaction reference ID.
     */
    @Nullable
    public String getTransactionReferenceId() {
        return transactionReferenceId;
    }

    /**
     * Transaction note providing a short description of the transaction.
     *
     * @return A transaction note if present.
     */
    @Nullable
    public String getTransactionNote() {
        return transactionNote;
    }

    /**
     * @return The transaction amount.
     */
    @Nullable
    public BigDecimal getPayeeAmount() {
        return payeeAmount;
    }

    /**
     * Minimum amount to be paid if different from transaction amount.
     *
     * @return The minimum amount.
     */
    @Nullable
    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    /**
     * Currency code. Currently ONLY "INR" is the supported value.
     *
     * @return The currency code.
     */
    @Nullable
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * This should be a URL when clicked provides customer with further transaction details like
     * complete bill details, bill copy, order copy, ticket details, etc. This can also be used to
     * deliver digital goods such as mp3 files etc., after payment.
     *
     * This URL, when used, MUST BE related to the particular transaction and MUST NOT be used to
     * send unsolicited information that are not relevant to the transaction.
     *
     * @return A reference URL related to the current transaction.
     */
    public String getReferenceUrl() {
        return referenceUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payeeName);
        dest.writeString(this.payeeAddress);
        dest.writeString(this.merchantCode);
        dest.writeString(this.transactionId);
        dest.writeString(this.transactionReferenceId);
        dest.writeString(this.transactionNote);
        dest.writeSerializable(this.payeeAmount);
        dest.writeSerializable(this.minimumAmount);
        dest.writeString(this.currencyCode);
        dest.writeString(this.referenceUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UpiPayload> CREATOR = new Parcelable.Creator<UpiPayload>() {
        @Override
        public UpiPayload createFromParcel(Parcel source) {
            return new UpiPayload(source);
        }

        @Override
        public UpiPayload[] newArray(int size) {
            return new UpiPayload[size];
        }
    };

    static Builder builder(@NonNull String payeeName, @NonNull String payeeAddress) {
        return new Builder(payeeName, payeeAddress);
    }

    static final class Builder {

        private static final String DEFAULT_CURRENCY_CODE = "INR";

        @NonNull private String payeeName;
        @NonNull private String payeeAddress;
        private String merchantCode;
        private String transactionId;
        private String transactionReferenceId;
        private String transactionNote;
        private BigDecimal payeeAmount;
        private BigDecimal minimumAmount;
        private String currencyCode;
        private String referenceUrl;

        Builder(@NonNull String payeeName, @NonNull String payeeAddress) {
            this.payeeAddress = payeeAddress;
            this.payeeName = payeeName;
        }

        Builder merchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
            return this;
        }

        Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        Builder transactionReferenceId(String transactionReferenceId) {
            this.transactionReferenceId = transactionReferenceId;
            return this;
        }

        Builder transactionNote(String transactionNote) {
            this.transactionNote = transactionNote;
            return this;
        }

        Builder payeeAmount(BigDecimal payeeAmount) {
            this.payeeAmount = payeeAmount;
            return this;
        }

        Builder minimumAmount(BigDecimal minimumAmount) {
            this.minimumAmount = minimumAmount;
            return this;
        }

        Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode == null ? DEFAULT_CURRENCY_CODE : currencyCode;
            return this;
        }

        Builder referenceUrl(String referenceUrl) {
            this.referenceUrl = referenceUrl;
            return this;
        }

        UpiPayload build() {
            return new UpiPayload(payeeName, payeeAddress, merchantCode, transactionId,
                    transactionReferenceId, transactionNote, payeeAmount, minimumAmount,
                    currencyCode, referenceUrl);
        }

    }

}
