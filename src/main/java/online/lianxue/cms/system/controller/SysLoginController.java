package online.lianxue.cms.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import online.lianxue.cms.common.response.ApiResponse;
import online.lianxue.cms.common.utils.PasswordUtils;
import online.lianxue.cms.common.utils.RedisUtils;
import online.lianxue.cms.system.entity.SysUser;
import online.lianxue.cms.system.entity.SysUserToken;
import online.lianxue.cms.system.service.SysUserService;
import online.lianxue.cms.system.service.SysUserTokenService;
import online.lianxue.cms.system.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录控制器
 */
@RestController
public class SysLoginController {

	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private RedisUtils redisUtils;

	@GetMapping("/captcha.jpg")
	public void captcha(HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
//		// 保存到验证码到 redis
		redisUtils.set("kaptcha", text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);	
		out.close();
	}

	/**
	 * 登录接口
	 */
	@CrossOrigin
	@PostMapping(value = "/login")
	public ApiResponse login(@RequestBody LoginRequest loginRequest){
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		String captcha = loginRequest.getCaptcha();
		
		// 从session中获取之前保存的验证码跟前台传来的验证码进行匹配
		Object kaptcha = redisUtils.get("kaptcha");
//		if(kaptcha == null){
//			return ApiResponse.error("验证码已失效");
//		}
//		if(!captcha.equals(kaptcha)){
//			return ApiResponse.error("验证码不正确");
//		}
		
		// 用户信息
		SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("name",username));

		// 账号不存在、密码错误
		if (user == null) {
			return ApiResponse.error("账号不存在");
		}
		
		if (!match(user, password)) {
			return ApiResponse.error("密码不正确");
		}

		// 账号锁定
		if (user.getStatus() == 0) {
			return ApiResponse.error("账号已被锁定,请联系管理员");
		}

		// 生成token，并保存到数据库
		SysUserToken data = sysUserTokenService.createToken(user);
		return ApiResponse.ok(data);
	}

	/**
	 * 验证用户密码
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean match(SysUser user, String password) {
		return user.getPassword().equals(PasswordUtils.encrypte(password, user.getSalt()));
	}
	
	/**
	 * 登出接口
	 */
	@GetMapping(value = "/logout")
	public ApiResponse logout() {
//		ShiroUtils.logout();
		return ApiResponse.ok();
	}
}
