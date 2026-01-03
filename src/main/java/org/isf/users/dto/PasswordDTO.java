package org.isf.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class PasswordDTO {
	@NotNull
	@Schema(description = "The name of the user whose password is to be changed", example = "admin")
	private String username;

	@NotNull
	@Schema(description = "The user's old password to changed", example = "21@U2g423", maxLength = 50)
	private String oldPasswd;

	@NotNull
	@Schema(description = "The user's new password", example = "new21@U2g423", maxLength = 50)
	private String newPasswd;

	public PasswordDTO() {}

	public PasswordDTO(String username, String oldPasswd, String newPasswd) {
		this.username = username;
		this.oldPasswd = oldPasswd;
		this.newPasswd = newPasswd;
	}

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getOldPasswd() { return oldPasswd; }
	public void setOldPasswd(String oldPasswd) { this.oldPasswd = oldPasswd; }

	public String getNewPasswd() { return newPasswd; }
	public void setNewPasswd(String newPasswd) { this.newPasswd = newPasswd; }

}
