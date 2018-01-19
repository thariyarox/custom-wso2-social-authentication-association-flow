# custom-wso2-social-authentication-association-flow

This is a local authenticator for WSO2 Identity Server 5.3.0 which can be added as the step 2 of the local and outbound authentication flow of a service provider.

Step 1 of the authentication flow should be facebook federated authentication.

When using this service provider, in the very first time, user has to login to facebook (step 1) and then to the local user account in Identity Server (step 2).
After completing both steps, this authenticator will create a user association with the facebook user id and local user id.

From the next login, after user logs into the same facebook account, since there is already a user association with that facebook account, the step 2 authenticaiton will not be prompted.

The user association is created in-memory for demonstration purpose and will be destroyed upon server restart.
