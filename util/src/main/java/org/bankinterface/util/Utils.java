/*
 * Copyright 2013 bankinterface.org
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
package org.bankinterface.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 支付接口专用工具类
 * 
 */
public class Utils {

    /** MD5消息摘要算法 */
    public static final String      ALGORITHM_MD5         = "MD5";
    /** SHA消息摘要算法 */
    public static final String      ALGORITHM_SHA         = "SHA";
    /** SHA1withRSA签名算法 */
    public static final String      ALGORITHM_SHA1WITHRSA = "SHA1withRSA";

    /** GBK字符集 */
    public static final String      CHARSET_GBK           = "GBK";
    /** UTF-8字符集 */
    public static final String      CHARSET_UTF8          = "UTF-8";

    /** BASE64编码 */
    public static final String      CODE_BASE64           = "BASE64";
    /** HEX编码 */
    public static final String      CODE_HEX              = "HEX";
    /** GBK字符集内容URL编码 */
    public static final String      CODE_URL_GBK          = "URL_GBK";
    /** UTF-8字符集内容URL编码 */
    public static final String      CODE_URL_UTF8         = "URL_UTF-8";

    private static final BigDecimal ONE_HUNDRED           = new BigDecimal(100);

    /**
     * 金额格式化,以分为单位,精确到分,之后四舍五入.
     * 
     * @param amount
     * @return
     */
    public static String amountToFen(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException();
        }
        return amount.multiply(ONE_HUNDRED).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 金额格式化,以元为单位,精确到分,之后四舍五入.
     * 
     * @param amount
     * @return
     */
    public static String amountToYuan(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException();
        }
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 时间格式化,依据pattern进行转换.
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if ((date == null) || (pattern == null)) {
            throw new IllegalArgumentException();
        }
        return new SimpleDateFormat(pattern).format(date).toString();
    }

    /**
     * 字符串到金额类型转换,以分为单位,精确到分,之后四舍五入.
     * 
     * @param amount
     * @return
     */
    public static BigDecimal parseFromFen(String amount) {
        if (amount == null) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(amount).divide(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 字符串到金额类型转换,以元为单位,精确到分,之后四舍五入.
     * 
     * @param amount
     * @return
     */
    public static BigDecimal parseFromYuan(String amount) {
        if (amount == null) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 字符串到时间类型转换,依据pattern进行转换.
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date parseDate(String date, String pattern) {
        if ((date == null) || (pattern == null)) {
            throw new IllegalArgumentException();
        }
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断指定对象是否为空
     * 
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).length() == 0;
        }
        if (value instanceof Map) {
            return ((Map<? extends Object, ? extends Object>) value).size() == 0;
        }
        if (value instanceof Collection) {
            return ((Collection<? extends Object>) value).size() == 0;
        }
        if (value instanceof java.util.Date) {
            return false;
        }
        if (value instanceof Boolean) {
            return false;
        }
        if (value instanceof Number) {
            return false;
        }
        if (value instanceof Character) {
            return false;
        }
        if (value instanceof CharSequence) {
            return ((CharSequence) value).length() == 0;
        }
        return false;
    }

    /**
     * 用指定的编码对指定的值编码
     * 
     * @param data
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(byte[] data, String code) throws UnsupportedEncodingException {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        if (code == null) {
            return new String(data);
        }
        if (Utils.CODE_BASE64.equals(code)) {
            return Base64.encodeBase64String(data);
        } else if (Utils.CODE_HEX.equals(code)) {
            return Hex.encodeHexString(data);
        } else if (Utils.CODE_URL_UTF8.equals(code)) {
            return URLEncoder.encode(new String(data, CHARSET_UTF8), CHARSET_UTF8);
        } else if (Utils.CODE_URL_GBK.equals(code)) {
            return URLEncoder.encode(new String(data, CHARSET_GBK), CHARSET_GBK);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 用指定的编码对指定的值反编码
     * 
     * @param data
     * @param code
     * @return
     * @throws DecoderException
     * @throws UnsupportedEncodingException
     */
    public static String decode(String data, String code) throws DecoderException, UnsupportedEncodingException {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        if (code == null) {
            return data;
        }
        if (Utils.CODE_BASE64.equals(code)) {
            return new String(Base64.decodeBase64(data));
        } else if (Utils.CODE_HEX.equals(code)) {
            return new String(Hex.decodeHex(data.toCharArray()));
        } else if (Utils.CODE_URL_UTF8.equals(code)) {
            return URLDecoder.decode(data, CHARSET_UTF8);
        } else if (Utils.CODE_URL_GBK.equals(code)) {
            return URLDecoder.decode(data, CHARSET_GBK);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 获取指定字符串和字符编码(默认UTF-8)的字节数组
     * 
     * @param data
     * @param charset
     * @return
     */
    public static byte[] getBytes(String data, String charset) {
        try {
            return data.getBytes(charset == null ? CHARSET_UTF8 : charset);
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对字符串进行SHA1withRSA数字签名处理,并把结果转换为指定的编码
     * 
     * @param data
     * @param charset
     * @param certFilePath
     * @param privateKeyAlias
     * @param code
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static String signSHA1withRSA(String data, String charset, String certFilePath, String privateKeyAlias,
            String code) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        PrivateKey privateKey = KeyStoreUtil.getPrivateKey(certFilePath, privateKeyAlias);
        Signature signature = Signature.getInstance(ALGORITHM_SHA1WITHRSA);
        signature.initSign(privateKey);
        signature.update(getBytes(data, charset));
        byte[] bytes = signature.sign();
        return Utils.encode(bytes, code);
    }

    /**
     * 对字符串进行SHA1withRSA签名验证
     * 
     * @param sourceData
     * @param signData
     * @param certFilePath
     * @param publicKeyAlias
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws SignatureException
     */
    public static boolean verifySHA1withRSA(String sourceData, String signData, String charset, String certFilePath,
            String publicKeyAlias) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            UnsupportedEncodingException {
        PublicKey publicKey = KeyStoreUtil.getPublicKey(certFilePath, publicKeyAlias);
        Signature signature = Signature.getInstance(ALGORITHM_SHA1WITHRSA);
        signature.initVerify(publicKey);
        signature.update(getBytes(sourceData, charset));
        return signature.verify(getBytes(signData, charset));
    }

    /**
     * 对字符串进行消息摘要处理,并把结果转换为指定的编码
     * 
     * @param algorithm
     * @param data
     * @param charset
     * @param code
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String messageDigest(String algorithm, String data, String charset, String code)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(getBytes(data, charset));
        byte[] bytes = messageDigest.digest();
        return Utils.encode(bytes, code);
    }
}
