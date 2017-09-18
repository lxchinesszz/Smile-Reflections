package reflect.modle;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Builder;
import lombok.Data;

/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 上午10:54
 */
@Data
@Builder
public class OrderDo {
    @Ignore
    private String userId;
    @Ignore
    private int userType;

    private String orderId;

    private String price;
}
