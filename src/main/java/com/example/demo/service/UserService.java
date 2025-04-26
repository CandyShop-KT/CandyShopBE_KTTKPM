package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.AddressRequestDTO;
import com.example.demo.dto.AddressRequestUpdateDTO;
import com.example.demo.dto.ChangeEmailRequestDTO;
import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.CreateUserRequestDTO;
import com.example.demo.dto.UserProfileRequestDTO;
import com.example.demo.dto.VerifyUserRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.model.enums.Role;

public interface UserService {
	
	public User getUserById(String userId);
	
	public User updateUser(String userId, UserProfileRequestDTO profileRequestDTO)
			throws Exception, ResourceNotFoundException;

	public void changePassword(String userId, ChangePasswordRequestDTO changePasswordRequestDTO)
			throws Exception, ResourceNotFoundException;
	
	public User uploadAvatar(String userId, MultipartFile multipartFile) throws IOException, Exception;
	
	public User changeEmail(String userId, ChangeEmailRequestDTO changeEmailRequestDTO) throws Exception, ResourceNotFoundException;
	
	public Address createAddress(String userId, AddressRequestDTO address) throws Exception, ResourceNotFoundException;
	
	public Address updateAddress(String userId, String addressId, AddressRequestUpdateDTO addressRequestUpdateDTO) throws Exception, ResourceNotFoundException;
	
	public void deleteAddress(String userId, String addressId) throws Exception, ResourceNotFoundException;
	
	public Address getAddress(String userId, String addressId) throws Exception, ResourceNotFoundException;
	
	public List<Address> getAddresses(String userId) throws Exception, ResourceNotFoundException;
	
	public User verifyUser(String userId, VerifyUserRequest verifyUserRequest) throws Exception;
	public List<User> getAllUsers();
	
	public User updateUserRole(String userId, Role newRole);
	
	public User createUserByAdmin(CreateUserRequestDTO dto) throws Exception;
}
