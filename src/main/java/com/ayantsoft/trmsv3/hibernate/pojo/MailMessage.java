package com.ayantsoft.trmsv3.hibernate.pojo;
// Generated 14 Sep, 2017 12:05:51 PM by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MailMessage generated by hbm2java
 */
@Entity
@Table(name = "mail_message", catalog = "trmsv2_hr_sales")
public class MailMessage implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 313419975120137989L;
	
	private Integer id;
	private String message;
	private String messageFor;

	public MailMessage() {
	}

	public MailMessage(String message, String messageFor) {
		this.message = message;
		this.messageFor = messageFor;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "message", length = 65535)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "message_for", length = 45)
	public String getMessageFor() {
		return this.messageFor;
	}

	public void setMessageFor(String messageFor) {
		this.messageFor = messageFor;
	}

}
