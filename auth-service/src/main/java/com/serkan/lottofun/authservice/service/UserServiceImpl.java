package com.serkan.lottofun.authservice.service;

import com.serkan.lottofun.authservice.dto.UserResponse;
import com.serkan.lottofun.authservice.entity.User;
import com.serkan.lottofun.authservice.exception.BaseException;
import com.serkan.lottofun.authservice.exception.ErrorMessage;
import com.serkan.lottofun.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> optinal = userRepository.findById(id);
        UserResponse userResponse = new UserResponse();
        if(optinal.isEmpty()){
            throw new BaseException(ErrorMessage.USER_NOT_FOUND);
        }
        User user = optinal.get();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ErrorMessage.USER_NOT_FOUND));

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    @Transactional
    public void updateBalance(Long id, BigDecimal amount) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new BaseException(ErrorMessage.USER_NOT_FOUND)
        );;
        user.setBalance(amount);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void decreaseBalance(Long userId, BigDecimal ticketPrice) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorMessage.USER_NOT_FOUND));
        if (user.getBalance().compareTo(ticketPrice) < 0) {
            throw new BaseException(ErrorMessage.INSUFFICIENT_BALANCE);
        }
        user.setBalance(user.getBalance().subtract(ticketPrice));

        userRepository.save(user);

    }

    @Override
    public void increaseBalance(Long userId, BigDecimal prizeAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorMessage.USER_NOT_FOUND));

        user.setBalance(user.getBalance().add(prizeAmount));
        userRepository.save(user);

    }
}
