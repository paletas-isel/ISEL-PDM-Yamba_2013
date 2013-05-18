package pt.isel.pdm.yamba.exceptions;

public abstract class TwitterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 987845002890118355L;

	public TwitterException(String msg) {
		super(msg);
	}
	
}
