package online.lianxue.cms.system.dto;

import lombok.Data;

/**
 * 登录接口封装对象
 */
@Data
public class LoginRequest {

	private String username;
	private String password;
	private String captcha;
	
}
