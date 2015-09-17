package com.itanelse.mobileguard.domain;

/**
 * 关于版本信息的描述
 * @author 毓添
 *
 */
public class VersionBean {
	private int version;// 版本号
	private String url;// 下载路径
	private String desc;// 新版本描述
	private String versionName;//版本名

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "VersionBean [version=" + version + ", url=" + url + ", desc="
				+ desc + ", versionName=" + versionName + "]";
	}
}
