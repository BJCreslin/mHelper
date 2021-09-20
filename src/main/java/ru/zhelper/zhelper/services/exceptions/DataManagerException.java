package ru.zhelper.zhelper.services.exceptions;

public class DataManagerException extends RuntimeException {

	private static final long serialVersionUID = 4340326295999999261L;

	public static final String NON_EXISTING_LOAD_OR_DELETE_EXCEPTION = "Procurement with id %d does not exist.";
	public static final String COULD_NOT_SAVE_PROCUREMENT = "Could not save procurement!";

	public static final String COULD_NOT_LOAD_PROCUREMENT_NULL_DATA = "Could not load procurement: it is null or its ID is null!";
	public static final String COULD_NOT_SAVE_PROCUREMENT_NULL_DATA = "Could not save procurement: it is null!";
	public static final String COULD_NOT_DELETE_PROCUREMENT_NULL_DATA = "Could not delete procurement: it is null or its ID is null!";
	
	public DataManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
