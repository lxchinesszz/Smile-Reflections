package reflect.modle;

/**
 * @Package: reflect.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/18 上午11:01
 */
@GateWayMapping(value = "oto",version = "1.0")
public class OtoServiceImpl implements OtoService {

    @ApiMapping(value = "payment")
    public void payment(String orderId, String price) {
        System.out.println(orderId+":**********:"+price);
    }

    @ApiMapping(value = "orderInfo")
    public OrderDo getOrderInfo(String orderId,Integer price) {
        return OrderDo.builder().orderId(orderId).price("1.1").userId("liuxin").build();
    }

    @Override
    public OrderDo getOrderParam(@ParamKey("test") String orderId,Integer price) {
        return null;
    }
}
