package spring;

public class RegisterRequest {
  private String email;
  
  private String name;
  
  private String password;
  
  private String confirmPassword;
  
  public String getEmail() {
    return this.email;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getConfirmPassword() {
    return this.confirmPassword;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }
  
  public boolean isPasswordEqualToConfirmPassword() {
    return this.password.equals(this.confirmPassword);
  }
}
