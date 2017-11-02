/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.AppConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ThirtyDegreesRay on 2017/10/23 11:29:30
 */

public class SignatureUtils {

    private final static String TAG = SignatureUtils.class.getSimpleName();

    public static boolean isReleaseSign(@NonNull Context context){
        return AppConfig.OPENHUB_RELEASE_SIGN_MD5.equals(getSignMd5(context));
    }

    /**
     * get sign md5 of current application
     */
    public static String getSignMd5(@NonNull Context context){
        String signMD5 = null;
        try {
            Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;
            signMD5 = getMd5(sigs[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
        return signMD5;
    }

    /**
     * get sign md5 by signature, example C0:99:37:D9:6A:36:FB:B7:AB:4C:5E:3D:42:96:FA:AF
     */
    private static String getMd5 (Signature signature) throws Exception {
        return encryptionMD5(signature.toByteArray());
    }

    private static String encryptionMD5(byte[] byteStr) throws Exception {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
                if(i != byteArray.length - 1){
                    md5StrBuff.append(":");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString().toUpperCase();
    }

}