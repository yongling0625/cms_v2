package online.lianxue.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import online.lianxue.cms.system.entity.SysUser;
import online.lianxue.cms.system.entity.SysUserToken;

/**
 * <p>
 * 用户Token 服务类
 * </p>
 *
 * @author lianxue
 * @since 2018-11-22
 */
public interface SysUserTokenService extends IService<SysUserToken> {

    /**
     * 生成token
     * @param sysUser
     * @return
     */
    SysUserToken createToken(SysUser sysUser);
}
