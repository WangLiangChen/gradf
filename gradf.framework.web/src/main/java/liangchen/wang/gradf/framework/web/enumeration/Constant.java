package liangchen.wang.gradf.framework.web.enumeration;

/**
 * @author LiangChen.Wang
 */
public interface Constant {
    enum Path {
        /**
         *
         */
        ROOT("/"), MVC("/business/"), AUTH("/business/auth/");
        private String path;

        Path(String path) {
            this.path = path;
        }

        public String getPath(String suffixPattern) {
            return String.format("%s%s", this.path, suffixPattern);
        }
    }
}
