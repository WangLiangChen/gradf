package liangchen.wang.gradf.framework.data.pagination;

import liangchen.wang.gradf.framework.commons.validator.Assert;

/**
 * @author LiangChen.Wang
 */
public class OrderBy {
    private String orderBy;
    private String direction;

    public static OrderBy newInstance(String orderby, OrderByDirection direction) {
        return new OrderBy(orderby, direction);
    }

    public OrderBy(String orderby) {
        Assert.INSTANCE.notBlank(orderby, "参数orderby不能为空");
        this.orderBy = orderby;
        this.direction = OrderByDirection.asc.name();
    }

    public OrderBy(String orderby, OrderByDirection direction) {
        Assert.INSTANCE.notBlank(orderby, "参数orderby不能为空");
        Assert.INSTANCE.notNull(direction, "参数direction不能为空");
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
