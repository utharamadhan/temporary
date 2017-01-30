package id.base.app.webMember.rest;

import id.base.app.exception.SystemException;

public class IdentificationRestCaller {
	public void submitNewPassword(Long userPK, String oldPassword,
			String newPassword,String confirmationPassword,String userName) throws SystemException {
		//TODO implement submit new password
	}
	
	public boolean validateOldPassword(Long userPK, String password) throws SystemException {
		// TODO implement validate old password
		return false;
	}
	
	public void addHistory(String password, Long fkAppUser) throws SystemException {
		// TODO implement add history password
	}
	
	public void changePassword(Long userPK,String oldPassword,String newPassword,String confirmationPassword,String userName) throws SystemException {
		// TODO implement change password
	}
	
}
