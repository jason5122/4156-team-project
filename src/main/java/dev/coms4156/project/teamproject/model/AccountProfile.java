package dev.coms4156.project.teamproject.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents an account profile for users of the service.
 * An account can either be a provider profile or a recipient profile.
 */
@Entity
public class AccountProfile implements Serializable {

  @Serial
  private static final long serialVersionUID = 123456L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id", updatable = false, nullable = false)
  private int accountId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)  
  @JoinColumn(name = "client", nullable = false)  
  private ClientProfile client;

  private final AccountType accountType;
  private String phoneNumber;
  private String name;

  /**
   * Enum for specifying whether the account is a food provider or a recipient.
   */
  public enum AccountType {
    PROVIDER,
    RECIPIENT
  }

  /**
   * Constructs a new Account Profile object.
   *
   * @param accountId unique ID of the account
   * @param accountType type of account (provider or recipient)
   * @param phoneNumber phone number for contact (validated for length)
   * @param name name associated with the account
   */
  public AccountProfile(ClientProfile client, AccountType accountType, 
      String phoneNumber, String name) {
    this.client = client;
    this.accountType = accountType;
    // [TODO] Check if phoneNumber string is only digits
    if (phoneNumber == null || !(phoneNumber.length() == 10 || phoneNumber.length() == 11)) {
      throw new IllegalArgumentException("Phone number must be 10 or 11 digits.");
    }
    this.phoneNumber = phoneNumber;
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty.");
    }
    this.name = name;
  }

  /**
   * Gets the account ID.
   *
   * @return The account ID.
   */
  public int getAccountId() {
    return accountId;
  }

  /**
   * Gets the account type (provider or recipient).
   *
   * @return The account type.
   */
  public AccountType getAccountType() {
    return accountType;
  }

  /**
   * Gets the phone number.
   *
   * @return The phone number.
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Gets the name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  public ClientProfile getClient() {
    return client;
  }

  /**
   * Provides a formatted string representing the account profile details.
   *
   * @return A string with account profile details.
   */
  @Override
  public String toString() {
    return "AccountProfile{" 
           + "accountId='" + accountId + '\'' 
           + ", accountType=" + accountType 
           + ", phoneNumber='" + phoneNumber + '\'' 
           + ", name='" + name + '\'' 
           + '}';
  }
}
