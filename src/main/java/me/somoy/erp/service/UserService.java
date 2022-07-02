package me.somoy.erp.service;

import me.somoy.erp.dao.UserRepository;
import me.somoy.erp.model.user.User;
import me.somoy.erp.utils.exception.EmployeeAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserService(){}

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) throws EmployeeAlreadyExistsException {
        User inDbName = userRepository.findByUsername(user.getUsername());
        User inDbEmail = userRepository.findByEmail(user.getEmail());
        if(inDbName != null || inDbEmail != null){
            //throw new DuplicateDataException("User already exists");
            throw new EmployeeAlreadyExistsException();
        }
        if(user.getAccessLevel() == null){
            user.setAccessLevel("USER");
        }
        if(user.getDateOfJoining() == null){
            user.setDateOfJoining(Calendar.getInstance().getTime());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public UserRepository getRepository(){
        return this.userRepository;
    }
    public User getUserById(Long id){
        return this.userRepository.findById(id).get();
    }

    public void deleteUserById(Long id){
        User user = getUserById(id);
        this.userRepository.delete(user);
    }
}
