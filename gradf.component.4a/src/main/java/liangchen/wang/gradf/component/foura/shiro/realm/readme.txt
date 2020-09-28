简单扩展Shiro实现  类似
organization:create OR organization:update OR organization:delete

 ( organization:create Or organization:update ) OR  NOT organization:delete

 ( organization:create && organization:update ) OR  ! organization:delete

 

其中操作符不限大小写，支持and、or、not，以及&&、||、！

唯一缺点就是为了解析方便，所有内容必须用空格隔开


看到了这篇博客：http://jinnianshilongnian.iteye.com/blog/1864800

可以用逆波兰表达式实现复杂一些的表达式解析

实现思路：

1.将字符串分割成字符串集合

（类似： ( organization:create Or organization:update ) OR  NOT organization:delete  

就可以分割成[(, organization:create, Or, organization:update, ), OR, , NOT, organization:delete]这样的集合 ）

2.然后将该集合转换为逆波兰表达式（此处将操作符做了忽略大小写的操作）：

（上例就可以转换为：[organization:create, organization:update, or, organization:delete, not, or]）

3.再将其中除了操作符以外的权限字符串，用shiro的验证方法转为true  或者是  false

（转换为：[false, true, or, true, not, or]）

4.然后再求最终逆波兰表达式的值，大功告成！