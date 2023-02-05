package com.uni.sd.mappers;

import com.uni.sd.data.dto.UserDto;
import com.uni.sd.data.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUsername(userEntity.getUsername());
        userDto.setBirthday(userEntity.getBirthday());
        userDto.setUserType(userEntity.getUserType());
        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        User userEntity = new User();
        userEntity.setId(userDto.getId());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setBirthday(userDto.getBirthday());
        userEntity.setUserType(userDto.getUserType());
        return userEntity;
    }

}
