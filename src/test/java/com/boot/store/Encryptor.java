package com.boot.store;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;

public class Encryptor {

	@Test
	 public void getPass() {
	 BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	 textEncryptor.setPassword("hB0_kA1@eD05eE0`aA0(kB0)cG2+jE");
	 String pwd = textEncryptor.encrypt("123456");
	 System.out.println(pwd + "----------------");
	 pwd = textEncryptor.decrypt(pwd);
	 System.out.println(pwd + "----------------");
	 }
}
