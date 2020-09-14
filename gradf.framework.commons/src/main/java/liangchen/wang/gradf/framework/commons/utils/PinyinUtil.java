package liangchen.wang.gradf.framework.commons.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * @author WangLiangChen
 */
public enum PinyinUtil {
    /**
     * instance
     */
    INSTANCE;
    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public Pinyin toPinyin(String cn) {
        String jianpin = "", pinyin = "";
        char[] array = cn.toCharArray();
        String first = null;
        for (char c : array) {
            if (c < 128) {
                continue;
            }
            try {
                String[] swap = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (CollectionUtil.INSTANCE.isEmpty(swap)) {
                    continue;
                }
                first = swap[0];
            } catch (Exception e) {
                continue;
            }
            pinyin += first;
            jianpin += String.valueOf(first.charAt(0));
        }
        String shoupin = "";
        if (StringUtil.INSTANCE.isNotBlank(jianpin)) {
            shoupin = String.valueOf(jianpin.charAt(0));
        }
        Pinyin entity = new Pinyin();
        entity.setPinyin(pinyin);
        entity.setJianpin(jianpin);
        entity.setShoupin(shoupin);
        return entity;
    }

    public class Pinyin {
        private String pinyin;
        private String jianpin;
        private String shoupin;

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getJianpin() {
            return jianpin;
        }

        public void setJianpin(String jianpin) {
            this.jianpin = jianpin;
        }

        public String getShoupin() {
            return shoupin;
        }

        public void setShoupin(String shoupin) {
            this.shoupin = shoupin;
        }
    }
}
