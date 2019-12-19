package com.mastertek.service;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;

public class CustomUserManager extends AbstractUserManager{

	@Override
	public User getUserByName(String username) throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String username) throws FtpException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(User user) throws FtpException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doesExist(String username) throws FtpException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		// TODO Auto-generated method stub
		return null;
	}

}
