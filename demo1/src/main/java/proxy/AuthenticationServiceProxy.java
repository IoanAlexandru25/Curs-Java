package proxy;

import com.mongodb.client.MongoCollection;
import controllers.LoginController;
import javafx.scene.control.Label;
import models.User;
import org.bson.Document;
import utils.MongoDBUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

public class AuthenticationServiceProxy implements AbstractAuthenticationService {
    private LoginController loginController;
    private static final int MAX_ATTEMPTS = 3;
    private static final long COOLDOWN_TIME = 3 * 60 * 1000;
    private AbstractAuthenticationService authenticationService;

    public AuthenticationServiceProxy(LoginController loginController, AbstractAuthenticationService authenticationService) {
        this.loginController = loginController;
        this.authenticationService = authenticationService;
    }

    @Override
    public User authentication(String username, String password, String deviceId) {
        MongoCollection<Document> deviceCollection = MongoDBUtil.getDevicesCollection();
        Document deviceDoc = deviceCollection.find(eq("deviceId", deviceId)).first();

        if (deviceDoc == null) {
            deviceDoc = new Document("deviceId", deviceId)
                    .append("failedAttempts", 0)
                    .append("lockUntil", null);
            deviceCollection.insertOne(deviceDoc);
        }

        int failedAttempts = deviceDoc.getInteger("failedAttempts", 0);
        Date lockUntil = deviceDoc.getDate("lockUntil");

        if (lockUntil != null && lockUntil.after(new Date())) {
            loginController.setFailLoginMessage("try again later");
            return null;
        }

        User user = authenticationService.authentication(username, password, deviceId);

        if (user == null) {
            failedAttempts++;
            Document updateDoc = new Document("failedAttempts", failedAttempts);

            if (failedAttempts >= MAX_ATTEMPTS) {
                Date lockTime = new Date(System.currentTimeMillis() + COOLDOWN_TIME);
                updateDoc.append("lockUntil", lockTime);
                loginController.setFailLoginMessage("try again later");
            }

            deviceCollection.updateOne(eq("deviceId", deviceId), new Document("$set", updateDoc));
        } else {
            deviceCollection.updateOne(eq("deviceId", deviceId), new Document("$set",
                    new Document("failedAttempts", 0).append("lockUntil", null)));
        }

        return user;
    }
}
