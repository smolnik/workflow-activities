package net.adamsmolnik.workflow.local;

import com.amazonaws.auth.AWSCredentials;

/**
 * @author ASmolnik
 *
 */
public class SystemCredentials implements AWSCredentials {

    private final String accessKeyId;

    private final String secretKey;

    public SystemCredentials() {
        accessKeyId = System.getProperty("AWS_ACCESS_KEY_ID");
        secretKey = System.getProperty("AWS_SECRET_KEY");
    }

    @Override
    public String getAWSAccessKeyId() {
        return accessKeyId;
    }

    @Override
    public String getAWSSecretKey() {
        return secretKey;
    }
}
