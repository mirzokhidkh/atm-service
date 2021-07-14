package uz.mk.atmservice.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.mk.atmservice.entity.Role;
import uz.mk.atmservice.entity.User;
import uz.mk.atmservice.entity.enums.RoleName;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CommonUtils {

    public static Map<String, Object> getPrincipalAndRoleFromSecurityContextHolder() {
        Map<String, Object> map = new HashMap<String, Object>();

        User principalUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principalUser = (User) authentication.getPrincipal();

        Set<Role> principalUserRoles = principalUser.getRoles();
        map.put("principalUser", principalUser);
        map.put("principalUserRoles", principalUserRoles);
        return map;
    }

    public static boolean isExistsAuthority(Set<Role> roles, RoleName roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public static boolean isExistsAuthority(Set<Role> roles, String roleName) {
        return roles.stream().anyMatch(role -> role.getName().name().contains(roleName));
    }


    public static Integer generateCode() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
