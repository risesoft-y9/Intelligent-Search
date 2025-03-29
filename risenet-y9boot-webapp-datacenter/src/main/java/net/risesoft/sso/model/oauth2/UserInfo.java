package net.risesoft.sso.model.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
	private static final long serialVersionUID = -867112844100528415L;

	private String tenantID;
	
	private String tenantLoginName;
	
	private String tenantName;
	
	private String personID;
	
	private String loginName;
	
	private Integer sex;
	
	@JsonProperty("CAID")
	private String CAID;
	
	private String email;
	
	private String mobile;
	
	private String guidPath;
	
	private String loginType;
	
	private String parentID;
	
	private String isValidateIE;
	
	private Integer original;
	
	private String originalID;
	
	private String positionId;
	
	private Boolean tenantManager = false;
	
	private String avator;
	
	private String roles;
	
	private String dn;
	
	private String name;
	
	private String personType;
}