package com.wso2.custom.basic.authenticator.util;

import com.wso2.custom.basic.authenticator.DataHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;

import java.util.HashMap;

public class UserAssociationUtil {

    private static Log log = LogFactory.getLog(UserAssociationUtil.class);

    public static AuthenticatedUser getAssociatedLocalUserIDByFederatedUserId(String userID) {

        HashMap userAssociationMap = DataHolder.getInstance().getUserAssociationMap();

        AuthenticatedUser authenticatedUser = (AuthenticatedUser) userAssociationMap.get(userID);

        if (authenticatedUser != null) {
            log.info("getAssociatedLocalUserIDByFederatedUserId for federated userID: " + userID + ", found local user: " + authenticatedUser.getUserName() + " in userstore: " + authenticatedUser.getUserStoreDomain() + " in tenant: " + authenticatedUser.getTenantDomain());
        } else {
            log.info("getAssociatedLocalUserIDByFederatedUserId for federated userID: " + userID + ", no association found");
        }


        return authenticatedUser;

    }

    public static void createUserAssociation(String federatedUserID, AuthenticatedUser authenticatedUser) {

        log.info("creating user association for federated userID: " + federatedUserID + " with localUserID: " + authenticatedUser.getUserName() + " in userstore: " + authenticatedUser.getUserStoreDomain() + " in tenant: " + authenticatedUser.getTenantDomain());

        DataHolder.getInstance().getUserAssociationMap().put(federatedUserID, authenticatedUser);

    }
}
