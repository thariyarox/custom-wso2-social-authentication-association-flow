package com.wso2.custom.basic.authenticator;

import com.wso2.custom.basic.authenticator.util.UserAssociationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorFlowStatus;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authentication.framework.exception.LogoutFailedException;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedIdPData;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.authenticator.basicauth.BasicAuthenticator;
import org.wso2.carbon.identity.application.common.model.Claim;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CustomBasicAuthenticator extends BasicAuthenticator {

    private static Log log = LogFactory.getLog(CustomBasicAuthenticator.class);


    private static final String AUTHENTICATOR_FRIENDLY_NAME = "FB Social Authenticator";
    private static final String AUTHENTICATOR_NAME = "FBSocialAuthenticator";

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return super.canHandle(request);
    }

    @Override
    public AuthenticatorFlowStatus process(HttpServletRequest request, HttpServletResponse response, AuthenticationContext context) throws AuthenticationFailedException, LogoutFailedException {

        try {

            AuthenticatedIdPData data = (AuthenticatedIdPData) context.getCurrentAuthenticatedIdPs().entrySet().iterator().next().getValue();

            String facebookUserId = getFacebookUserIdentifier(data.getUser().getUserAttributes());

            AuthenticatedUser authenticatedUser = UserAssociationUtil.getAssociatedLocalUserIDByFederatedUserId(facebookUserId);

            if (authenticatedUser != null && authenticatedUser.getAuthenticatedSubjectIdentifier() != null) {

                context.setSubject(authenticatedUser);

                return AuthenticatorFlowStatus.SUCCESS_COMPLETED;

            }
        } catch (Exception e) {
            // do nothing
        }

        return super.process(request, response, context);
    }

    @Override
    protected void initiateAuthenticationRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationContext context) throws AuthenticationFailedException {

        super.initiateAuthenticationRequest(request, response, context);

    }

    @Override
    protected void processAuthenticationResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationContext context) throws AuthenticationFailedException {

        AuthenticatedIdPData data = (AuthenticatedIdPData) context.getCurrentAuthenticatedIdPs().entrySet().iterator().next().getValue();

        String facebookUserId = getFacebookUserIdentifier(data.getUser().getUserAttributes());

        //proceed with basic authentication
        super.processAuthenticationResponse(request, response, context);

        //Get the authenticated user from the context
        AuthenticatedUser authenticatedUser = context.getSubject();

        if(authenticatedUser != null && authenticatedUser.getAuthenticatedSubjectIdentifier() != null){

            // create user association
            UserAssociationUtil.createUserAssociation(facebookUserId, authenticatedUser);
        }
    }

    @Override
    protected boolean retryAuthenticationEnabled() {
        return super.retryAuthenticationEnabled();
    }

    @Override
    public String getContextIdentifier(HttpServletRequest request) {
        return super.getContextIdentifier(request);
    }

    @Override
    public String getFriendlyName() {

        //This is the name listed in the dropdown in Local & Outbound Authenticators section of Service Provider configuration
        return AUTHENTICATOR_FRIENDLY_NAME;
    }

    @Override
    public String getName() {

        //This is the name of the authenticator coming in the 'authenticators' query parameter in authenticationendpoint
        return AUTHENTICATOR_NAME;
    }

    private String getFacebookUserIdentifier(Map<ClaimMapping, String> userAttributes) {

        //Treat 'id' claim sent by facebook as the user identifier
        Claim idClaim = new Claim();
        idClaim.setClaimId(0);
        idClaim.setClaimUri("id");

        ClaimMapping idClaimMapping = new ClaimMapping();
        idClaimMapping.setLocalClaim(idClaim);
        idClaimMapping.setRemoteClaim(idClaim);

        String facebookUserID = userAttributes.get(idClaimMapping);

        return facebookUserID;

    }

}
