package org.jnuLib.data;

import org.jsoup.select.Elements;

public class InfoContent
{
			
		public Elements getEls() {
		return els;
	}
	public void setEls(Elements els) {
		this.els = els;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
		public Elements els;
		public String url;
}
