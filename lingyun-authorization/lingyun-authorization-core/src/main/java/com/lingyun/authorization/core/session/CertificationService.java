package com.lingyun.authorization.core.session;

import java.util.Collection;

import com.lingyun.authorization.core.entity.Credential;
import com.lingyun.authorization.core.entity.User;

public interface CertificationService {
    /**
     * 验证身份, 实际应该由子单元进行
     * 
     * @see Authenticator
     * @return 令牌
     */
    String issueToken(Credential credential);

    Collection<Authenticator<?>> getAuthenticators();

    void removeToken();

    /**
     * 负责验证身份的子单元
     */
    interface Authenticator<T extends Credential> {
        User authenticate(Credential credential);

        Class<T> getSupportedCredentialType();
    }
}
