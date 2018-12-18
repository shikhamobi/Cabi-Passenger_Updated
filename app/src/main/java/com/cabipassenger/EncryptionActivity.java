package com.cabipassenger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cabipassenger.util.AESHelper;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by developer on 4/4/17.
 */
public class EncryptionActivity extends AppCompatActivity {

    EditText result,value;
    Button encrypt,decrypt;
    byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    String plainText;
    byte[] cipherData;
    byte[] keyBytes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aescheck);
        result=(EditText)findViewById(R.id.result);
        value=(EditText)findViewById(R.id.value);
        encrypt=(Button) findViewById(R.id.encrypt);
        String key = "e8ffc7e56311679f12b6fc91aa77a5eb";
        try {
             keyBytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        decrypt=(Button)findViewById(R.id.decrypt);
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    result.setText(AESHelper.decrypt(ivBytes,keyBytes,value.getText().toString().getBytes("UTF-8")).toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
            }
        });
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    result.setText(AESHelper.encrypt(ivBytes,keyBytes,value.getText().toString().getBytes("UTF-8")).toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //    public class CryptoExample {
//        public  void main(String[] args) {
//            final String password = "I AM SHERLOCKED";
//            final String salt = KeyGenerators.string().generateKey();
//
//            TextEncryptor encryptor = Encryptors.text(password, salt);
//            System.out.println("Salt: \"" + salt + "\"");
//
//            String textToEncrypt = "*royal secrets*";
//            System.out.println("Original text: \"" + textToEncrypt + "\"");
//
//            String encryptedText = encryptor.encrypt(textToEncrypt);
//            System.out.println("Encrypted text: \"" + encryptedText + "\"");
//
//            // Could reuse encryptor but wanted to show reconstructing TextEncryptor
//            TextEncryptor decryptor = Encryptors.text(password, salt);
//            String decryptedText = decryptor.decrypt(encryptedText);
//            System.out.println("Decrypted text: \"" + decryptedText + "\"");
//
//            if(textToEncrypt.equals(decryptedText)) {
//                System.out.println("Success: decrypted text matches");
//            } else {
//                System.out.println("Failed: decrypted text does not match");
//            }
//        }
//    }
}
