package online.lianxue.cms.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.lianxue.cms.common.utils.RedisUtils;
import online.lianxue.cms.shiro.JWTUtil;
import online.lianxue.cms.system.entity.SysUser;
import online.lianxue.cms.system.entity.SysUserToken;
import online.lianxue.cms.system.mapper.SysUserTokenMapper;
import online.lianxue.cms.system.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用户Token 服务实现类
 * </p>
 *
 * @author lianxue
 * @since 2018-11-22
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements SysUserTokenService {

    @Autowired
    private RedisUtils redisUtils;

    private static final String TOKEN_PREFIX = "token:";

    @Override
    public SysUserToken createToken(SysUser sysUser) {
        // 生成一个token
        String token = JWTUtil.generateToken(sysUser.getId(), sysUser.getName(), sysUser.getPassword());
        // 判断是否生成过token
        SysUserToken sysUserToken = null;
        if (Objects.isNull(sysUserToken)) {
            sysUserToken = new SysUserToken();
            sysUserToken.setUserId(sysUser.getId());
        }
        sysUserToken.setToken(token);
        sysUserToken.setExpireTime(LocalDateTime.now().plusDays(1));

        redisUtils.set(TOKEN_PREFIX + sysUser.getId(), sysUserToken);
        return sysUserToken;
    }
}
