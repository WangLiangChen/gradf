package liangchen.wang.gradf.framework.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiangChen.Wang
 */
@RestController
@RequestMapping(value = "/")
public class CsrfController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void root() {
    }

    @RequestMapping(value = "/csrf", method = RequestMethod.GET)
    public void csrf() {
    }
}
