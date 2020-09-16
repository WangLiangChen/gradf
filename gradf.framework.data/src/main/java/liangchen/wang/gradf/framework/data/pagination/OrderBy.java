package liangchen.wang.gradf.framework.data.pagination;

/**
 * @author LiangChen.Wang
 */
public class OrderBy {
    private String orderBy;
    private String direction;

    public OrderBy() {

    }

    public static OrderBy newInstance(String orderby, OrderByDirection direction) {
        return new OrderBy(orderby, direction);
    }

    public OrderBy(String orderby, OrderByDirection direction) {
        this.orderBy = orderby;
        this.direction = direction.name();
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDirection() {
        return direction;
    }

}
