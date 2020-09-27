package liangchen.wang.gradf.component.foura.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public enum PasswordUtil {
	INSTANCE;
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	private String algorithmName = "md5";

	private int hashIterations = 2;

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
	public String randomSalt(){
		return randomNumberGenerator.nextBytes().toHex();
	}

	// salt=account_id+salt;
	public Password encryptPassword(long account_id, String password) {
		Password p = new Password();
		String password_salt = randomNumberGenerator.nextBytes().toHex();
		p.setPassword_salt(password_salt);
		password_salt = account_id + password_salt;
		password = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(password_salt), hashIterations).toHex();
		p.setPassword(password);
		return p;
	}

	public String encryptPassword(long account_id, String password, String password_salt) {
		password_salt = account_id + password_salt;
		password = new SimpleHash(algorithmName, password, ByteSource.Util.bytes(password_salt), hashIterations).toHex();
		return password;
	}

	public static class Password {
		private String password;
		private String password_salt;

		public Password(){
			
		}
		public Password(String password, String password_salt) {
			this.password = password;
			this.password_salt = password_salt;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPassword_salt() {
			return password_salt;
		}

		public void setPassword_salt(String password_salt) {
			this.password_salt = password_salt;
		}
	}
}
