package sg.ic.asteroidmonitor.util;

import sg.ic.asteroidmonitor.dto.BaseResponse;

public class ResponseUtil {

    private ResponseUtil() {
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setData(data);

        return response;
    }
}
