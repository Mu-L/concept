package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.ServiceRouterLocation;
import com.netflix.loadbalancer.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class RibbonServiceRouterLocation implements ServiceRouterLocation {

    @NonNull
    private String serviceId;

    @NonNull
    private Server server;

    @NonNull
    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return server.getHost();
    }

    @Override
    public int getPort() {
        return server.getPort();
    }
}