package com.jwaoo.account.sevice;

import com.jwaoo.common.core.config.Constants;
import com.jwaoo.common.core.redis.JedisManager;
import com.jwaoo.common.security.domain.MyUserDetail;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EmailVerifyCodeService {


    private static EmailVerifyCodeService instance = new EmailVerifyCodeService();

    private EmailVerifyCodeService(){}

    public static EmailVerifyCodeService getInstance(){
        return instance;
    }

	public String getCode(MyUserDetail user){
        long expires = System.currentTimeMillis() + 1000L * 7 * 3600 * 24;
		String code = computeSignature(user, expires);

		JedisManager.getInstance().set(Constants.REDIS_DB_0, code, user.getId().toString(), 3600 * 24 * 7L);
		
		return code;
	}
	
	public String findCode(String code){
		return JedisManager.getInstance().get(Constants.REDIS_DB_0, code);
	}
	
	public void ClearCode(String code){
        JedisManager.getInstance().get(Constants.REDIS_DB_0, code);
	}

    private String computeSignature(MyUserDetail userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getId());
        signatureBuilder.append(expires);
//        signatureBuilder.append(userDetails.getPassword());
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

}
