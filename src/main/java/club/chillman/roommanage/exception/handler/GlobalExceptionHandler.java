package club.chillman.roommanage.exception.handler;


import club.chillman.roommanage.exception.RoomManageException;
import club.chillman.roommanage.utils.ExceptionUtil;
import club.chillman.roommanage.utils.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public R error(Exception e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }
    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据
    public R error(ArithmeticException e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理..");
    }

    @ExceptionHandler(RoomManageException.class)
    @ResponseBody
    public R error(RoomManageException e){
        log.error(ExceptionUtil.getMessage(e));
        e.printStackTrace();
        if (e.getCode() == 666) {
            return R.mid().message(e.getMsg()).code(e.getCode());
        }
        return R.error().message(e.getMsg()).code(e.getCode());
    }


}
