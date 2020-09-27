package liangchen.wang.gradf.component.foura.enumeration;

public enum AccountStatus {
	NONE("无状态"), INITIAL("初始"), NORMAL("正常"),PENDING("待审核"),REJECT("驳回"),SUSPEND("暂停"),AUTOLOCKED("自动锁定"), LOCKED("锁定"), PASSWORD_EXPIRED("密码过期"), EXPIRED("过期"), DELETED("删除");
	private String text;

	AccountStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
