package uz.mk.atmservice.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mk.atmservice.entity.*;
import uz.mk.atmservice.entity.enums.RoleName;
import uz.mk.atmservice.repository.*;
import uz.mk.atmservice.entity.Role;
import uz.mk.atmservice.entity.User;
import uz.mk.atmservice.repository.RoleRepository;
import uz.mk.atmservice.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BankRepository bankRepository;

    @Value("${spring.sql.init.mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            User user = new User(
                    "John",
                    "Doe",
                    "mirzohid.xasanov@mail.ru",
                    passwordEncoder.encode("123")
            );
            Role director = roleRepository.findByName(RoleName.ROLE_DIRECTOR);
            Bank ipak_yuli_banki = bankRepository.findByName("IPAK_YULI_BANKI");
            user.setBank(ipak_yuli_banki);
            user.setRoles(Collections.singleton(director));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
