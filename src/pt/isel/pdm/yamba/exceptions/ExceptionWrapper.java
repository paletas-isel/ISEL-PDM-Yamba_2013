package pt.isel.pdm.yamba.exceptions;

public class ExceptionWrapper extends TwitterException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8380351350107921786L;

	public ExceptionWrapper(Exception e) {
		super(e.getMessage());
		// TODO Auto-generated constructor stub
	}

}
