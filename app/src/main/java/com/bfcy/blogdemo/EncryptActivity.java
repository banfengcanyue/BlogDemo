package com.bfcy.blogdemo;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.bfcy.blogdemo.encrypt.CommonUtil;
import com.bfcy.blogdemo.encrypt.DesUtil;


public class EncryptActivity extends BaseActivity {

    /**
     * 源字符串
     */
    private String sourceString = "Hello WuXiaAChao!";
    /**
     * 同一字符串，不同秘钥，得到的加密字符串不同
     * 010203040506070809  1234567890abcdefg
     */
    private String privateKey = "1234567890abcdefg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        initView();
    }

    private void initView() {
        Log.i(TAG, "encrypt--Test--源字符串: " + sourceString);
        testBase64();
        testMD5();
        testDes();
        testUpsetData();
    }

    private void testUpsetData() {
        String aaa = CommonUtil.upsetData(sourceString);
        String bbb = CommonUtil.recoveryData(aaa);
        Log.i(TAG, "encrypt--upsetData--打乱后的数据: " + aaa);
        Log.i(TAG, "encrypt--upsetData--还原的数据: " + bbb);
    }

    private void testBase64() {
        String strEncode = Base64.encodeToString(sourceString.getBytes(), Base64.DEFAULT);
        String strDecode = new String(Base64.decode(strEncode, Base64.DEFAULT));
        Log.i(TAG, "encrypt--Base64--加密后数据: " + strEncode);
        Log.i(TAG, "encrypt--Base64--解密后数据: " + strDecode);
    }

    private void testMD5() {

    }

    private void testDes() {
        String strEncode = DesUtil.encrypt(privateKey, sourceString);
        String strDecode = DesUtil.decrypt(privateKey, strEncode);
        Log.i(TAG, "encrypt--DES--加密后数据: " + strEncode);
        Log.i(TAG, "encrypt--DES--解密后数据: " + strDecode);
    }
}
