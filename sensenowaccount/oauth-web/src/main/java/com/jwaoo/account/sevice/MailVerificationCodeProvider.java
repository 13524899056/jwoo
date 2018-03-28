package com.jwaoo.account.sevice;

import com.google.common.collect.Maps;
import com.jwaoo.account.config.Constants;
import com.jwaoo.account.util.DateTimeUtils;
import com.jwaoo.account.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class MailVerificationCodeProvider {

	private final Logger log = LoggerFactory.getLogger(MailVerificationCodeProvider.class);

    private static Map<String, CacheCodeDto> VerificationCodeCache = Maps.newHashMap();
    /** 验证码有效时间 */
    private final static int VERIFICATION_CODE_END_IN_MINUTES = 60 * 24;

    private static MailVerificationCodeProvider instance = new MailVerificationCodeProvider();

    private MailVerificationCodeProvider (){}

    public static MailVerificationCodeProvider getInstance(){
        return instance;
    }

    public String createCode() {
        return RandomUtil.generateVerificationCode();
    }


    public void cacheCode(String loginName, String code){
        StringBuilder sb = new StringBuilder(code);
        sb.append(loginName);
        try {
	        InputStream is = new ByteArrayInputStream(sb.toString().getBytes(Constants.DEFAULT_CHARSET));
	        String codeMd5 = Encodes.encodeHex(Digests.md5(is));
	        long endTime = DateTimeUtils.afterMinutes(VERIFICATION_CODE_END_IN_MINUTES);
	        VerificationCodeCache.put(codeMd5, new CacheCodeDto(code, loginName, endTime));
        }catch (Exception e){
        	log.error("cacheCode error " + e);
        }
    }

    public String validateCode(String codeMd5) {
        // 短信MD5获取短信明文
        if (!VerificationCodeCache.containsKey(codeMd5)) {
            // 无此短信检验码
            return null;
        }
        CacheCodeDto codeInfo = VerificationCodeCache.get(codeMd5);
        // 短信生命期检查
        if (DateTimeUtils.isBeforNow(codeInfo.getEndTime())) {
            // 短信检验码已经过期
            return null;
        }
        return String.valueOf(codeInfo.getCode());
    }


    private class CacheCodeDto {
        private  String code;
        private  String loginName;
        private  long endTime;

        public CacheCodeDto(String code, String loginName, long endTime) {
            this.code = code;
            this.loginName = loginName;
            this.endTime = endTime;
        }

        public String getCode() {
            return code;
        }

        public long getEndTime() {
            return endTime;
        }

    }
}
