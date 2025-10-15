package org.company.finance.common.util;

/**
 * 自定义异常
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public ApiException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public ApiException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public ApiException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public ApiException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
