package ca.javateacher.studentdata.passwords;

public interface PasswordGenerator {
    String randomPassword();
    String randomPassword(int length);
}
