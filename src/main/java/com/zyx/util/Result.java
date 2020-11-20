package com.zyx.util;



import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Page;

public class Result<T> implements Serializable {
    public static final Result OK = new Result(true);
    public static final Result FAIL = errorOf("操作失败");
    private static final String TOTAL = "total";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUMBER = "pageNumber";
    @ApiModelProperty("是否操作成功")
    private Boolean success;
    @ApiModelProperty("错误代码")
    private String errorCode;
    @ApiModelProperty("错误原因")
    private String msg;
    @ApiModelProperty("数据")
    private T data;
    private HashMap page;

    private Result(boolean success) {
        this.success = true;
        this.success = success;
    }

    private Result(boolean success, String errorCode, String msg) {
        this(success);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    private Result(T data) {
        this.success = true;
        this.success = true;
        this.data = data;
    }

    private Result(T data, int pageNum, int pageSize, long total) {
        this(data);
        this.page = new HashMap();
        this.page.put("pageNumber", pageNum);
        this.page.put("pageSize", pageSize);
        this.page.put("total", total);
    }

    public static Result dataOf(Object t) {
        return new Result(t);
    }

    public static Result pageOf(Page<?> page) {
        return pageOf(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public static Result pageOf(List<?> data, int pageNum, int pageSize, long total) {
        return new Result(data, pageNum, pageSize, total);
    }

    public static Result errorOf(String msg) {
        return errorOf("500", msg);
    }

    public static Result errorOf(String errorCode, String msg) {
        return new Result(false, errorCode, msg);
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public HashMap getPage() {
        return this.page;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setPage(final HashMap page) {
        this.page = page;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$success = this.getSuccess();
                    Object other$success = other.getSuccess();
                    if (this$success == null) {
                        if (other$success == null) {
                            break label71;
                        }
                    } else if (this$success.equals(other$success)) {
                        break label71;
                    }

                    return false;
                }

                Object this$errorCode = this.getErrorCode();
                Object other$errorCode = other.getErrorCode();
                if (this$errorCode == null) {
                    if (other$errorCode != null) {
                        return false;
                    }
                } else if (!this$errorCode.equals(other$errorCode)) {
                    return false;
                }

                label57: {
                    Object this$msg = this.getMsg();
                    Object other$msg = other.getMsg();
                    if (this$msg == null) {
                        if (other$msg == null) {
                            break label57;
                        }
                    } else if (this$msg.equals(other$msg)) {
                        break label57;
                    }

                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$page = this.getPage();
                Object other$page = other.getPage();
                if (this$page == null) {
                    if (other$page == null) {
                        return true;
                    }
                } else if (this$page.equals(other$page)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : $success.hashCode());
        Object $errorCode = this.getErrorCode();
        result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $page = this.getPage();
        result = result * 59 + ($page == null ? 43 : $page.hashCode());
        return result;
    }

    public String toString() {
        return "Result(success=" + this.getSuccess() + ", errorCode=" + this.getErrorCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", page=" + this.getPage() + ")";
    }

    public Result() {
        this.success = true;
    }
}
