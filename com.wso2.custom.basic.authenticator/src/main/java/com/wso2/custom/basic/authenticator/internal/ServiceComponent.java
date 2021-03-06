package com.wso2.custom.basic.authenticator.internal;

import java.util.Hashtable;

import com.wso2.custom.basic.authenticator.CustomBasicAuthenticator;
import com.wso2.custom.basic.authenticator.DataHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="com.wso2.custom.basic.authenticator.component" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */
public class ServiceComponent {

    private static Log log = LogFactory.getLog(ServiceComponent.class);

    private static RealmService realmService;

    protected void activate(ComponentContext ctxt) {

        CustomBasicAuthenticator basicAuth = new CustomBasicAuthenticator();
        Hashtable<String, String> props = new Hashtable<String, String>();

        ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(), basicAuth, props);

        if (log.isDebugEnabled()) {
            log.info("CustomBasicAuthenticator bundle is activated");
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.info("CustomBasicAuthenticator bundle is deactivated");
        }
    }

    protected void setRealmService(RealmService realmService) {
        log.debug("Setting the Realm Service");
        DataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        log.debug("UnSetting the Realm Service");
        DataHolder.getInstance().setRealmService(null);
    }

    public static RealmService getRealmService() {
        return realmService;
    }





}
