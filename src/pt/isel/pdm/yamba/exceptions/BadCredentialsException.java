package pt.isel.pdm.yamba.exceptions;

public class BadCredentialsException extends TwitterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4926243044371509207L;

	public BadCredentialsException() {
		super("Invalid credentials!");
		// TODO Auto-generated constructor stub
	}

}
