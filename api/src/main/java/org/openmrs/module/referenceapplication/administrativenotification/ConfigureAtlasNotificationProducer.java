package org.openmrs.module.referenceapplication.administrativenotification;

import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.appframework.domain.AdministrativeNotification;
import org.openmrs.module.appframework.domain.AdministrativeNotificationAction;
import org.openmrs.module.appframework.factory.AdministrativeNotificationProducer;
import org.openmrs.module.atlas.AtlasConstants;
import org.openmrs.module.atlas.AtlasService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * If the Atlas has not been configured yet, and they haven't yet opted out, notify they should configure it.
 */
@Component
public class ConfigureAtlasNotificationProducer implements AdministrativeNotificationProducer {

    private boolean cachedDoNotShow = false;

    @Override
    public List<AdministrativeNotification> generateNotifications() {
        if (!ModuleFactory.isModuleStarted("atlas")) {
            return null;
        }

        if (cachedDoNotShow) {
            return null;
        }
        if (isConfigured() || optedOut()) {
            cachedDoNotShow = true;
            return null;
        }
        if (!Context.hasPrivilege(AtlasConstants.PRIV_MANAGE_ATLAS_DATA)) {
            return null;
        }

        AdministrativeNotification notification = new AdministrativeNotification();
        notification.setId("atlas.configure");
        notification.setIcon("icon-info-sign");
        notification.setCssClass("success");
        notification.setLabel("atlas.notification.label");
        notification.setRequiredPrivilege(AtlasConstants.PRIV_MANAGE_ATLAS_DATA);

        AdministrativeNotificationAction action = new AdministrativeNotificationAction();
        action.setId("atlas.configure");
        action.setLabel("atlas.notification.action.label");
        action.setUrl("/atlas/map.page");
        notification.setActions(Arrays.asList(action));

        return Arrays.asList(notification);
    }

    private boolean optedOut() {
        return Context.getService(AtlasService.class).getStopAskingToConfigure();
    }

    private boolean isConfigured() {
        return Context.getService(AtlasService.class).getAtlasData().getModuleEnabled();
    }

    public void clearCache() {
        cachedDoNotShow = false;
    }
}
