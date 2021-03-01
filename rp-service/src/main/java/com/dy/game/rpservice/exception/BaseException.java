package com.dy.game.rpservice.exception;


import com.dy.game.rpcommon.enums.ResCode;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -6783551049743441442L;

	private Integer code;//返回编码

	private String message="";//返回消息

	private Object data;//返回数据


	public BaseException(String message){
		this.message=message;
	}
	public BaseException(ResCode resCode){
		this.message=resCode.getMessage();
		this.code=resCode.getCode();
	}
	public BaseException(Integer code, String message){
		this.message=message;
		this.code=code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
