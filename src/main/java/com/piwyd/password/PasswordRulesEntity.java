package com.piwyd.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "password")
public class PasswordRulesEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private Boolean requireLowercase;

	@Column
	private Boolean requireUppercase;

	@Column
	private Boolean requireNumbers;

	@Column
	private Boolean requireSpecialCharacters;

	@Column
	@Min(1)
	private Integer minLength;

	@Column
	private Integer expirationTime;
}
