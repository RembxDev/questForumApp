package org.it.questforumapp.config;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class UserLoginListener implements ApplicationListener<ApplicationEvent> {

    @Getter
    private static int loggedInUsers = 0;

    @Override
    public synchronized void onApplicationEvent(final ApplicationEvent event) {

        if (event instanceof AuthenticationSuccessEvent) {
            loggedInUsers++;
        }

        else if (event instanceof LogoutSuccessEvent) {
            loggedInUsers--;
        }
    }

}

