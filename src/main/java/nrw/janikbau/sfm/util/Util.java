package nrw.janikbau.sfm.util;
// <- Import ->

// <- Static_Import ->

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static nrw.janikbau.sfm.util.Constants.LOCALE_DATE_TIME_FORMAT_STRING;

public class Util{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private static final DateTimeFormatter DATE_TIME_FORMATTER;

	// <- Static ->
	static{
		DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(LOCALE_DATE_TIME_FORMAT_STRING);
	}

	// <- Constructor ->
	private Util(){
		throw new IllegalStateException("Do not instantiate !~!");
	}

	// <- Abstract ->
	// <- Object ->
	// <- Getter & Setter ->

	// <- Static ->
	public static String HashToString(final byte[] hash){
		return new BigInteger(1, hash).toString(16);
	}

	public static byte[] Hash(final byte[] data) throws NoSuchAlgorithmException{
		final MessageDigest digest = MessageDigest.getInstance("SHA3-512");

		return digest.digest(data);
	}

	public static byte[] HexStringToByteArray(final String s){
		final BigInteger bigInt = new BigInteger(s, 16);

		final byte[] data = bigInt.toByteArray();

		if(data.length != bigInt.bitLength() / 8){
			final byte[] ret = new byte[data.length - 1];

			System.arraycopy(data, 1, ret, 0, ret.length);

			return ret;
		}

		return data;
	}

	public static String FormatDate(final LocalDateTime dateTime){
		return dateTime.format(DATE_TIME_FORMATTER);
	}

	public static LocalDateTime FormatDate(final String s){
		return LocalDateTime.parse(s, DATE_TIME_FORMATTER);
	}
}

/*
		if(files != null){
			for(final File client : files){
				if(client.isDirectory()){
					System.out.printf("Client:'%s'.\n", client.getName());

					final File[] jobSites = client.listFiles();

					if(jobSites != null){
						for(final File jobSite : jobSites){
							System.out.printf("\tJobSite:'%s'.\n", jobSite.getName());

							if(jobSite.isDirectory()){
								final File[] invoiceDirectories = jobSite.listFiles();

								if(invoiceDirectories != null){
									for(final File invoiceDirectory : invoiceDirectories){
										if(invoiceDirectory.isDirectory()){
											System.out.printf("\t\tDir:'%s'.\n", invoiceDirectory.getName());
										}
									}
								}
							}
						}
					}
				}
			}
		}
* */