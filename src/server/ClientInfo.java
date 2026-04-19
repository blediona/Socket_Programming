package server;

import java.net.SocketAddress;
import java.util.Set;

public class ClientInfo {
    private final String username;
    private final SocketAddress address;
    private final Set<Permission>permissions;

    public ClientInfo(String username, SocketAddress address, Set<Permission>permissions){
        this.username = username;
        this.address = address;
        this.permissions = permissions;
    }

    public String getUsername(){
        return username;
    }

    public SocketAddress getAddress(){
        return address;
    }

    public Set<Permission> getPermissions(){
        return permissions;
    }

    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }

}
