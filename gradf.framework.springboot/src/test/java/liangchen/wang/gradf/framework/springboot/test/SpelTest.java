package liangchen.wang.gradf.framework.springboot.test;

import org.junit.jupiter.api.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author LiangChen.Wang 2020/10/20
 */
public class SpelTest {
    @Test
    public void testArray() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("array", new int[]{7, 8, 9, 10});
        Object value = parser.parseExpression("#array[5]").getValue(context);
        System.out.println(value);
    }
}
