package com.dy.game.rpservice.exception;





import com.dy.game.rpcommon.response.ResponseUtil;
import com.dy.game.rpcommon.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 liulin
 * @描述 全局异常处理
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("rawtypes")
public class ExceptionConfig {
	
	/**
	 * BaseException自定义异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BaseException.class)
    public WebResponse handleException(BaseException ex) {
		log.info("----------------自定义异常BaseException异常处理-------------",ex);
		return ResponseUtil.fail(ex.getCode(),ex.getMessage());
    }

	/**
	 * 捕获MethodArgumentNotValidException参数不正确异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public WebResponse processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.info("----------------捕获MethodArgumentNotValidException参数不正确异常-------------",ex);
		List<Map<String, Object>> invalidArguments = new ArrayList<Map<String, Object>>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		String errors ="";
		for (FieldError fieldError : fieldErrors) {
			Map<String, Object> invalidArgument = new HashMap<String, Object>();
			invalidArgument.put("field", fieldError.getField());
			invalidArgument.put("rejectedValue", fieldError.getRejectedValue());
			invalidArgument.put("defaultMessage", fieldError.getDefaultMessage());
			errors=fieldError.getDefaultMessage();
            invalidArguments.add(invalidArgument);  
		}
        return ResponseUtil.fail(invalidArguments,"");
    }
	
    /**
     * 其他未知异常
     * @param ex
     * @return
     */
	@ExceptionHandler(Exception.class)
    public WebResponse unknownException(Exception ex) {
		log.info("----------------其他未知异常处理-------------",ex);
		return ResponseUtil.fail("系统内部错误，请联系管理员");
    }
	
}
