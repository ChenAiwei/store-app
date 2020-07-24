package com.boot.store.handler;

import com.boot.store.enums.ResultEnum;
import com.boot.store.exception.PermissionException;
import com.boot.store.exception.ServiceException;
import com.boot.store.exception.TokenException;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author aiwei
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

	@Value("${spring.profiles.active}")
	private String profiles;

	private static final String PRO_FILE_STR = "prod";
    /**
     * http请求的方法不正确
     *      
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultVo<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("http请求的方法不正确:【" + e.getMessage() + "】");
        return ResultVoUtil.error(ResultEnum.RequestMethodNotAllowed);
    }

    /**
     * 请求参数不全
     *      
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResultVo<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("请求参数不全:【" + e.getMessage() + "】");
        return ResultVoUtil.error(ResultEnum.MissingServletRequestParameter);
    }

    /**
     * 请求参数类型不正确
     *      
     */
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResultVo<Object> typeMismatchExceptionHandler(TypeMismatchException e) {
        log.error("请求参数类型不正确:【" + e.getMessage() + "】");
        return ResultVoUtil.error(ResultEnum.TypeMismatchException);
    }


    /**
     * RequestBody 为空
     *      
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultVo<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("RequestBody为空:【" + e.getMessage() + "】");
        return ResultVoUtil.error(ResultEnum.RequestBodyEmpty);
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResultVo<String> serviceExceptionHandler(HttpServletRequest req, ServiceException e) {
        log.error(e.getMessage());
        return ResultVoUtil.error(e.getMessage());
    }

	/**
	 * 处理token异常
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = TokenException.class)
	@ResponseBody
	public ResultVo<String> tokenExceptionHandler(HttpServletRequest req, TokenException e) {
		log.error(e.getMessage());
		return ResultVoUtil.error(ResultEnum.TokenError);
	}

	/**
	 * 处理权限异常
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = PermissionException.class)
	@ResponseBody
	public ResultVo<String> permissionExceptionHandler(HttpServletRequest req, PermissionException e) {
		log.error(e.getMessage());
		return ResultVoUtil.error(ResultEnum.PermissionError);
	}
	/**
	 * 404异常
	 *      
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
	public ResultVo<Object> noHandlerFoundException(NoHandlerFoundException e) {
		log.error("404接口不存在:【" + e.getMessage() + "】");
		return ResultVoUtil.error(ResultEnum.InterfaceNotExist);
	}
    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVo<String> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("发生异常！原因是:", e);
        if (PRO_FILE_STR.equals(profiles)) {
            return ResultVoUtil.error("系统异常");
        }
        int count = 0;
        StringBuffer stringBuffer = new StringBuffer();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            stringBuffer.append(stackTraceElement.toString());
            if (count++ > 15) {
                break;
            }
        }
        return ResultVoUtil.error(stringBuffer.toString());
    }

	/**
	 * 表单验证
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResultVo<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error(e.getMessage(), e);
		BindingResult bindingResult = e.getBindingResult();
		return ResultVoUtil.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
	}
}
