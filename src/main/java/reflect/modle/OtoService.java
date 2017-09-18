package reflect.modle;

/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 上午10:53
 */
@GateWayMapping(value = "oto", version = "1.0")
public interface OtoService {
    @ApiMapping(value = "payment")
    void payment(String orderId, String price);

    @ApiMapping(value = "orderInfo")
    OrderDo getOrderInfo(String orderId, Integer price);

    @ApiMapping(value = "orderInfo")
    OrderDo getOrderParam(@ParamKey("test") String orderId, Integer price);
}