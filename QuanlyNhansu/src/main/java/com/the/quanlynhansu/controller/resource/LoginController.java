package com.the.quanlynhansu.controller.resource;


import com.the.quanlynhansu.model.dto.RoleDTO;
import com.the.quanlynhansu.model.dto.UserDTO;
import com.the.quanlynhansu.service.IUserservice;
import com.the.quanlynhansu.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private IUserservice userservice;

    @GetMapping(value = {"/login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/process-after-login")
    public String processAfterLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = userservice.findUserByUsername(username);
        if (CollectionUtils.isEmpty(userDTO.getRoleDTOS())) {
            return "redirect:/logout";
        }
        boolean isAdmin = false;
        boolean isUser = false;
        for (int i = 0; i < userDTO.getRoleDTOS().size(); i++) {
            RoleDTO roleDto = userDTO.getRoleDTOS().get(i);
            if (Constant.ROLE_ADMIN.equalsIgnoreCase(roleDto.getName())) {
                isAdmin = true;
            }
            if (Constant.ROLE_USER.equalsIgnoreCase(roleDto.getName())) {
                isUser = true;
            }
        }

        if (isAdmin) {
            return "redirect:/admin";
        }
        if (isUser) {
            return "redirect:/views/home";
        }
        return "redirect:/logout";
    }
}
