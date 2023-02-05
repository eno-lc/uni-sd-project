package com.uni.sd.data.service;

import com.uni.sd.data.dto.UserDto;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.entity.User;
import com.uni.sd.data.repository.UserRepository;
import com.uni.sd.mappers.UserMapper;
import com.uni.sd.security.SecurityService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.uni.sd.data.entity.SecurityUser;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public List<UserDto> findAllUsers(String filterText){

        List<User> users = userRepository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
        } else {
            List<User> userList = userRepository.search(filterText);
            return userList.stream()
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
        }
    }

    public List<UserDto> findAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos =  users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
        return userDtos;
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public void saveUser(UserDto user) {
        User userEntity = UserMapper.mapToUser(user);
        if(userEntity == null){
            Notification.show("User is null");
        }else{
            String password = userEntity.getPassword();
            String encodedPassword = new BCryptPasswordEncoder().encode(password);
            userEntity.setPassword(encodedPassword);
            userRepository.save(userEntity);
        }
    }

    public void updateUser(UserDto user) {

        User userEntity = UserMapper.mapToUser(user);

        if(userEntity == null){
            Notification.show("User is null");
        }else{
            String password = userEntity.getPassword();
            String encodedPassword = new BCryptPasswordEncoder().encode(password);
            userEntity.setPassword(encodedPassword);
            userRepository.save(userEntity);
        }
    }

    public UserDto getCurrentUser() {
        Optional<User> user =  userRepository.findByUsername(SecurityService.getUsername());
        UserDto userDto = UserMapper.mapToUserDto(user.get());
        return userDto;
    }
    public UserDto get(Long id) {
        Optional<User> user = userRepository.findById(id);
        User userEntity = user.get();
        return UserMapper.mapToUserDto(userEntity);
    }
    public Page<UserDto> list(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::mapToUserDto);
    }
}
