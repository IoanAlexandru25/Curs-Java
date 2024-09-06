package proxy;

import models.User;

public interface AbstractAuthenticationService {
    User authentication(String username, String password, String deviceId);
}
