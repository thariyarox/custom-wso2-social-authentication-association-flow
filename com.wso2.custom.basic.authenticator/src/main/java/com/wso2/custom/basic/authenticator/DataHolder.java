package com.wso2.custom.basic.authenticator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.HashMap;

public class DataHolder {

    private static RealmService realmService;
    private static volatile DataHolder dataHolder;
    private static CustomBasicAuthenticator customBasicAuthenticator;
    private static boolean isRealmServiceSet = false;
    private static HashMap<String, AuthenticatedUser> userAssociationMap;


    private static Log log = LogFactory.getLog(DataHolder.class);


    private DataHolder() {

        log.info("----------------------------------------- creating data holder instance");

    }

    public static DataHolder getInstance() {

        if (dataHolder == null) {

            synchronized (DataHolder.class) {
                if (dataHolder == null) {
                    dataHolder = new DataHolder();
                    customBasicAuthenticator = new CustomBasicAuthenticator();
                    userAssociationMap = new HashMap<String, AuthenticatedUser>();
                    isRealmServiceSet = false;
                }
            }

        }

        return dataHolder;
    }

    public void setRealmService(RealmService realmService) {

        if(!isRealmServiceSet){
            //realm searvice is not already set
            this.realmService = realmService;
            isRealmServiceSet = true;
        }
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public CustomBasicAuthenticator getCustomBasicAuthenticator() {
        return customBasicAuthenticator;
    }

    public HashMap getUserAssociationMap(){
        return userAssociationMap;
    }

}
